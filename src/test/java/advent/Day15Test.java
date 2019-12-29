package advent;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

public class Day15Test
{
	@Test
	public void part1() throws Exception
	{
		Day15 day15 = new Day15(new ClassPathResource("advent/day15input.txt"));
		int result = day15.part1();
		assertThat(result).isEqualTo(294);
	}

	@Test
	public void part2() throws Exception
	{
		Day15 day15 = new Day15(new ClassPathResource("advent/day15input.txt"));
		int result = day15.part2();
		assertThat(result).isLessThan(783);
		assertThat(result).isEqualTo(388);
	}
}

