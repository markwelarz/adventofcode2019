package advent;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class Day14Test
{
	@Test
	public void parseLine1()
	{
		String input = "14 ORE => 15 A";
		Day14 day14 = new Day14();
		Day14.Reaction reaction = day14.parseLine(input);
		assertThat(reaction.inputs).extracting("amount", "name")
						.contains(tuple(14, "ORE"));
		assertThat(reaction.output).hasFieldOrPropertyWithValue("amount", 15)
						.hasFieldOrPropertyWithValue("name", "A");
	}

	@Test
	public void parseLine2()
	{
		String input = "7 A, 1 B => 1 C";
		Day14 day14 = new Day14();
		Day14.Reaction reaction = day14.parseLine(input);
		assertThat(reaction.inputs).extracting("amount", "name")
						.contains(tuple(7, "A"))
						.contains(tuple(1, "B"));
		assertThat(reaction.output).hasFieldOrPropertyWithValue("amount", 1)
						.hasFieldOrPropertyWithValue("name", "C");
	}

	@Test
	public void parseLine3()
	{
		String input = "2 AB, 3 BC, 4 CA => 1 FUEL";
		Day14 day14 = new Day14();
		Day14.Reaction reaction = day14.parseLine(input);
		assertThat(reaction.inputs).extracting("amount", "name")
						.contains(tuple(2, "AB"))
						.contains(tuple(3, "BC"))
						.contains(tuple(4, "CA"));
		assertThat(reaction.output).hasFieldOrPropertyWithValue("amount", 1)
						.hasFieldOrPropertyWithValue("name", "FUEL");
	}

	@Test
	public void parseAll() throws Exception
	{
		String input = "10 ORE => 10 A\n"
						+ "1 ORE => 1 B\n"
						+ "7 A, 1 B => 1 C\n"
						+ "7 A, 1 C => 1 D\n"
						+ "7 A, 1 D => 1 E\n"
						+ "7 A, 1 E => 1 FUEL";
		Day14 day14 = new Day14();
		List<Day14.Reaction> reactions = day14.parse(stringResource(input));

		assertThat(reactions.get(0).inputs).extracting("amount", "name").contains(tuple(10, "ORE"));
		assertThat(reactions.get(0).output).hasFieldOrPropertyWithValue("amount", 10)
						.hasFieldOrPropertyWithValue("name", "A");

		assertThat(reactions.get(1).inputs).extracting("amount", "name").contains(tuple(1, "ORE"));
		assertThat(reactions.get(1).output).hasFieldOrPropertyWithValue("amount", 1)
						.hasFieldOrPropertyWithValue("name", "B");

		assertThat(reactions.get(2).inputs).extracting("amount", "name").contains(tuple(7, "A"), tuple(1, "B"));
		assertThat(reactions.get(2).output).hasFieldOrPropertyWithValue("amount", 1)
						.hasFieldOrPropertyWithValue("name", "C");

		assertThat(reactions.get(3).inputs).extracting("amount", "name").contains(tuple(7, "A"), tuple(1, "C"));
		assertThat(reactions.get(3).output).hasFieldOrPropertyWithValue("amount", 1)
						.hasFieldOrPropertyWithValue("name", "D");

		assertThat(reactions.get(4).inputs).extracting("amount", "name").contains(tuple(7, "A"), tuple(1, "D"));
		assertThat(reactions.get(4).output).hasFieldOrPropertyWithValue("amount", 1)
						.hasFieldOrPropertyWithValue("name", "E");

		assertThat(reactions.get(5).inputs).extracting("amount", "name").contains(tuple(7, "A"), tuple(1, "E"));
		assertThat(reactions.get(5).output).hasFieldOrPropertyWithValue("amount", 1)
						.hasFieldOrPropertyWithValue("name", "FUEL");
	}

	@Test
	public void part1Example1() throws Exception
	{
		Day14 day14 = new Day14();
		int amount = day14.calculatePart1(stringResource("10 ORE => 10 A\n"
						+ "1 ORE => 1 B\n"
						+ "7 A, 1 B => 1 C\n"
						+ "7 A, 1 C => 1 D\n"
						+ "7 A, 1 D => 1 E\n"
						+ "7 A, 1 E => 1 FUEL"));
		assertThat(amount).isEqualTo(31);
	}

	@Test
	public void part1Example2() throws Exception
	{
		Day14 day14 = new Day14();
		int amount = day14.calculatePart1(stringResource("9 ORE => 2 A\n"
						+ "8 ORE => 3 B\n"
						+ "7 ORE => 5 C\n"
						+ "3 A, 4 B => 1 AB\n"
						+ "5 B, 7 C => 1 BC\n"
						+ "4 C, 1 A => 1 CA\n"
						+ "2 AB, 3 BC, 4 CA => 1 FUEL"));
		assertThat(amount).isEqualTo(165);
	}

	@Test
	public void part1Example3() throws Exception
	{
		Day14 day14 = new Day14();
		int amount = day14.calculatePart1(stringResource("157 ORE => 5 NZVS\n"
						+ "165 ORE => 6 DCFZ\n"
						+ "44 XJWVT, 5 KHKGT, 1 QDVJ, 29 NZVS, 9 GPVTF, 48 HKGWZ => 1 FUEL\n"
						+ "12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ\n"
						+ "179 ORE => 7 PSHF\n"
						+ "177 ORE => 5 HKGWZ\n"
						+ "7 DCFZ, 7 PSHF => 2 XJWVT\n"
						+ "165 ORE => 2 GPVTF\n"
						+ "3 DCFZ, 7 NZVS, 5 HKGWZ, 10 PSHF => 8 KHKGT"));
		assertThat(amount).isEqualTo(13312);
	}

	@Test
	public void part1Example4() throws Exception
	{
		Day14 day14 = new Day14();
		int amount = day14.calculatePart1(stringResource("2 VPVL, 7 FWMGM, 2 CXFTF, 11 MNCFX => 1 STKFG\n"
						+ "17 NVRVD, 3 JNWZP => 8 VPVL\n"
						+ "53 STKFG, 6 MNCFX, 46 VJHF, 81 HVMC, 68 CXFTF, 25 GNMV => 1 FUEL\n"
						+ "22 VJHF, 37 MNCFX => 5 FWMGM\n"
						+ "139 ORE => 4 NVRVD\n"
						+ "144 ORE => 7 JNWZP\n"
						+ "5 MNCFX, 7 RFSQX, 2 FWMGM, 2 VPVL, 19 CXFTF => 3 HVMC\n"
						+ "5 VJHF, 7 MNCFX, 9 VPVL, 37 CXFTF => 6 GNMV\n"
						+ "145 ORE => 6 MNCFX\n"
						+ "1 NVRVD => 8 CXFTF\n"
						+ "1 VJHF, 6 MNCFX => 4 RFSQX\n"
						+ "176 ORE => 6 VJHF"));
		assertThat(amount).isEqualTo(180697);
	}

	@Test
	public void part1Example5() throws Exception
	{
		Day14 day14 = new Day14();
		int amount = day14.calculatePart1(stringResource("171 ORE => 8 CNZTR\n"
						+ "7 ZLQW, 3 BMBT, 9 XCVML, 26 XMNCP, 1 WPTQ, 2 MZWV, 1 RJRHP => 4 PLWSL\n"
						+ "114 ORE => 4 BHXH\n"
						+ "14 VRPVC => 6 BMBT\n"
						+ "6 BHXH, 18 KTJDG, 12 WPTQ, 7 PLWSL, 31 FHTLT, 37 ZDVW => 1 FUEL\n"
						+ "6 WPTQ, 2 BMBT, 8 ZLQW, 18 KTJDG, 1 XMNCP, 6 MZWV, 1 RJRHP => 6 FHTLT\n"
						+ "15 XDBXC, 2 LTCX, 1 VRPVC => 6 ZLQW\n"
						+ "13 WPTQ, 10 LTCX, 3 RJRHP, 14 XMNCP, 2 MZWV, 1 ZLQW => 1 ZDVW\n"
						+ "5 BMBT => 4 WPTQ\n"
						+ "189 ORE => 9 KTJDG\n"
						+ "1 MZWV, 17 XDBXC, 3 XCVML => 2 XMNCP\n"
						+ "12 VRPVC, 27 CNZTR => 2 XDBXC\n"
						+ "15 KTJDG, 12 BHXH => 5 XCVML\n"
						+ "3 BHXH, 2 VRPVC => 7 MZWV\n"
						+ "121 ORE => 7 VRPVC\n"
						+ "7 XCVML => 6 RJRHP\n"
						+ "5 BHXH, 4 VRPVC => 5 LTCX"));
		assertThat(amount).isEqualTo(2210736);
	}

	@Test
	public void howMuchToMake()
	{
		assertThat(Day14.howMuchToMake(10, 10, 7)).isEqualTo(10);
		assertThat(Day14.howMuchToMake(7, 1, 1)).isEqualTo(7);
		assertThat(Day14.howMuchToMake(9, 2, 3)).isEqualTo(18);
		assertThat(Day14.howMuchToMake(9, 1, 1)).isEqualTo(9);
		assertThat(Day14.howMuchToMake(9, 2, 2)).isEqualTo(9);
		assertThat(Day14.howMuchToMake(9, 2, 3)).isEqualTo(18);
		assertThat(Day14.howMuchToMake(9, 2, 9)).isEqualTo(45);
	}

	@Test
	public void mustMakeAmount()
	{
		assertThat(Day14.mustMakeAmount(10, 10)).isEqualTo(10);
		assertThat(Day14.mustMakeAmount(1, 2)).isEqualTo(2);
		assertThat(Day14.mustMakeAmount(3, 7)).isEqualTo(9);
		assertThat(Day14.mustMakeAmount(8, 1)).isEqualTo(8);
	}

	private Resource stringResource(String code)
	{
		return new InputStreamResource(IOUtils.toInputStream(code, StandardCharsets.UTF_8));
	}
}
