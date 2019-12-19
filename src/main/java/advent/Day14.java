package advent;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.math.IntMath;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14
{
	private Logger logger = LoggerFactory.getLogger(Day14.class);

	private Map<String, Reaction> reactions;
	private Map<String, Integer> wastage = new HashMap<>();

	public int calculatePart1(Resource resource) throws IOException
	{
		process(resource);

		Map<String, Integer> amountPerMaterial = recurse(reactions.get("FUEL"), 1);

		amountPerMaterial.forEach((k, v) -> logger.debug("{} : {}", k, v));
		logger.debug("wasted materials {}", wastage);

		return amountPerMaterial.get("ORE");
	}

	Map<String, Integer> recurse(Reaction start, int requiredUnits)
	{
		logger.debug("make {}, output-units={}, need units: {}", start.getOutput().getName(),
						start.getOutput().getAmount(), requiredUnits);

		Map<String, Integer> amountPerMaterial = new HashMap<>();

		// may have to make more than needed
		int mustProduceAmount = mustMakeAmount(start.output.amount, requiredUnits);

		int fromWaste = wastage.getOrDefault(start.output.name, 0);

		logger.debug("found {} of {} in waste", fromWaste, start.output.name);
		if (requiredUnits <= fromWaste)
		{
			logger.debug("more than enough of {} in waste", start.output.name);
			wastage.merge(start.output.name, -requiredUnits, (v1, v2) -> v1 + v2);
			Preconditions.checkState(wastage.getOrDefault(start.output.name, 0) >= 0);
			return amountPerMaterial;
		}
		else if (fromWaste > 0)
		{
			// there might be a smaller figure that is more efficient to use waste
			int tryWithWasteProduceAmount = mustMakeAmount(start.output.amount, requiredUnits - fromWaste);
			logger.debug("using {} waste, still need to make {}", fromWaste, tryWithWasteProduceAmount);
			if (tryWithWasteProduceAmount < mustProduceAmount)
			{
				logger.debug("using some waste units");
				wastage.merge(start.output.name, -fromWaste, (v1, v2) -> v1 + v2);
				Preconditions.checkState(wastage.getOrDefault(start.output.name, 0) >= 0);
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
		Preconditions.checkState(wastage.getOrDefault(start.output.name, 0) >= 0);

		Validate.isTrue(mustProduceAmount > 0);

		for (Material inp : start.inputs)
		{
			Reaction childReaction = reactions.get(inp.name);
			if (childReaction != null)
			{
				Map<String, Integer> thisAmount = recurse(childReaction, mustProduceAmount * inp.getAmount());
				thisAmount.forEach((k, v) -> amountPerMaterial.merge(k, v, (v1, v2) -> v1 + v2));
			}
			else
			{
				int runs = mustProduceAmount / start.output.amount;
				Validate.isTrue(mustProduceAmount % start.output.amount == 0);

				int actualOreAmount = runs * inp.amount;
				logger.debug("at bottom level {} input-amount={}, mustProduceAmount={},actualOreAmount={},runs={}",
								inp.getName(), inp.getAmount(), mustProduceAmount, actualOreAmount, runs);

				amountPerMaterial.merge(inp.getName(), actualOreAmount, (v1, v2) -> v1 + v2);
			}
		}

		return amountPerMaterial;
	}

	//	public static int howMuchToMake2(Material material, int required)
	//	{
	//		material.getAmount();
	//	}

	public static int mustMakeAmount(int outputForSingleRun, int requiredUnits)
	{
		int ceiling = (requiredUnits + outputForSingleRun - 1) / outputForSingleRun;
		int result = ceiling * outputForSingleRun;
		return result;
	}

	public static int howMuchToMake(int in, int out, int required)
	{
		int newRequired = Math.max(required, out);
		int multiple = IntMath.divide(newRequired, out, RoundingMode.HALF_UP);
		return multiple * in;
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
