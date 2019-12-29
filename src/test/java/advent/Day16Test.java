package advent;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class Day16Test
{
	/*
	0+0-0+0-0+0-0+0-
	00++00--00++00--
	000+++000---000+
	0000++++0000----

	 */
	@Test
	public void part1Example1Phase1() throws Exception
	{
		Day16 day16 = new Day16();
		String result = day16.applyFftPart1(1, "12345678");
		assertThat(result).isEqualTo("48226158");
	}

	@Test
	public void part1Example1Phase2() throws Exception
	{
		Day16 day16 = new Day16();
		String result = day16.applyFftPart1(2, "12345678");
		assertThat(result).isEqualTo("34040438");
	}

	@Test
	public void part1Example1Phase3() throws Exception
	{
		Day16 day16 = new Day16();
		String result = day16.applyFftPart1(3, "12345678");
		assertThat(result).isEqualTo("03415518");
	}

	@Test
	public void part1Example1Phase4() throws Exception
	{
		Day16 day16 = new Day16();
		String result = day16.applyFftPart1(4, "12345678");
		assertThat(result).isEqualTo("01029498");
	}

	@Test
	public void part1Example2() throws Exception
	{
		Day16 day16 = new Day16();
		String result = day16.applyFftPart1(100, "80871224585914546619083218645595");
		assertThat(result).isEqualTo("24176176");
	}

	@Test
	public void part1Example3() throws Exception
	{
		Day16 day16 = new Day16();
		String result = day16.applyFftPart1(100, "19617804207202209144916044189917");
		assertThat(result).isEqualTo("73745418");
	}

	@Test
	public void part1Example4() throws Exception
	{
		Day16 day16 = new Day16();
		String result = day16.applyFftPart1(100, "69317163492948606335995924319873");
		assertThat(result).isEqualTo("52432133");
	}

	@Test
	public void part1() throws Exception
	{
		Day16 day16 = new Day16();
		String input = IOUtils.toString(new ClassPathResource("advent/day16input.txt").getInputStream(),
						StandardCharsets.UTF_8);
		String result = day16.applyFftPart1(100, input);
		assertThat(result).isEqualTo("30379585");
	}

	@Nested
	class Part2Examples
	{
		@Test
		public void part2Example1() throws Exception
		{
			Day16 day16 = new Day16();
			String result = day16.applyFftPart2(100, "03036732577212944063491565474664");
			assertThat(result).isEqualTo("84462026");
		}

		@Test
		public void part2Example2() throws Exception
		{
			Day16 day16 = new Day16();
			String result = day16.applyFftPart2(100, "02935109699940807407585447034323");
			assertThat(result).isEqualTo("78725270");
		}

		@Test
		public void part2Example3() throws Exception
		{
			Day16 day16 = new Day16();
			String result = day16.applyFftPart2(100, "03081770884921959731165446850517");
			assertThat(result).isEqualTo("53553731");
		}
	}

	@Test
	public void part2() throws Exception
	{
		Day16 day16 = new Day16();
		String input = IOUtils.toString(new ClassPathResource("advent/day16input.txt").getInputStream(),
						StandardCharsets.UTF_8);
		String result = day16.applyFftPart2(100, input);
		assertThat(result).isEqualTo("22808931");
	}
}
