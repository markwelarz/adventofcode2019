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

public class Day7Part2Test
{
	@Test
	public void part2Example1() throws Exception
	{
		Day7Part2 day7Part2 = new Day7Part2(resource("3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,"
						+ "27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5"));
		int result = day7Part2.chainInputOutput(9, 8, 7, 6, 5);
		assertThat(result).isEqualTo(139629729);
	}

	@Test
	public void part2Example1FindMax() throws Exception
	{
		int result = Day7Part2.maxOutputPart2(resource("3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,"
						+ "27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5"));
		assertThat(result).isEqualTo(139629729);
	}

	@Test
	public void part2Example2() throws Exception
	{
		Day7Part2 day7Part2 = new Day7Part2(
						resource("3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,"
										+ "-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,"
										+ "53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10"));
		int result = day7Part2.chainInputOutput(9, 7, 8, 5, 6);
		assertThat(result).isEqualTo(18216);
	}

	@Test
	public void part2Example2FindMax() throws Exception
	{
		int result = Day7Part2.maxOutputPart2(
						resource("3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,"
										+ "-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,"
										+ "53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10"));
		assertThat(result).isEqualTo(18216);
	}

	@Test
	public void part2() throws Exception
	{
		int result = Day7Part2.maxOutputPart2(new ClassPathResource("advent/day7input.txt"));
		assertThat(result).isEqualTo(61019896);
	}

	private Resource resource(String program) throws IOException
	{
		return new ByteArrayResource(IOUtils.toByteArray(new StringReader(program), StandardCharsets.UTF_8));
	}
}
