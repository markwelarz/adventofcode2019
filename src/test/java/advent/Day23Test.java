package advent;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.assertj.core.api.Assertions.assertThat;

public class Day23Test
{
	@Test
	public void part1() throws Exception
	{
		Resource program = new ClassPathResource("advent/day23input.txt");
		Day23 day23 = new Day23(program, 50);
		long answer = day23.part1();
		assertThat(answer).isEqualTo(17541);
	}

	@Test
	public void part2() throws Exception
	{
		Resource program = new ClassPathResource("advent/day23input.txt");
		Day23 day23 = new Day23(program, 50);
		long answer = day23.part2();
		assertThat(answer).isLessThan(12881);
		assertThat(answer).isGreaterThan(11672);
		assertThat(answer).isEqualTo(12415);
	}
}
