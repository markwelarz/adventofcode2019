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

public class Day7Part1Test
{
	@Test
	public void t0()
	{

	}

	@Test
	public void part1Example1() throws Exception
	{
		Day7Part1 day7Part1 = new Day7Part1();
		int result = day7Part1.chainInputOutputOnce(resource("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0"),
						4, 3, 2, 1, 0);
		assertThat(result).isEqualTo(43210);
	}

	@Test
	public void part1Example2() throws Exception
	{
		Day7Part1 day7Part1 = new Day7Part1();
		int result = day7Part1.chainInputOutputOnce(resource("3,23,3,24,1002,24,10,24,1002,23,-1,23,"
										+ "101,5,23,23,1,24,23,23,4,23,99,0,0"),
						0, 1, 2, 3, 4);
		assertThat(result).isEqualTo(54321);
	}

	@Test
	public void part1Example3() throws Exception
	{
		Day7Part1 day7Part1 = new Day7Part1();
		int result = day7Part1.chainInputOutputOnce(resource("3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,"
										+ "1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0"),
						1, 0, 4, 3, 2);
		assertThat(result).isEqualTo(65210);
	}

	@Test
	public void part1() throws Exception
	{
		Day7Part1 day7Part1 = new Day7Part1();
		int result = day7Part1.maxOutputPart1(new ClassPathResource("advent/day7input.txt"));
		assertThat(result).isEqualTo(101490);
	}

	private Resource resource(String program) throws IOException
	{
		return new ByteArrayResource(IOUtils.toByteArray(new StringReader(program), StandardCharsets.UTF_8));
	}
}
