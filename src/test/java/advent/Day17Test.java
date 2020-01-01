package advent;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Day17Test
{
	private Logger logger = LoggerFactory.getLogger(Day17Test.class);

	@Test
	public void part1Example1()
	{
		Day17 day17 = new Day17();
		Day17.ScaffholdReader scaffholdReader = createMap(
						"..#..........\n"
										+ "..#..........\n"
										+ "#######...###\n"
										+ "#.#...#...#.#\n"
										+ "#############\n"
										+ "..#...#...#..\n"
										+ "..#####...#..");
		long sumOfAlignmentParams = day17.alignmentParams(scaffholdReader);
		assertThat(sumOfAlignmentParams).isEqualTo(76);
	}

	@Test
	public void part2Example1FindPath()
	{
		String map = "#######...#####\n"
						+ "#.....#...#...#\n"
						+ "#.....#...#...#\n" // end
						+ "......#...#...#\n"
						+ "......#...###.#\n"
						+ "......#.....#.#\n"
						+ "^########...#.#\n" // start
						+ "......#.#...#.#\n"
						+ "......#########\n"
						+ "........#...#..\n"
						+ "....#########..\n"
						+ "....#...#......\n"
						+ "....#...#......\n"
						+ "....#...#......\n"
						+ "....#####......";
		Day17.ScaffholdReader scaffholdReader = createMap(map);
		List<List<String>> allCommands = scaffholdReader.startToEndRoute();
		assertThat(allCommands).contains(
						List.of("R", "8", "R", "8", "R", "4", "R", "4", "R", "8", "L", "6", "L", "2", "R", "4", "R",
										"4", "R", "8", "R", "8", "R", "8", "L", "6", "L", "2"));
	}

	@Test
	public void testFactorize()
	{
		List<String> commands = List.of("R", "8", "R", "8", "R", "4", "R", "4", "R", "8", "L", "6", "L", "2", "R", "4",
						"R", "4", "R", "8", "R", "8", "R", "8", "L", "6", "L", "2");

		Day17 day17 = new Day17();
		Set<Day17.MovementProgram> programs = day17.factorize(commands, 0);
		assertThat(programs).extracting(Day17.MovementProgram::expand)
						.contains("R,8,R,8,R,4,R,4,R,8,L,6,L,2,R,4,R,4,R,8,R,8,R,8,L,6,L,2");

		Set<String> uniquePrograms = programs.stream().map(Day17.MovementProgram::expand).collect(Collectors.toSet());

		logger.debug("programs size {}, unique programs size:  {}", programs.size(), uniquePrograms.size());
	}

	@Test
	public void testFactorize2()
	{
		List<String> commands = List.of("R", "8", "L", "10", "L", "12", "R", "4", "R", "8", "L", "12", "R", "4", "R",
						"4", "R", "8", "L", "10", "L", "12", "R", "4", "R", "8", "L", "10", "R", "8", "R", "8", "L",
						"10", "L", "12", "R", "4", "R", "8", "L", "12", "R", "4", "R", "4", "R", "8", "L", "10", "R",
						"8", "R", "8", "L", "12", "R", "4", "R", "4", "R", "8", "L", "10", "R", "8", "R", "8", "L",
						"12", "R", "4", "R", "4");

		Day17 day17 = new Day17();
		Set<Day17.MovementProgram> programs = day17.factorize(commands, 0);
		logger.debug("programs {}", programs);
	}

	@Test
	public void part1() throws Exception
	{
		Day17 day17 = new Day17();
		int sumOfAlignmentParams = day17.part1(new ClassPathResource("advent/day17input.txt"));
		assertThat(sumOfAlignmentParams).isEqualTo(4044);
	}

	@Test
	public void part2PrintMap() throws Exception
	{
		Day17 day17 = new Day17();
		day17.part2PrintMap(new ClassPathResource("advent/day17input.txt"));
	}

	@Test
	public void part2FindProgram() throws Exception
	{
		Day17 day17 = new Day17();
		day17.part2FindPrograms(new ClassPathResource("advent/day17input.txt"));
	}

	@Test
	public void part2Solution() throws Exception
	{
		Day17 day17 = new Day17();
		Day17.MovementProgram program = new Day17.MovementProgram("A,B,A,C,A,B,C,B,C,B",
						List.of("R,8,L,10,L,12,R,4", "R,8,L,12,R,4,R,4", "R,8,L,10,R,8"));

		int dust = day17.part2Solution(new ClassPathResource("advent/day17input.txt"), program);
		assertThat(dust).isEqualTo(893283);
	}

	private Day17.ScaffholdReader createMap(String map)
	{
		Day17.ScaffholdReader scaffholdReader = new Day17.ScaffholdReader();
		map.chars().forEach(v -> scaffholdReader.onMessage(String.valueOf(v)));
		scaffholdReader.finished();
		return scaffholdReader;
	}
}
