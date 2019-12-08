package advent;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;

import java.nio.charset.StandardCharsets;

public class Day5Test
{
	@Test
	public void t1() throws Exception
	{
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(IOUtils.toInputStream("1002,4,3,4,33", StandardCharsets.UTF_8)));
	}

	@Test
	public void t2() throws Exception
	{
		System.setIn(IOUtils.toInputStream("5", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new ClassPathResource("advent/day5input.txt"));
	}

	@Test
	public void t3() throws Exception
	{
		System.setIn(IOUtils.toInputStream("8", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,9,8,9,10,9,4,9,99,-1,8", StandardCharsets.UTF_8)));
		System.out.println("* should output 1");
	}

	@Test
	public void t4() throws Exception
	{
		System.setIn(IOUtils.toInputStream("7", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,9,8,9,10,9,4,9,99,-1,8", StandardCharsets.UTF_8)));
		System.out.println("* should output 0");
	}

	@Test
	public void t5() throws Exception
	{
		System.setIn(IOUtils.toInputStream("4", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,9,7,9,10,9,4,9,99,-1,8", StandardCharsets.UTF_8)));
		System.out.println("* should output 1");
	}

	@Test
	public void t6() throws Exception
	{
		System.setIn(IOUtils.toInputStream("9", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,9,7,9,10,9,4,9,99,-1,8", StandardCharsets.UTF_8)));
		System.out.println("* should output 0");
	}

	@Test
	public void t7() throws Exception
	{
		System.setIn(IOUtils.toInputStream("8", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,3,1108,-1,8,3,4,3,99", StandardCharsets.UTF_8)));
		System.out.println("* should output 1");
	}

	@Test
	public void t8() throws Exception
	{
		System.setIn(IOUtils.toInputStream("6", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,3,1108,-1,8,3,4,3,99", StandardCharsets.UTF_8)));
		System.out.println("* should output 0");
	}

	@Test
	public void t9() throws Exception
	{
		System.setIn(IOUtils.toInputStream("6", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,3,1107,-1,8,3,4,3,99", StandardCharsets.UTF_8)));
		System.out.println("* should output 1");
	}

	@Test
	public void t10() throws Exception
	{
		System.setIn(IOUtils.toInputStream("9", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,3,1107,-1,8,3,4,3,99", StandardCharsets.UTF_8)));
		System.out.println("* should output 0");
	}

	@Test
	public void t11() throws Exception
	{
		System.setIn(IOUtils.toInputStream("0", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9",
														StandardCharsets.UTF_8)));
		System.out.println("* should output 0");
	}

	@Test
	public void t12() throws Exception
	{
		System.setIn(IOUtils.toInputStream("5", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9",
														StandardCharsets.UTF_8)));
		System.out.println("* should output 1");
	}

	@Test
	public void t13() throws Exception
	{
		System.setIn(IOUtils.toInputStream("0", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,3,1105,-1,9,1101,0,0,12,4,12,99,1",
														StandardCharsets.UTF_8)));
		System.out.println("* should output 0");
	}

	@Test
	public void t14() throws Exception
	{
		System.setIn(IOUtils.toInputStream("2", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,3,1105,-1,9,1101,0,0,12,4,12,99,1",
														StandardCharsets.UTF_8)));
		System.out.println("* should output 1");
	}

	@Test
	public void t16() throws Exception
	{
		System.setIn(IOUtils.toInputStream("9", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,"
																		+ "1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,"
																		+ "999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99",
														StandardCharsets.UTF_8)));
		System.out.println("* should output 1001");
	}

	@Test
	public void t17() throws Exception
	{
		System.setIn(IOUtils.toInputStream("8", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,"
																		+ "1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,"
																		+ "999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99",
														StandardCharsets.UTF_8)));
		System.out.println("* should output 1000");
	}

	@Test
	public void t18() throws Exception
	{
		System.setIn(IOUtils.toInputStream("2", StandardCharsets.UTF_8));
		Day5 day5 = new Day5();
		day5.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,"
																		+ "1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,"
																		+ "999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99",
														StandardCharsets.UTF_8)));
		System.out.println("* should output 999");
	}
}
