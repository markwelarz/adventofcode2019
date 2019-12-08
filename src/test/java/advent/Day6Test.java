package advent;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class Day6Test
{
	@Test
	public void part1Example1() throws Exception
	{
		Day6 day6 = new Day6();
		int answer = day6.calculate(new InputStreamResource(
						IOUtils.toInputStream("COM)B\nB)C\nC)D\nB)E", StandardCharsets.UTF_8)));
		assertThat(answer).isEqualTo(8);
	}

	@Test
	public void part1Example2() throws Exception
	{
		Day6 day6 = new Day6();
		int answer = day6.calculate(new InputStreamResource(
						IOUtils.toInputStream("COM)B\n"
										+ "B)C\n"
										+ "C)D\n"
										+ "D)E\n"
										+ "E)F\n"
										+ "B)G\n"
										+ "G)H\n"
										+ "D)I\n"
										+ "E)J\n"
										+ "J)K\n"
										+ "K)L", StandardCharsets.UTF_8)));
		assertThat(answer).isEqualTo(42);
	}

	@Test
	public void part2Example1() throws Exception
	{
		Day6 day6 = new Day6();
		int answer = day6.calculatePart2(new InputStreamResource(
						IOUtils.toInputStream("COM)B\n"
										+ "B)C\n"
										+ "C)D\n"
										+ "D)E\n"
										+ "E)F\n"
										+ "B)G\n"
										+ "G)H\n"
										+ "D)I\n"
										+ "E)J\n"
										+ "J)K\n"
										+ "K)L\n"
										+ "K)YOU\n"
										+ "I)SAN", StandardCharsets.UTF_8)));
		assertThat(answer).isEqualTo(4);
	}

	@Test
	public void part1Input() throws Exception
	{
		Day6 day6 = new Day6();
		int answer = day6.calculate(new ClassPathResource("advent/day6input.txt"));
		assertThat(answer).isEqualTo(278744);
	}

	@Test
	public void part2Input() throws Exception
	{
		Day6 day6 = new Day6();
		int answer = day6.calculatePart2(new ClassPathResource("advent/day6input.txt"));
		assertThat(answer).isEqualTo(475);
	}
}
