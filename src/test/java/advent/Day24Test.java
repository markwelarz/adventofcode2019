package advent;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class Day24Test
{
	@Test
	public void testParse() throws Exception
	{
		String input = "....#\n"
						+ "#..#.\n"
						+ "#..##\n"
						+ "..#..\n"
						+ "#....\n";

		Day24 day24 = new Day24();
		day24.parseInput(stringResource(input));

		assertThat(day24.getGrid(0)).containsExactly(
						new int[] { 0, 0, 0, 0, 1 },
						new int[] { 1, 0, 0, 1, 0 },
						new int[] { 1, 0, 0, 1, 1 },
						new int[] { 0, 0, 1, 0, 0 },
						new int[] { 1, 0, 0, 0, 0 });
	}

	@Test
	public void part1Example1() throws Exception
	{
		Day24 day24 = new Day24();
		Resource input = stringResource(
						"....#\n"
										+ "#..#.\n"
										+ "#..##\n"
										+ "..#..\n"
										+ "#....");
		day24.parseInput(input);
		day24.advanceMinutePart1(0);
		assertThat(day24.getGrid(0)).containsExactly(
						new int[] { 1, 0, 0, 1, 0 },
						new int[] { 1, 1, 1, 1, 0 },
						new int[] { 1, 1, 1, 0, 1 },
						new int[] { 1, 1, 0, 1, 1 },
						new int[] { 0, 1, 1, 0, 0 });
	}

	@Test
	public void part1Example2() throws Exception
	{
		Day24 day24 = new Day24();
		Resource input = stringResource(
						"....#\n"
										+ "#..#.\n"
										+ "#..##\n"
										+ "..#..\n"
										+ "#....");
		day24.parseInput(input);
		day24.advanceMinutePart1(0);
		day24.advanceMinutePart1(0);
		assertThat(day24.getGrid(0)).containsExactly(
						new int[] { 1, 1, 1, 1, 1 },
						new int[] { 0, 0, 0, 0, 1 },
						new int[] { 0, 0, 0, 0, 1 },
						new int[] { 0, 0, 0, 1, 0 },
						new int[] { 1, 0, 1, 1, 1 });
	}

	@Test
	public void part1Example3() throws Exception
	{
		Day24 day24 = new Day24();
		Resource input = stringResource(
						"....#\n"
										+ "#..#.\n"
										+ "#..##\n"
										+ "..#..\n"
										+ "#....");
		day24.parseInput(input);
		day24.advanceMinutePart1(0);
		day24.advanceMinutePart1(0);
		day24.advanceMinutePart1(0);
		assertThat(day24.getGrid(0)).containsExactly(
						new int[] { 1, 0, 0, 0, 0 },
						new int[] { 1, 1, 1, 1, 0 },
						new int[] { 0, 0, 0, 1, 1 },
						new int[] { 1, 0, 1, 1, 0 },
						new int[] { 0, 1, 1, 0, 1 });
	}

	@Test
	public void part1Example4() throws Exception
	{
		Day24 day24 = new Day24();
		Resource input = stringResource(
						"....#\n"
										+ "#..#.\n"
										+ "#..##\n"
										+ "..#..\n"
										+ "#....");
		day24.parseInput(input);
		day24.advanceMinutePart1(0);
		day24.advanceMinutePart1(0);
		day24.advanceMinutePart1(0);
		day24.advanceMinutePart1(0);
		assertThat(day24.getGrid(0)).containsExactly(
						new int[] { 1, 1, 1, 1, 0 },
						new int[] { 0, 0, 0, 0, 1 },
						new int[] { 1, 1, 0, 0, 1 },
						new int[] { 0, 0, 0, 0, 0 },
						new int[] { 1, 1, 0, 0, 0 });
	}

	@Test
	public void part1Example5() throws Exception
	{
		Day24 day24 = new Day24();
		Resource input = stringResource(
						"....#\n"
										+ "#..#.\n"
										+ "#..##\n"
										+ "..#..\n"
										+ "#....");
		day24.part1(input);
		assertThat(day24.getGrid(0)).containsExactly(
						new int[] { 0, 0, 0, 0, 0 },
						new int[] { 0, 0, 0, 0, 0 },
						new int[] { 0, 0, 0, 0, 0 },
						new int[] { 1, 0, 0, 0, 0 },
						new int[] { 0, 1, 0, 0, 0 });
	}

	@Test
	public void biodiversityTest() throws Exception
	{
		Day24 day24 = new Day24();
		Resource input = stringResource(
						"....#\n"
										+ "#..#.\n"
										+ "#..##\n"
										+ "..#..\n"
										+ "#....");
		day24.part1(input);
		int rating = day24.biodiversityRating(0);
		assertThat(rating).isEqualTo(2129920);
	}

	@Test
	public void part1() throws Exception
	{
		Day24 day24 = new Day24();
		day24.part1(new ClassPathResource("advent/day24input.txt"));
		int rating = day24.biodiversityRating(0);
		assertThat(rating).isEqualTo(28772955);
	}

	@Test
	public void part2() throws Exception
	{
		Day24 day24 = new Day24();
		int bugs = day24.part2(new ClassPathResource("advent/day24input.txt"), 200);
		assertThat(bugs).isEqualTo(2023);
	}

	@Test
	public void part2Example1() throws Exception
	{
		String input = "....#\n"
						+ "#..#.\n"
						+ "#..##\n"
						+ "..#..\n"
						+ "#....\n";

		Day24 day24 = new Day24();
		int bugs = day24.part2(stringResource(input), 10);
		assertThat(bugs).isEqualTo(99);
	}

	private Resource stringResource(String code)
	{
		return new InputStreamResource(IOUtils.toInputStream(code, StandardCharsets.UTF_8));
	}
}
