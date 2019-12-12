package advent;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Day11Test
{
	@Test
	public void part1Test() throws Exception
	{
		Day11 day11 = new Day11();
		int panels = day11.part1();
		assertThat(panels).isEqualTo(2343);
	}

	@Test
	public void part2Test() throws Exception
	{
		Day11 day11 = new Day11();
		int panels = day11.part2();
	}
}
