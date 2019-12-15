package advent;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class Day12Test
{
	private Logger logger = LoggerFactory.getLogger(Day12Test.class);

	@Test
	public void stepExample1()
	{
		String input = "<x=-1, y=0, z=2>\n"
						+ "<x=2, y=-10, z=-7>\n"
						+ "<x=4, y=-8, z=8>\n"
						+ "<x=3, y=5, z=-1>";
		Day12 day12 = new Day12();
		day12.readInput(input);
		day12.doSteps(1);
		List<Moon> moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(2, -1, 1, 3, -1, -1),
										tuple(3, -7, -4, 1, 3, 3),
										tuple(1, -7, 5, -3, 1, -3),
										tuple(2, 2, 0, -1, -3, 1));
		//After `3` step:
		day12.doSteps(1);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(5, -3, -1, 3, -2, -2),
										tuple(1, -2, 2, -2, 5, 6),
										tuple(1, -4, -1, 0, 3, -6),
										tuple(1, -4, 2, -1, -6, 2));

		//After 3 steps:
		day12.doSteps(1);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(5, -6, -1, 0, -3, 0),
										tuple(0, 0, 6, -1, 2, 4),
										tuple(2, 1, -5, 1, 5, -4),
										tuple(1, -8, 2, 0, -4, 0));

		//After 4 steps:
		day12.doSteps(1);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(2, -8, 0, -3, -2, 1),
										tuple(2, 1, 7, 2, 1, 1),
										tuple(2, 3, -6, 0, 2, -1),
										tuple(2, -9, 1, 1, -1, -1));

		//After 5 steps:
		day12.doSteps(1);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(-1, -9, 2, -3, -1, 2),
										tuple(4, 1, 5, 2, 0, -2),
										tuple(2, 2, -4, 0, -1, 2),
										tuple(3, -7, -1, 1, 2, -2));

		//After 6 steps:
		day12.doSteps(1);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(-1, -7, 3, 0, 2, 1),
										tuple(3, 0, 0, -1, -1, -5),
										tuple(3, -2, 1, 1, -4, 5),
										tuple(3, -4, -2, 0, 3, -1));

		//After 7 steps:
		day12.doSteps(1);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(2, -2, 1, 3, 5, -2),
										tuple(1, -4, -4, -2, -4, -4),
										tuple(3, -7, 5, 0, -5, 4),
										tuple(2, 0, 0, -1, 4, 2));

		//After 8 steps:
		day12.doSteps(1);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(5, 2, -2, 3, 4, -3),
										tuple(2, -7, -5, 1, -3, -1),
										tuple(0, -9, 6, -3, -2, 1),
										tuple(1, 1, 3, -1, 1, 3));

		//After 9 steps:
		day12.doSteps(1);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(5, 3, -4, 0, 1, -2),
										tuple(2, -9, -3, 0, -2, 2),
										tuple(0, -8, 4, 0, 1, -2),
										tuple(1, 1, 5, 0, 0, 2));

		//After 10 steps:
		int totalEnergy = day12.doSteps(1);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(2, 1, -3, -3, -2, 1),
										tuple(1, -8, 0, -1, 1, 3),
										tuple(3, -6, 1, 3, 2, -3),
										tuple(2, 0, 4, 1, -1, -1));

		assertThat(totalEnergy).isEqualTo(179);
	}

	@Test
	public void stepExample2()
	{
		String input = "<x=-8, y=-10, z=0>\n"
						+ "<x=5, y=5, z=10>\n"
						+ "<x=2, y=-7, z=3>\n"
						+ "<x=9, y=-8, z=-3>";

		List<Moon> moons;
		Day12 day12 = new Day12();
		day12.readInput(input);

		//		After 0 steps:
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(-8, -10, 0, 0, 0, 0),
										tuple(5, 5, 10, 0, 0, 0),
										tuple(2, -7, 3, 0, 0, 0),
										tuple(9, -8, -3, 0, 0, 0));

		//					After 10 steps:
		day12.doSteps(10);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(-9, -10, 1, -2, -2, -1),
										tuple(4, 10, 9, -3, 7, -2),
										tuple(8, -10, -3, 5, -1, -2),
										tuple(5, -10, 3, 0, -4, 5));

		//					After 20 steps:
		day12.doSteps(10);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(-10, 3, -4, -5, 2, 0),
										tuple(5, -25, 6, 1, 1, -4),
										tuple(13, 1, 1, 5, -2, 2),
										tuple(0, 1, 7, -1, -1, 2));

		//					After 30 steps:
		day12.doSteps(10);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(15, -6, -9, -5, 4, 0),
										tuple(-4, -11, 3, -3, -10, 0),
										tuple(0, -1, 11, 7, 4, 3),
										tuple(-3, -2, 5, 1, 2, -3));

		//					After 40 steps:
		day12.doSteps(10);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(14, -12, -4, 11, 3, 0),
										tuple(-1, 18, 8, -5, 2, 3),
										tuple(-5, -14, 8, 1, -2, 0),
										tuple(0, -12, -2, -7, -3, -3));

		//					After 50 steps:
		day12.doSteps(10);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(-23, 4, 1, -7, -1, 2),
										tuple(20, -31, 13, 5, 3, 4),
										tuple(-4, 6, 1, -1, 1, -3),
										tuple(15, 1, -5, 3, -3, -3));

		//					After 60 steps:
		day12.doSteps(10);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(36, -10, 6, 5, 0, 3),
										tuple(-18, 10, 9, -3, -7, 5),
										tuple(8, -12, -3, -2, 1, -7),
										tuple(-18, -8, -2, 0, 6, -1));

		//					After 70 steps:
		day12.doSteps(10);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(-33, -6, 5, -5, -4, 7),
										tuple(13, -9, 2, -2, 11, 3),
										tuple(11, -8, 2, 8, -6, -7),
										tuple(17, 3, 1, -1, -1, -3));

		//					After 80 steps:
		day12.doSteps(10);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(30, -8, 3, 3, 3, 0),
										tuple(-2, -4, 0, 4, -13, 2),
										tuple(-18, -7, 15, -8, 2, -2),
										tuple(-2, -1, -8, 1, 8, 0));

		//					After 90 steps:
		day12.doSteps(10);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(-25, -1, 4, 1, -3, 4),
										tuple(2, -9, 0, -3, 13, -1),
										tuple(32, -8, 14, 5, -4, 6),
										tuple(-1, -2, -8, -3, -6, -9));

		//					After 100 steps:
		int totalEnergy = day12.doSteps(10);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(8, -12, -9, -7, 3, 0),
										tuple(13, 16, -3, 3, -11, -5),
										tuple(-29, -11, -1, -3, 7, 4),
										tuple(16, -13, 23, 7, 1, 1));

		assertThat(totalEnergy).isEqualTo(1940);
	}

	@Test
	public void stepExample2NoStages()
	{
		String input = "<x=-8, y=-10, z=0>\n"
						+ "<x=5, y=5, z=10>\n"
						+ "<x=2, y=-7, z=3>\n"
						+ "<x=9, y=-8, z=-3>";

		List<Moon> moons;
		Day12 day12 = new Day12();
		day12.readInput(input);

		int totalEnergy = day12.doSteps(100);
		moons = day12.getMoons();
		assertThat(moons).extracting("x", "y", "z", "velocityX", "velocityY", "velocityZ")
						.contains(tuple(8, -12, -9, -7, 3, 0),
										tuple(13, 16, -3, 3, -11, -5),
										tuple(-29, -11, -1, -3, 7, 4),
										tuple(16, -13, 23, 7, 1, 1));

		assertThat(totalEnergy).isEqualTo(1940);
	}

	@Test
	public void testParse()
	{
		String input = "<x=-1, y=0, z=2>\n"
						+ "<x=2, y=-10, z=-7>\n"
						+ "<x=4, y=-8, z=8>\n"
						+ "<x=3, y=5, z=-1>";
		Day12 day12 = new Day12();
		List<Moon> moons = day12.parse(input);
		assertThat(moons).extracting("x", "y", "z")
						.contains(tuple(-1, 0, 2),
										tuple(2, -10, -7),
										tuple(4, -8, 8),
										tuple(3, 5, -1));
	}

	@Test
	public void part1() throws IOException
	{
		ClassPathResource input = new ClassPathResource("advent/day12input.txt");
		Day12 day12 = new Day12();
		day12.readInput(IOUtils.toString(input.getInputStream(), StandardCharsets.UTF_8));
		int totalEnergy = day12.doSteps(1000);
		assertThat(totalEnergy).isEqualTo(10198);
	}

	@Test
	public void part2Example1() throws IOException
	{
		String input = "<x=-1, y=0, z=2>\n"
						+ "<x=2, y=-10, z=-7>\n"
						+ "<x=4, y=-8, z=8>\n"
						+ "<x=3, y=5, z=-1>";
		Day12 day12 = new Day12();
		day12.readInput(input);
		long steps = day12.stepsToRepeat();
		assertThat(steps).isEqualTo(2772);
	}

	@Test
	public void part2Example2() throws IOException
	{
		String input = "<x=-8, y=-10, z=0>\n"
						+ "<x=5, y=5, z=10>\n"
						+ "<x=2, y=-7, z=3>\n"
						+ "<x=9, y=-8, z=-3>";
		Day12 day12 = new Day12();
		day12.readInput(input);
		long steps = day12.stepsToRepeat();
		assertThat(steps).isEqualTo(4686774924L);
	}

	@Test
	public void part2() throws IOException
	{
		ClassPathResource input = new ClassPathResource("advent/day12input.txt");
		Day12 day12 = new Day12();
		day12.readInput(IOUtils.toString(input.getInputStream(), StandardCharsets.UTF_8));
		long steps = day12.stepsToRepeat();
		assertThat(steps).isEqualTo(271442326847376L);
	}
}
