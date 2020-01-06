package advent;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.awt.Point;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class Day18Test
{
	@Nested
	class MoveTests
	{
		@Test
		public void part1Example1() throws Exception
		{
			// 012345678
			// #b.A.@.a#
			Day18 day18 = new Day18();
			day18.part1(stringResource("#########\n"
							+ "#b.A.@.a#\n"
							+ "#########"));
		}

		@Test
		public void part1Example2() throws Exception
		{
			Day18 day18 = new Day18();
			day18.part1(stringResource(
							"########################\n"
											+ "#f.D.E.e.C.b.A.@.a.B.c.#\n"
											+ "######################.#\n"
											+ "#d.....................#\n"
											+ "########################"));

		}

		@Test
		public void part1Example3() throws Exception
		{
			Day18 day18 = new Day18();
			day18.part1(stringResource(
							"########################\n"
											+ "#...............b.C.D.f#\n"
											+ "#.######################\n"
											+ "#.....@.a.B.c.d.A.e.F.g#\n"
											+ "########################"));
		}

		@Test
		public void part1Example4() throws Exception
		{
			Day18 day18 = new Day18();
			day18.part1(stringResource(
							"#################\n"
											+ "#i.G..c...e..H.p#\n"
											+ "########.########\n"
											+ "#j.A..b...f..D.o#\n"
											+ "########@########\n"
											+ "#k.E..a...g..B.n#\n"
											+ "########.########\n"
											+ "#l.F..d...h..C.m#\n"
											+ "#################"));
		}

		@Test
		public void part1Example5() throws Exception
		{
			Day18 day18 = new Day18();
			day18.part1(stringResource("########################\n"
							+ "#@..............ac.GI.b#\n"
							+ "###d#e#f################\n"
							+ "###A#B#C################\n"
							+ "###g#h#i################\n"
							+ "########################"));
		}

	}

	@Nested
	class DirectlyAccessibleKeysTests
	{
		@Test
		public void part1Example1() throws Exception
		{
			// 012345678
			// #b.A.@.a#
			Day18.Maze maze = Day18.Maze.create(stringResource(
							"#########\n"
											+ "#b.A.@.a#\n"
											+ "#########"));
			Day18.MazePoint start = maze.getStartPoint();
			Set<Character> directlyAccessibleKeys = maze.directlyAccessibleKeys(start, new Day18.RouteInfo());
			assertThat(directlyAccessibleKeys).containsExactly('a');
		}

		@Test
		public void part1Example1OnKey() throws Exception
		{
			// 012345678
			// #b.A.@.a#
			Day18.Maze maze = Day18.Maze.create(stringResource(
							"#########\n"
											+ "#b.A.@.a#\n"
											+ "#########"));
			Day18.MazePoint aKey = maze.findMazePointAt(7, 1);
			Day18.RouteInfo routeInfo = new Day18.RouteInfo();
			routeInfo.gotKeys.add('a');
			Set<Character> directlyAccessibleKeys = maze.directlyAccessibleKeys(aKey, routeInfo);
			assertThat(directlyAccessibleKeys).containsExactly('b');
		}

		@Test
		public void part1Example2() throws Exception
		{
			Day18.Maze maze = Day18.Maze.create(stringResource(
							"########################\n"
											+ "#f.D.E.e.C.b.A.@.a.B.c.#\n"
											+ "######################.#\n"
											+ "#d.....................#\n"
											+ "########################"));
			Day18.MazePoint start = maze.getStartPoint();
			Set<Character> directlyAccessibleKeys = maze.directlyAccessibleKeys(start, new Day18.RouteInfo());
			assertThat(directlyAccessibleKeys).containsExactly('a');
		}

		@Test
		public void part1Example3() throws Exception
		{
			Day18.Maze maze = Day18.Maze.create(stringResource(
							"########################\n"
											+ "#...............b.C.D.f#\n"
											+ "#.######################\n"
											+ "#.....@.a.B.c.d.A.e.F.g#\n"
											+ "########################"));
			Day18.MazePoint start = maze.getStartPoint();
			Set<Character> directlyAccessibleKeys = maze.directlyAccessibleKeys(start, new Day18.RouteInfo());
			assertThat(directlyAccessibleKeys).containsOnly('a', 'b');
		}

		@Test
		public void part1Example4() throws Exception
		{
			Day18.Maze maze = Day18.Maze.create(stringResource(
							"#################\n"
											+ "#i.G..c...e..H.p#\n"
											+ "########.########\n"
											+ "#j.A..b...f..D.o#\n"
											+ "########@########\n"
											+ "#k.E..a...g..B.n#\n"
											+ "########.########\n"
											+ "#l.F..d...h..C.m#\n"
											+ "#################"));
			Day18.MazePoint start = maze.getStartPoint();
			Set<Character> directlyAccessibleKeys = maze.directlyAccessibleKeys(start, new Day18.RouteInfo());
			assertThat(directlyAccessibleKeys).containsExactlyInAnyOrder('c', 'e', 'b', 'f', 'a', 'g', 'd', 'h');
		}

		@Test
		public void part1Example5() throws Exception
		{
			String input = "########################\n"
							+ "#@..............ac.GI.b#\n"
							+ "###d#e#f################\n"
							+ "###A#B#C################\n"
							+ "###g#h#i################\n"
							+ "########################";
			Day18.Maze maze = Day18.Maze.create(stringResource(input));
			Day18.MazePoint start = maze.getStartPoint();
			Set<Character> directlyAccessibleKeys = maze.directlyAccessibleKeys(start, new Day18.RouteInfo());
			assertThat(directlyAccessibleKeys).containsExactlyInAnyOrder('a', 'd', 'e', 'f');
		}
	}

	@Nested
	class ParseMazeTests
	{
		@Test
		public void part1Example1() throws Exception
		{
			Day18.Maze maze = Day18.Maze.create(stringResource(
							"#########\n"
											+ "#b.A.@.a#\n"
											+ "#########"));

			assertThat(maze.getPoints()).extracting("xPos", "yPos", "contains").containsExactly(
							tuple(1, 1, 'b'),
							tuple(2, 1, '.'),
							tuple(3, 1, 'A'),
							tuple(4, 1, '.'),
							tuple(5, 1, '@'),
							tuple(6, 1, '.'),
							tuple(7, 1, 'a'));

			assertThat(maze.getConnections().size()).isEqualTo(6);
			assertThat(maze.isConnected(new Point(1, 1), new Point(2, 1))).isTrue();
			assertThat(maze.isConnected(new Point(2, 1), new Point(3, 1))).isTrue();
			assertThat(maze.isConnected(new Point(3, 1), new Point(4, 1))).isTrue();
			assertThat(maze.isConnected(new Point(4, 1), new Point(5, 1))).isTrue();
			assertThat(maze.isConnected(new Point(5, 1), new Point(6, 1))).isTrue();
			assertThat(maze.isConnected(new Point(6, 1), new Point(7, 1))).isTrue();
		}

		@Test
		public void part1Example2() throws Exception
		{
			Day18.Maze maze = Day18.Maze.create(stringResource(
							"########################\n"
											+ "#f.D.E.e.C.b.A.@.a.B.c.#\n"
											+ "######################.#\n"
											+ "#d.....................#\n"
											+ "########################"));

			assertThat(maze.getPoints()).extracting("xPos", "yPos", "contains").containsExactly(
							tuple(1, 1, 'f'), tuple(2, 1, '.'), tuple(3, 1, 'D'), tuple(4, 1, '.'),
							tuple(5, 1, 'E'), tuple(6, 1, '.'), tuple(7, 1, 'e'), tuple(8, 1, '.'),
							tuple(9, 1, 'C'), tuple(10, 1, '.'), tuple(11, 1, 'b'), tuple(12, 1, '.'),
							tuple(13, 1, 'A'), tuple(14, 1, '.'), tuple(15, 1, '@'), tuple(16, 1, '.'),
							tuple(17, 1, 'a'), tuple(18, 1, '.'), tuple(19, 1, 'B'), tuple(20, 1, '.'),
							tuple(21, 1, 'c'), tuple(22, 1, '.'),
							tuple(22, 2, '.'),
							tuple(1, 3, 'd'), tuple(2, 3, '.'), tuple(3, 3, '.'), tuple(4, 3, '.'),
							tuple(5, 3, '.'), tuple(6, 3, '.'), tuple(7, 3, '.'), tuple(8, 3, '.'),
							tuple(9, 3, '.'), tuple(10, 3, '.'), tuple(11, 3, '.'), tuple(12, 3, '.'),
							tuple(13, 3, '.'), tuple(14, 3, '.'), tuple(15, 3, '.'), tuple(16, 3, '.'),
							tuple(17, 3, '.'), tuple(18, 3, '.'), tuple(19, 3, '.'), tuple(20, 3, '.'),
							tuple(21, 3, '.'), tuple(22, 3, '.'));

			assertThat(maze.getConnections().size()).isEqualTo(44);
			assertThat(maze.isConnected(new Point(1, 1), new Point(2, 1))).isTrue();
			assertThat(maze.isConnected(new Point(2, 1), new Point(3, 1))).isTrue();
			assertThat(maze.isConnected(new Point(3, 1), new Point(4, 1))).isTrue();
			assertThat(maze.isConnected(new Point(4, 1), new Point(5, 1))).isTrue();
			assertThat(maze.isConnected(new Point(5, 1), new Point(6, 1))).isTrue();
			assertThat(maze.isConnected(new Point(6, 1), new Point(7, 1))).isTrue();
			assertThat(maze.isConnected(new Point(7, 1), new Point(8, 1))).isTrue();
			assertThat(maze.isConnected(new Point(8, 1), new Point(9, 1))).isTrue();
			assertThat(maze.isConnected(new Point(9, 1), new Point(10, 1))).isTrue();
			assertThat(maze.isConnected(new Point(10, 1), new Point(11, 1))).isTrue();
			assertThat(maze.isConnected(new Point(11, 1), new Point(12, 1))).isTrue();
			assertThat(maze.isConnected(new Point(12, 1), new Point(13, 1))).isTrue();
			assertThat(maze.isConnected(new Point(13, 1), new Point(14, 1))).isTrue();
			assertThat(maze.isConnected(new Point(14, 1), new Point(15, 1))).isTrue();
			assertThat(maze.isConnected(new Point(15, 1), new Point(16, 1))).isTrue();
			assertThat(maze.isConnected(new Point(16, 1), new Point(17, 1))).isTrue();
			assertThat(maze.isConnected(new Point(17, 1), new Point(18, 1))).isTrue();
			assertThat(maze.isConnected(new Point(18, 1), new Point(19, 1))).isTrue();
			assertThat(maze.isConnected(new Point(19, 1), new Point(20, 1))).isTrue();
			assertThat(maze.isConnected(new Point(20, 1), new Point(21, 1))).isTrue();
			assertThat(maze.isConnected(new Point(21, 1), new Point(22, 1))).isTrue();
			assertThat(maze.isConnected(new Point(22, 1), new Point(22, 2))).isTrue();
			assertThat(maze.isConnected(new Point(22, 2), new Point(22, 3))).isTrue();
			assertThat(maze.isConnected(new Point(1, 3), new Point(2, 3))).isTrue();
			assertThat(maze.isConnected(new Point(2, 3), new Point(3, 3))).isTrue();
			assertThat(maze.isConnected(new Point(3, 3), new Point(4, 3))).isTrue();
			assertThat(maze.isConnected(new Point(4, 3), new Point(5, 3))).isTrue();
			assertThat(maze.isConnected(new Point(5, 3), new Point(6, 3))).isTrue();
			assertThat(maze.isConnected(new Point(6, 3), new Point(7, 3))).isTrue();
			assertThat(maze.isConnected(new Point(7, 3), new Point(8, 3))).isTrue();
			assertThat(maze.isConnected(new Point(8, 3), new Point(9, 3))).isTrue();
			assertThat(maze.isConnected(new Point(9, 3), new Point(10, 3))).isTrue();
			assertThat(maze.isConnected(new Point(10, 3), new Point(11, 3))).isTrue();
			assertThat(maze.isConnected(new Point(11, 3), new Point(12, 3))).isTrue();
			assertThat(maze.isConnected(new Point(12, 3), new Point(13, 3))).isTrue();
			assertThat(maze.isConnected(new Point(13, 3), new Point(14, 3))).isTrue();
			assertThat(maze.isConnected(new Point(14, 3), new Point(15, 3))).isTrue();
			assertThat(maze.isConnected(new Point(15, 3), new Point(16, 3))).isTrue();
			assertThat(maze.isConnected(new Point(16, 3), new Point(17, 3))).isTrue();
			assertThat(maze.isConnected(new Point(17, 3), new Point(18, 3))).isTrue();
			assertThat(maze.isConnected(new Point(18, 3), new Point(19, 3))).isTrue();
			assertThat(maze.isConnected(new Point(19, 3), new Point(20, 3))).isTrue();
			assertThat(maze.isConnected(new Point(20, 3), new Point(21, 3))).isTrue();
			assertThat(maze.isConnected(new Point(21, 3), new Point(22, 3))).isTrue();
		}

		@Test
		public void part1Example3() throws Exception
		{
			Day18.Maze maze = Day18.Maze.create(stringResource(
							"########################\n"
											+ "#...............b.C.D.f#\n"
											+ "#.######################\n"
											+ "#.....@.a.B.c.d.A.e.F.g#\n"
											+ "########################"));

			assertThat(maze.getPoints()).extracting("xPos", "yPos", "contains").containsExactly(
							tuple(1, 1, '.'), tuple(2, 1, '.'), tuple(3, 1, '.'), tuple(4, 1, '.'),
							tuple(5, 1, '.'), tuple(6, 1, '.'), tuple(7, 1, '.'), tuple(8, 1, '.'),
							tuple(9, 1, '.'), tuple(10, 1, '.'), tuple(11, 1, '.'), tuple(12, 1, '.'),
							tuple(13, 1, '.'), tuple(14, 1, '.'), tuple(15, 1, '.'), tuple(16, 1, 'b'),
							tuple(17, 1, '.'), tuple(18, 1, 'C'), tuple(19, 1, '.'), tuple(20, 1, 'D'),
							tuple(21, 1, '.'), tuple(22, 1, 'f'),
							tuple(1, 2, '.'),
							tuple(1, 3, '.'), tuple(2, 3, '.'), tuple(3, 3, '.'), tuple(4, 3, '.'),
							tuple(5, 3, '.'), tuple(6, 3, '@'), tuple(7, 3, '.'), tuple(8, 3, 'a'),
							tuple(9, 3, '.'), tuple(10, 3, 'B'), tuple(11, 3, '.'), tuple(12, 3, 'c'),
							tuple(13, 3, '.'), tuple(14, 3, 'd'), tuple(15, 3, '.'), tuple(16, 3, 'A'),
							tuple(17, 3, '.'), tuple(18, 3, 'e'), tuple(19, 3, '.'), tuple(20, 3, 'F'),
							tuple(21, 3, '.'), tuple(22, 3, 'g'));

			assertThat(maze.getConnections().size()).isEqualTo(44);
			assertThat(maze.isConnected(new Point(1, 1), new Point(2, 1))).isTrue();
			assertThat(maze.isConnected(new Point(2, 1), new Point(3, 1))).isTrue();
			assertThat(maze.isConnected(new Point(3, 1), new Point(4, 1))).isTrue();
			assertThat(maze.isConnected(new Point(4, 1), new Point(5, 1))).isTrue();
			assertThat(maze.isConnected(new Point(5, 1), new Point(6, 1))).isTrue();
			assertThat(maze.isConnected(new Point(6, 1), new Point(7, 1))).isTrue();
			assertThat(maze.isConnected(new Point(7, 1), new Point(8, 1))).isTrue();
			assertThat(maze.isConnected(new Point(8, 1), new Point(9, 1))).isTrue();
			assertThat(maze.isConnected(new Point(9, 1), new Point(10, 1))).isTrue();
			assertThat(maze.isConnected(new Point(10, 1), new Point(11, 1))).isTrue();
			assertThat(maze.isConnected(new Point(11, 1), new Point(12, 1))).isTrue();
			assertThat(maze.isConnected(new Point(12, 1), new Point(13, 1))).isTrue();
			assertThat(maze.isConnected(new Point(13, 1), new Point(14, 1))).isTrue();
			assertThat(maze.isConnected(new Point(14, 1), new Point(15, 1))).isTrue();
			assertThat(maze.isConnected(new Point(15, 1), new Point(16, 1))).isTrue();
			assertThat(maze.isConnected(new Point(16, 1), new Point(17, 1))).isTrue();
			assertThat(maze.isConnected(new Point(17, 1), new Point(18, 1))).isTrue();
			assertThat(maze.isConnected(new Point(18, 1), new Point(19, 1))).isTrue();
			assertThat(maze.isConnected(new Point(19, 1), new Point(20, 1))).isTrue();
			assertThat(maze.isConnected(new Point(20, 1), new Point(21, 1))).isTrue();
			assertThat(maze.isConnected(new Point(21, 1), new Point(22, 1))).isTrue();
			assertThat(maze.isConnected(new Point(1, 1), new Point(1, 2))).isTrue();
			assertThat(maze.isConnected(new Point(1, 2), new Point(1, 3))).isTrue();
			assertThat(maze.isConnected(new Point(1, 3), new Point(2, 3))).isTrue();
			assertThat(maze.isConnected(new Point(2, 3), new Point(3, 3))).isTrue();
			assertThat(maze.isConnected(new Point(3, 3), new Point(4, 3))).isTrue();
			assertThat(maze.isConnected(new Point(4, 3), new Point(5, 3))).isTrue();
			assertThat(maze.isConnected(new Point(5, 3), new Point(6, 3))).isTrue();
			assertThat(maze.isConnected(new Point(6, 3), new Point(7, 3))).isTrue();
			assertThat(maze.isConnected(new Point(7, 3), new Point(8, 3))).isTrue();
			assertThat(maze.isConnected(new Point(8, 3), new Point(9, 3))).isTrue();
			assertThat(maze.isConnected(new Point(9, 3), new Point(10, 3))).isTrue();
			assertThat(maze.isConnected(new Point(10, 3), new Point(11, 3))).isTrue();
			assertThat(maze.isConnected(new Point(11, 3), new Point(12, 3))).isTrue();
			assertThat(maze.isConnected(new Point(12, 3), new Point(13, 3))).isTrue();
			assertThat(maze.isConnected(new Point(13, 3), new Point(14, 3))).isTrue();
			assertThat(maze.isConnected(new Point(14, 3), new Point(15, 3))).isTrue();
			assertThat(maze.isConnected(new Point(15, 3), new Point(16, 3))).isTrue();
			assertThat(maze.isConnected(new Point(16, 3), new Point(17, 3))).isTrue();
			assertThat(maze.isConnected(new Point(17, 3), new Point(18, 3))).isTrue();
			assertThat(maze.isConnected(new Point(18, 3), new Point(19, 3))).isTrue();
			assertThat(maze.isConnected(new Point(19, 3), new Point(20, 3))).isTrue();
			assertThat(maze.isConnected(new Point(20, 3), new Point(21, 3))).isTrue();
			assertThat(maze.isConnected(new Point(21, 3), new Point(22, 3))).isTrue();
		}

		@Test
		public void part1Example4() throws Exception
		{
			Day18.Maze maze = Day18.Maze.create(stringResource(
							"#################\n"
											+ "#i.G..c...e..H.p#\n"
											+ "########.########\n"
											+ "#j.A..b...f..D.o#\n"
											+ "########@########\n"
											+ "#k.E..a...g..B.n#\n"
											+ "########.########\n"
											+ "#l.F..d...h..C.m#\n"
											+ "#################"));

			assertThat(maze.getPoints()).extracting("xPos", "yPos", "contains").containsExactly(
							tuple(1, 1, 'i'), tuple(2, 1, '.'), tuple(3, 1, 'G'), tuple(4, 1, '.'),
							tuple(5, 1, '.'), tuple(6, 1, 'c'), tuple(7, 1, '.'), tuple(8, 1, '.'),
							tuple(9, 1, '.'), tuple(10, 1, 'e'), tuple(11, 1, '.'), tuple(12, 1, '.'),
							tuple(13, 1, 'H'), tuple(14, 1, '.'), tuple(15, 1, 'p'),
							tuple(8, 2, '.'),
							tuple(1, 3, 'j'), tuple(2, 3, '.'), tuple(3, 3, 'A'), tuple(4, 3, '.'),
							tuple(5, 3, '.'), tuple(6, 3, 'b'), tuple(7, 3, '.'), tuple(8, 3, '.'),
							tuple(9, 3, '.'), tuple(10, 3, 'f'), tuple(11, 3, '.'), tuple(12, 3, '.'),
							tuple(13, 3, 'D'), tuple(14, 3, '.'), tuple(15, 3, 'o'),
							tuple(8, 4, '@'),
							tuple(1, 5, 'k'), tuple(2, 5, '.'), tuple(3, 5, 'E'), tuple(4, 5, '.'),
							tuple(5, 5, '.'), tuple(6, 5, 'a'), tuple(7, 5, '.'), tuple(8, 5, '.'),
							tuple(9, 5, '.'), tuple(10, 5, 'g'), tuple(11, 5, '.'), tuple(12, 5, '.'),
							tuple(13, 5, 'B'), tuple(14, 5, '.'), tuple(15, 5, 'n'),
							tuple(8, 6, '.'),
							tuple(1, 7, 'l'), tuple(2, 7, '.'), tuple(3, 7, 'F'), tuple(4, 7, '.'),
							tuple(5, 7, '.'), tuple(6, 7, 'd'), tuple(7, 7, '.'), tuple(8, 7, '.'),
							tuple(9, 7, '.'), tuple(10, 7, 'h'), tuple(11, 7, '.'), tuple(12, 7, '.'),
							tuple(13, 7, 'C'), tuple(14, 7, '.'), tuple(15, 7, 'm'));

			assertThat(maze.getConnections().size()).isEqualTo(56 + 6);

			assertThat(maze.isConnected(new Point(8, 1), new Point(8, 2))).isTrue();
			assertThat(maze.isConnected(new Point(8, 2), new Point(8, 3))).isTrue();
			assertThat(maze.isConnected(new Point(8, 3), new Point(8, 4))).isTrue();
			assertThat(maze.isConnected(new Point(8, 4), new Point(8, 5))).isTrue();
			assertThat(maze.isConnected(new Point(8, 5), new Point(8, 6))).isTrue();
			assertThat(maze.isConnected(new Point(8, 6), new Point(8, 7))).isTrue();

			assertThat(maze.isConnected(new Point(1, 1), new Point(2, 1))).isTrue();
			assertThat(maze.isConnected(new Point(2, 1), new Point(3, 1))).isTrue();
			assertThat(maze.isConnected(new Point(3, 1), new Point(4, 1))).isTrue();
			assertThat(maze.isConnected(new Point(4, 1), new Point(5, 1))).isTrue();
			assertThat(maze.isConnected(new Point(5, 1), new Point(6, 1))).isTrue();
			assertThat(maze.isConnected(new Point(6, 1), new Point(7, 1))).isTrue();
			assertThat(maze.isConnected(new Point(7, 1), new Point(8, 1))).isTrue();
			assertThat(maze.isConnected(new Point(8, 1), new Point(9, 1))).isTrue();
			assertThat(maze.isConnected(new Point(9, 1), new Point(10, 1))).isTrue();
			assertThat(maze.isConnected(new Point(10, 1), new Point(11, 1))).isTrue();
			assertThat(maze.isConnected(new Point(11, 1), new Point(12, 1))).isTrue();
			assertThat(maze.isConnected(new Point(12, 1), new Point(13, 1))).isTrue();
			assertThat(maze.isConnected(new Point(13, 1), new Point(14, 1))).isTrue();
			assertThat(maze.isConnected(new Point(14, 1), new Point(15, 1))).isTrue();

			assertThat(maze.isConnected(new Point(1, 3), new Point(2, 3))).isTrue();
			assertThat(maze.isConnected(new Point(2, 3), new Point(3, 3))).isTrue();
			assertThat(maze.isConnected(new Point(3, 3), new Point(4, 3))).isTrue();
			assertThat(maze.isConnected(new Point(4, 3), new Point(5, 3))).isTrue();
			assertThat(maze.isConnected(new Point(5, 3), new Point(6, 3))).isTrue();
			assertThat(maze.isConnected(new Point(6, 3), new Point(7, 3))).isTrue();
			assertThat(maze.isConnected(new Point(7, 3), new Point(8, 3))).isTrue();
			assertThat(maze.isConnected(new Point(8, 3), new Point(9, 3))).isTrue();
			assertThat(maze.isConnected(new Point(9, 3), new Point(10, 3))).isTrue();
			assertThat(maze.isConnected(new Point(10, 3), new Point(11, 3))).isTrue();
			assertThat(maze.isConnected(new Point(11, 3), new Point(12, 3))).isTrue();
			assertThat(maze.isConnected(new Point(12, 3), new Point(13, 3))).isTrue();
			assertThat(maze.isConnected(new Point(13, 3), new Point(14, 3))).isTrue();
			assertThat(maze.isConnected(new Point(14, 3), new Point(15, 3))).isTrue();

			assertThat(maze.isConnected(new Point(1, 5), new Point(2, 5))).isTrue();
			assertThat(maze.isConnected(new Point(2, 5), new Point(3, 5))).isTrue();
			assertThat(maze.isConnected(new Point(3, 5), new Point(4, 5))).isTrue();
			assertThat(maze.isConnected(new Point(4, 5), new Point(5, 5))).isTrue();
			assertThat(maze.isConnected(new Point(5, 5), new Point(6, 5))).isTrue();
			assertThat(maze.isConnected(new Point(6, 5), new Point(7, 5))).isTrue();
			assertThat(maze.isConnected(new Point(7, 5), new Point(8, 5))).isTrue();
			assertThat(maze.isConnected(new Point(8, 5), new Point(9, 5))).isTrue();
			assertThat(maze.isConnected(new Point(9, 5), new Point(10, 5))).isTrue();
			assertThat(maze.isConnected(new Point(10, 5), new Point(11, 5))).isTrue();
			assertThat(maze.isConnected(new Point(11, 5), new Point(12, 5))).isTrue();
			assertThat(maze.isConnected(new Point(12, 5), new Point(13, 5))).isTrue();
			assertThat(maze.isConnected(new Point(13, 5), new Point(14, 5))).isTrue();
			assertThat(maze.isConnected(new Point(14, 5), new Point(15, 5))).isTrue();

			assertThat(maze.isConnected(new Point(1, 7), new Point(2, 7))).isTrue();
			assertThat(maze.isConnected(new Point(2, 7), new Point(3, 7))).isTrue();
			assertThat(maze.isConnected(new Point(3, 7), new Point(4, 7))).isTrue();
			assertThat(maze.isConnected(new Point(4, 7), new Point(5, 7))).isTrue();
			assertThat(maze.isConnected(new Point(5, 7), new Point(6, 7))).isTrue();
			assertThat(maze.isConnected(new Point(6, 7), new Point(7, 7))).isTrue();
			assertThat(maze.isConnected(new Point(7, 7), new Point(8, 7))).isTrue();
			assertThat(maze.isConnected(new Point(8, 7), new Point(9, 7))).isTrue();
			assertThat(maze.isConnected(new Point(9, 7), new Point(10, 7))).isTrue();
			assertThat(maze.isConnected(new Point(10, 7), new Point(11, 7))).isTrue();
			assertThat(maze.isConnected(new Point(11, 7), new Point(12, 7))).isTrue();
			assertThat(maze.isConnected(new Point(12, 7), new Point(13, 7))).isTrue();
			assertThat(maze.isConnected(new Point(13, 7), new Point(14, 7))).isTrue();
			assertThat(maze.isConnected(new Point(14, 7), new Point(15, 7))).isTrue();
		}

		@Test
		public void part1Example5() throws Exception
		{
			String input = "########################\n"
							+ "#@..............ac.GI.b#\n"
							+ "###d#e#f################\n"
							+ "###A#B#C################\n"
							+ "###g#h#i################\n"
							+ "########################";
			Day18.Maze maze = Day18.Maze.create(stringResource(input));
			assertThat(maze.getPoints()).extracting("xPos", "yPos", "contains").containsExactly(
							tuple(1, 1, '@'), tuple(2, 1, '.'), tuple(3, 1, '.'), tuple(4, 1, '.'),
							tuple(5, 1, '.'), tuple(6, 1, '.'), tuple(7, 1, '.'), tuple(8, 1, '.'),
							tuple(9, 1, '.'), tuple(10, 1, '.'), tuple(11, 1, '.'), tuple(12, 1, '.'),
							tuple(13, 1, '.'), tuple(14, 1, '.'), tuple(15, 1, '.'), tuple(16, 1, 'a'),
							tuple(17, 1, 'c'), tuple(18, 1, '.'), tuple(19, 1, 'G'), tuple(20, 1, 'I'),
							tuple(21, 1, '.'), tuple(22, 1, 'b'),
							tuple(3, 2, 'd'), tuple(5, 2, 'e'), tuple(7, 2, 'f'),
							tuple(3, 3, 'A'), tuple(5, 3, 'B'), tuple(7, 3, 'C'),
							tuple(3, 4, 'g'), tuple(5, 4, 'h'), tuple(7, 4, 'i'));

			assertThat(maze.getConnections().size()).isEqualTo(30);

			assertThat(maze.isConnected(new Point(3, 1), new Point(3, 2))).isTrue();
			assertThat(maze.isConnected(new Point(3, 2), new Point(3, 3))).isTrue();
			assertThat(maze.isConnected(new Point(3, 3), new Point(3, 4))).isTrue();
			assertThat(maze.isConnected(new Point(5, 1), new Point(5, 2))).isTrue();
			assertThat(maze.isConnected(new Point(5, 2), new Point(5, 3))).isTrue();
			assertThat(maze.isConnected(new Point(5, 3), new Point(5, 4))).isTrue();
			assertThat(maze.isConnected(new Point(7, 1), new Point(7, 2))).isTrue();
			assertThat(maze.isConnected(new Point(7, 2), new Point(7, 3))).isTrue();
			assertThat(maze.isConnected(new Point(7, 3), new Point(7, 4))).isTrue();
			assertThat(maze.isConnected(new Point(1, 1), new Point(2, 1))).isTrue();
			assertThat(maze.isConnected(new Point(2, 1), new Point(3, 1))).isTrue();
			assertThat(maze.isConnected(new Point(3, 1), new Point(4, 1))).isTrue();
			assertThat(maze.isConnected(new Point(4, 1), new Point(5, 1))).isTrue();
			assertThat(maze.isConnected(new Point(5, 1), new Point(6, 1))).isTrue();
			assertThat(maze.isConnected(new Point(6, 1), new Point(7, 1))).isTrue();
			assertThat(maze.isConnected(new Point(7, 1), new Point(8, 1))).isTrue();
			assertThat(maze.isConnected(new Point(8, 1), new Point(9, 1))).isTrue();
			assertThat(maze.isConnected(new Point(9, 1), new Point(10, 1))).isTrue();
			assertThat(maze.isConnected(new Point(10, 1), new Point(11, 1))).isTrue();
			assertThat(maze.isConnected(new Point(11, 1), new Point(12, 1))).isTrue();
			assertThat(maze.isConnected(new Point(12, 1), new Point(13, 1))).isTrue();
			assertThat(maze.isConnected(new Point(13, 1), new Point(14, 1))).isTrue();
			assertThat(maze.isConnected(new Point(14, 1), new Point(15, 1))).isTrue();
			assertThat(maze.isConnected(new Point(15, 1), new Point(16, 1))).isTrue();
			assertThat(maze.isConnected(new Point(16, 1), new Point(17, 1))).isTrue();
			assertThat(maze.isConnected(new Point(17, 1), new Point(18, 1))).isTrue();
			assertThat(maze.isConnected(new Point(18, 1), new Point(19, 1))).isTrue();
			assertThat(maze.isConnected(new Point(19, 1), new Point(20, 1))).isTrue();
			assertThat(maze.isConnected(new Point(20, 1), new Point(21, 1))).isTrue();
			assertThat(maze.isConnected(new Point(21, 1), new Point(22, 1))).isTrue();
		}
	}

	private Resource stringResource(String code)
	{
		return new InputStreamResource(IOUtils.toInputStream(code, StandardCharsets.UTF_8));
	}
}
