package advent;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.awt.Point;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class Day20Test
{
	@Nested
	class StepsTests
	{
		@Test
		public void part1Example1() throws Exception
		{
			String input = "         A           \n"
							+ "         A           \n"
							+ "  #######.#########  \n"
							+ "  #######.........#  \n"
							+ "  #######.#######.#  \n"
							+ "  #######.#######.#  \n"
							+ "  #######.#######.#  \n"
							+ "  #####  B    ###.#  \n"
							+ "BC...##  C    ###.#  \n"
							+ "  ##.##       ###.#  \n"
							+ "  ##...DE  F  ###.#  \n"
							+ "  #####    G  ###.#  \n"
							+ "  #########.#####.#  \n"
							+ "DE..#######...###.#  \n"
							+ "  #.#########.###.#  \n"
							+ "FG..#########.....#  \n"
							+ "  ###########.#####  \n"
							+ "             Z       \n"
							+ "             Z       ";
			Day20 day20 = new Day20();
			int steps = day20.part1(stringResource(input));
			assertThat(steps).isEqualTo(23);
		}

		@Test
		public void part1Example2() throws Exception
		{
			String input = "                   A               \n"
							+ "                   A               \n"
							+ "  #################.#############  \n"
							+ "  #.#...#...................#.#.#  \n"
							+ "  #.#.#.###.###.###.#########.#.#  \n"
							+ "  #.#.#.......#...#.....#.#.#...#  \n"
							+ "  #.#########.###.#####.#.#.###.#  \n"
							+ "  #.............#.#.....#.......#  \n"
							+ "  ###.###########.###.#####.#.#.#  \n"
							+ "  #.....#        A   C    #.#.#.#  \n"
							+ "  #######        S   P    #####.#  \n"
							+ "  #.#...#                 #......VT\n"
							+ "  #.#.#.#                 #.#####  \n"
							+ "  #...#.#               YN....#.#  \n"
							+ "  #.###.#                 #####.#  \n"
							+ "DI....#.#                 #.....#  \n"
							+ "  #####.#                 #.###.#  \n"
							+ "ZZ......#               QG....#..AS\n"
							+ "  ###.###                 #######  \n"
							+ "JO..#.#.#                 #.....#  \n"
							+ "  #.#.#.#                 ###.#.#  \n"
							+ "  #...#..DI             BU....#..LF\n"
							+ "  #####.#                 #.#####  \n"
							+ "YN......#               VT..#....QG\n"
							+ "  #.###.#                 #.###.#  \n"
							+ "  #.#...#                 #.....#  \n"
							+ "  ###.###    J L     J    #.#.###  \n"
							+ "  #.....#    O F     P    #.#...#  \n"
							+ "  #.###.#####.#.#####.#####.###.#  \n"
							+ "  #...#.#.#...#.....#.....#.#...#  \n"
							+ "  #.#####.###.###.#.#.#########.#  \n"
							+ "  #...#.#.....#...#.#.#.#.....#.#  \n"
							+ "  #.###.#####.###.###.#.#.#######  \n"
							+ "  #.#.........#...#.............#  \n"
							+ "  #########.###.###.#############  \n"
							+ "           B   J   C               \n"
							+ "           U   P   P               ";
			Day20 day20 = new Day20();
			int steps = day20.part1(stringResource(input));
			assertThat(steps).isEqualTo(58);
		}

		@Test
		public void part1() throws Exception
		{
			Day20 day20 = new Day20();
			int steps = day20.part1(new ClassPathResource("advent/day20input.txt"));
			assertThat(steps).isEqualTo(604);
		}
	}

	@Nested
	class ParseMazeTests
	{
		@Test
		public void part1MyExample1() throws Exception
		{
			Day20.Maze maze = Day20.Maze.create(stringResource(
							"#########\n"
											+ "#.......#\n"
											+ "#########"));

			assertThat(maze.getPoints()).containsExactly(
							new Point(1, 1),
							new Point(2, 1),
							new Point(3, 1),
							new Point(4, 1),
							new Point(5, 1),
							new Point(6, 1),
							new Point(7, 1));

			assertThat(maze.getConnections().size()).isEqualTo(6);
			assertThat(maze.isConnected(new Point(1, 1), new Point(2, 1))).isTrue();
			assertThat(maze.isConnected(new Point(2, 1), new Point(3, 1))).isTrue();
			assertThat(maze.isConnected(new Point(3, 1), new Point(4, 1))).isTrue();
			assertThat(maze.isConnected(new Point(4, 1), new Point(5, 1))).isTrue();
			assertThat(maze.isConnected(new Point(5, 1), new Point(6, 1))).isTrue();
			assertThat(maze.isConnected(new Point(6, 1), new Point(7, 1))).isTrue();
		}

		@Test
		public void part1MyExample2() throws Exception
		{
			Day20.Maze maze = Day20.Maze.create(stringResource(
							"########################\n"
											+ "#......................#\n"
											+ "######################.#\n"
											+ "#.......................AB\n"
											+ "#.######################\n"
											+ " A     \n"
											+ " B  "));

			assertThat(maze.isConnected(new Point(1, 4), new Point(23, 3))).isTrue();
		}

		@Test
		public void part1Example1() throws Exception
		{
			String input = "         A           \n"
							+ "         A           \n"
							+ "  #######.#########  \n"
							+ "  #######.........#  \n"
							+ "  #######.#######.#  \n"
							+ "  #######.#######.#  \n"
							+ "  #######.#######.#  \n"
							+ "  #####  B    ###.#  \n"
							+ "BC...##  C    ###.#  \n"
							+ "  ##.##       ###.#  \n"
							+ "  ##...DE  F  ###.#  \n"
							+ "  #####    G  ###.#  \n"
							+ "  #########.#####.#  \n"
							+ "DE..#######...###.#  \n"
							+ "  #.#########.###.#  \n"
							+ "FG..#########.....#  \n"
							+ "  ###########.#####  \n"
							+ "             Z       \n"
							+ "             Z       ";
			Day20.Maze maze = Day20.Maze.create(stringResource(input));
			assertThat(maze.isConnected(new Point(2, 8), new Point(9, 6))).isTrue();
			assertThat(maze.isConnected(new Point(2, 13), new Point(6, 10))).isTrue();
			assertThat(maze.isConnected(new Point(2, 15), new Point(11, 12))).isTrue();
			assertThat(maze.getEntrance()).isEqualTo(new Point(9, 2));
			assertThat(maze.getExit()).isEqualTo(new Point(13, 16));
		}
	}

	private Resource stringResource(String code)
	{
		return new InputStreamResource(IOUtils.toInputStream(code, StandardCharsets.UTF_8));
	}
}
