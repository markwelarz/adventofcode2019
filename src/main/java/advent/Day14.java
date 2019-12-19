package advent;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14
{
	private Logger logger = LoggerFactory.getLogger(Day14.class);

	private Map<String, Reaction> reactions;
	private Map<String, Long> wastage = new HashMap<>();

	public long calculatePart1(Resource resource) throws IOException
	{
		process(resource);
		return computeOre(1);
	}

	public long computeOre(int requiredFuel)
	{
		wastage.clear();
		Map<String, Long> amountPerMaterial = recurse(reactions.get("FUEL"), requiredFuel);

		amountPerMaterial.forEach((k, v) -> logger.debug("{} : {}", k, v));
		logger.debug("wasted materials {}", wastage);

		return amountPerMaterial.get("ORE");
	}

	int highestFuelLessThan1Trillion = 0;

	int searchForTrillionOre(int from, int to)
	{
		long ORE = 1000000000000L;

		if (to >= from)
		{
			int fuel = from + (to - from) / 2;

			logger.info("trying fuel={}", fuel);
			long ore = computeOre(fuel);
			if (ore <= ORE)
				highestFuelLessThan1Trillion = Math.max(highestFuelLessThan1Trillion, fuel);

			logger.info("result was {}", ore);
			if (ore == ORE)
				return fuel;

			if (ore > ORE)
				return searchForTrillionOre(from, fuel - 1);

			return searchForTrillionOre(fuel + 1, to);
		}

		return highestFuelLessThan1Trillion;
	}

	public int calculatePart2(Resource resource) throws IOException
	{
		process(resource);
		return searchForTrillionOre(1, 83000000);
	}

	Map<String, Long> recurse(Reaction start, long requiredUnits)
	{
		logger.debug("make {}, output-units={}, need units: {}", start.getOutput().getName(),
						start.getOutput().getAmount(), requiredUnits);

		logger.debug("waste is {}", wastage);

		Map<String, Long> amountPerMaterial = new HashMap<>();

		// may have to make more than needed
		long mustProduceAmount = mustMakeAmount(start.output.amount, requiredUnits);
		logger.debug("minimum can produce is {}", mustProduceAmount);

		long fromWaste = wastage.getOrDefault(start.output.name, 0L);

		logger.debug("found {} of {} in waste", fromWaste, start.output.name);
		if (requiredUnits <= fromWaste)
		{
			logger.debug("more than enough of {} in waste", start.output.name);
			wastage.merge(start.output.name, -requiredUnits, (v1, v2) -> v1 + v2);
			Preconditions.checkState(wastage.getOrDefault(start.output.name, 0L) >= 0);
			return amountPerMaterial;
		}
		else if (fromWaste > 0)
		{
			// there might be a smaller figure that is more efficient to use waste
			long tryWithWasteProduceAmount = mustMakeAmount(start.output.amount, requiredUnits - fromWaste);
			logger.debug("using {} waste, still need to make {}", fromWaste, tryWithWasteProduceAmount);
			if (tryWithWasteProduceAmount < mustProduceAmount)
			{
				logger.debug("using some waste units");
				wastage.merge(start.output.name, -fromWaste, (v1, v2) -> v1 + v2);
				wastage.merge(start.output.name, tryWithWasteProduceAmount + fromWaste - requiredUnits,
								(v1, v2) -> v1 + v2);

				Preconditions.checkState(wastage.getOrDefault(start.output.name, 0L) >= 0);
				mustProduceAmount = tryWithWasteProduceAmount;
			}
			else
				logger.debug("using waste had no benefit");
		}

		// now calculate waste
		if (mustProduceAmount > requiredUnits)
		{
			logger.debug("adding {} to waste", mustProduceAmount - requiredUnits);
			wastage.merge(start.output.name, mustProduceAmount - requiredUnits, (v1, v2) -> v1 + v2);
		}
		Preconditions.checkState(wastage.getOrDefault(start.output.name, 0L) >= 0);

		Validate.isTrue(mustProduceAmount > 0);

		for (Material inp : start.inputs)
		{
			Reaction childReaction = reactions.get(inp.name);
			if (childReaction != null)
			{
				long productNeeded = oreNeeded(inp.amount, start.output.amount, mustProduceAmount);

				Map<String, Long> thisAmount = recurse(childReaction, productNeeded);
				thisAmount.forEach((k, v) -> amountPerMaterial.merge(k, v, (v1, v2) -> v1 + v2));
			}
			else
			{
				long oreNeeded = oreNeeded(inp.amount, start.output.amount, mustProduceAmount);
				Validate.isTrue(mustProduceAmount % start.output.amount == 0);

				logger.debug("at bottom level {} input-amount={}, mustProduceAmount={},oreNeeded={}",
								inp.getName(), inp.getAmount(), mustProduceAmount, oreNeeded);

				amountPerMaterial.merge(inp.getName(), oreNeeded, (v1, v2) -> v1 + v2);
			}
		}

		return amountPerMaterial;
	}

	public long oreNeeded(long inputMaterialAmount1Run, long outputMaterialAmount1Run, long productAmountRequired)
	{
		long actualOreAmount = (productAmountRequired / outputMaterialAmount1Run) * inputMaterialAmount1Run;
		return actualOreAmount;
	}

	public static long mustMakeAmount(long outputForSingleRun, long requiredUnits)
	{
		long ceiling = (requiredUnits + outputForSingleRun - 1) / outputForSingleRun;
		long result = ceiling * outputForSingleRun;
		return result;
	}

	public void process(Resource resource) throws IOException
	{
		List<Reaction> reactionList = parse(resource);
		reactions = Maps.uniqueIndex(reactionList, v -> v.getOutput().getName());
	}

	public List<Reaction> parse(Resource resource) throws IOException
	{
		String input = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
		List<Reaction> reactions = new ArrayList<>();

		List<String> lines = Splitter.on("\n").splitToList(input);
		for (String line : lines)
		{
			Reaction reaction = parseLine(line);
			reactions.add(reaction);
		}

		return reactions;
	}

	Reaction parseLine(String line)
	{
		List<Material> materials = new ArrayList<>();
		Reaction reaction = new Reaction();

		String inputs = StringUtils.substringBefore(line, "=>");
		List<String> inputMaterials = Splitter.on(",").trimResults().splitToList(inputs);

		for (String material : inputMaterials)
		{
			int amount = Integer.parseInt(StringUtils.substringBefore(material, " "));
			String materialName = StringUtils.substringAfter(material, " ");
			materials.add(new Material(amount, materialName));
		}

		String output = StringUtils.substringAfter(line, "=>").trim();
		int amount = Integer.parseInt(StringUtils.substringBefore(output, " "));
		String materialName = StringUtils.substringAfter(output, " ");

		reaction.inputs = materials;
		reaction.output = new Material(amount, materialName);

		return reaction;
	}

	static class Reaction
	{
		List<Material> inputs = new ArrayList<>();
		Material output;

		public List<Material> getInputs()
		{
			return inputs;
		}

		public Material getOutput()
		{
			return output;
		}
	}

	static class Material
	{
		int amount;
		String name;

		public Material(int amount, String name)
		{
			this.amount = amount;
			this.name = name;
		}

		public int getAmount()
		{
			return amount;
		}

		public String getName()
		{
			return name;
		}
	}
}
