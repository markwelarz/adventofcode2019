package advent;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Day1Test
{
	Day1 day1 = new Day1();

	@Test
	public void test1() throws Exception
	{
		int total = day1.part1();
		System.out.println(total);
	}

	@Test
	public void test2() throws Exception
	{
		int total = day1.part2();
		System.out.println(total);
	}

	@Test
	public void test2a() throws Exception
	{
		int total = day1.calculateFuel(100756);
		assertThat(total).isEqualTo(50346);
	}

	@Test
	public void test2b() throws Exception
	{
		int total = day1.calculateFuel(1969);
		assertThat(total).isEqualTo(966);
	}
}