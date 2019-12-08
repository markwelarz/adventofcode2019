package advent;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class Day8Test
{
	@Test
	public void t1() throws Exception
	{
		Day8 day8 = new Day8();
		day8.calculatePart1(resource("012345"
						+ "123456"
						+ "234567"
						+ "345678"
						+ "456789"
						+ "567890"
						+ "678901"
						+ "789012"), 5, 2);
	}

	@Test
	public void part1() throws Exception
	{
		Day8 day8 = new Day8();
		int answer = day8.calculatePart1(new ClassPathResource("advent/day8input.txt"), 25, 6);
		assertThat(answer).isEqualTo(1742);
	}

	@Test
	public void part2Example() throws Exception
	{
		Day8 day8 = new Day8();
		day8.calculatePart2(resource("0222112222120000"), 2, 2);
	}

	@Test
	public void part2() throws Exception
	{
		Day8 day8 = new Day8();
		day8.calculatePart2(new ClassPathResource("advent/day8input.txt"), 25, 6);
	}

	@Test
	public void combineLayers() throws Exception
	{
		Day8 day8 = new Day8();
		assertThat(day8.combineLayers("0222112222120000", 2, 2)).isEqualTo("0110");
	}

	private Resource resource(String program) throws IOException
	{
		return new ByteArrayResource(IOUtils.toByteArray(new StringReader(program), StandardCharsets.UTF_8));
	}
}
