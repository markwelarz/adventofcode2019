package advent;

import advent.intcode.IntcodeComputerFacade;
import advent.intcode.IntcodeComputerListener;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterators;
import com.google.common.collect.Multiset;
import com.google.common.collect.PeekingIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.paukov.combinatorics3.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day17
{
	private Logger logger = LoggerFactory.getLogger(Day17.class);

	public static final int MAX_SIZE = 20 / 2;

	public int part1(Resource program) throws IOException, InterruptedException
	{
		ScaffholdReader scaffholdReader = new ScaffholdReader();
		IntcodeComputerFacade computer = new IntcodeComputerFacade(program, 2);
		computer.addListener(scaffholdReader);
		computer.run();

		scaffholdReader.printMap();

		return alignmentParams(scaffholdReader);
	}

	public Set<MovementProgram> part2FindPrograms(Resource program) throws IOException, InterruptedException
	{
		ScaffholdReader scaffholdReader = new ScaffholdReader();
		IntcodeComputerFacade computer = new IntcodeComputerFacade(program, 2);
		computer.addListener(scaffholdReader);
		computer.run();

		scaffholdReader.printMap();

		logger.debug("calculating routes");
		List<List<String>> allCommands = scaffholdReader.startToEndRoute();
		logger.debug("finished calculating routes, found {}", allCommands.size());

		Validate.isTrue(allCommands.size() > 0);

		logger.debug("factoring out common functions");

		Set<MovementProgram> programs = allCommands.stream().parallel()
						.map(v -> factorize(v, 0))
						.filter(v -> !v.isEmpty())
						.findFirst().get();

		logger.debug("finished, have {} programs", programs.size());

		return programs;
	}

	public void part2PrintMap(Resource program) throws IOException, InterruptedException
	{
		ScaffholdReader scaffholdReader = new ScaffholdReader();
		IntcodeComputerFacade computer = new IntcodeComputerFacade(program, 2);
		computer.addListener(scaffholdReader);
		computer.run();

		scaffholdReader.printMap();
	}

	public int alignmentParams(ScaffholdReader scaffholdReader)
	{
		List<Point> intersectionPoints = scaffholdReader.intersections();
		return intersectionPoints.stream()
						.map(v -> v.x * v.y)
						.mapToInt(v -> v)
						.sum();
	}

	public Set<MovementProgram> factorize(List<String> commands, long index)
	{
		logger.debug("factorize index={}, commands={}", index, commands);

		String commandString = Joiner.on(",").join(commands);
		Set<MovementProgram> validPrograms = allCombinations(commands, commandString, new LinkedList<>(), 0);

		return validPrograms;
	}

	public int part2Solution(Resource program, MovementProgram vacuumRobotProgram)
					throws IOException, InterruptedException
	{
		IntcodeComputerFacade computer = new IntcodeComputerFacade(program, 10);
		computer.setMemory(0, 2);
		VacuumRobot vacuumRobot = new VacuumRobot();
		computer.addListener(vacuumRobot);
		sendProgram(computer, vacuumRobotProgram);
		computer.run();

		logger.debug("dust is {}", vacuumRobot.dust);

		return vacuumRobot.dust;
	}

	private void sendProgram(IntcodeComputerFacade computer, MovementProgram vacuumRobotProgram)
	{
		vacuumRobotProgram.mainRoutine.chars().forEach(v -> computer.send(v));
		computer.send(10);
		vacuumRobotProgram.functions.get(0).chars().forEach(v -> computer.send(v));
		computer.send(10);
		vacuumRobotProgram.functions.get(1).chars().forEach(v -> computer.send(v));
		computer.send(10);
		vacuumRobotProgram.functions.get(2).chars().forEach(v -> computer.send(v));
		computer.send(10);

		computer.send(110);
		computer.send(10);
	}

	static class VacuumRobot implements IntcodeComputerListener
	{
		private Logger logger = LoggerFactory.getLogger(VacuumRobot.class);

		private Deque<StringBuilder> map = new LinkedList<>();
		private int dust;

		public VacuumRobot()
		{
			map.add(new StringBuilder());
		}

		@Override
		public void onMessage(String message)
		{
			//			logger.debug("received [{}]", message);

			if (message.length() > 3)
			{
				dust = Integer.parseInt(message);
			}
			else
			{
				char ch[] = Character.toChars(Integer.parseInt(message));
				if (ch.length > 3)
				{
					dust = Integer.parseInt(message);
				}
				if (ch[0] == 10)
				{
					map.add(new StringBuilder());
				}
				else
				{
					map.getLast().append(ch);
				}
			}
		}

		@Override
		public void finished()
		{
			logger.debug("finished");
			map.forEach(v -> System.out.println(v));

		}
	}

	static class MovementProgram
	{
		String mainRoutine;
		List<String> functions;

		public MovementProgram(String mainRoutine, List<String> functions)
		{
			this.mainRoutine = mainRoutine;
			this.functions = functions;
		}

		public String expand()
		{
			String expanded = mainRoutine;
			expanded = StringUtils.replace(expanded, "A", functions.get(0));
			expanded = StringUtils.replace(expanded, "B", functions.get(1));
			expanded = StringUtils.replace(expanded, "C", functions.get(2));

			return expanded;
		}

		@Override
		public String toString()
		{
			return new ToStringBuilder(this)
							.append("mainRoutine", mainRoutine)
							.append("functions", functions)
							.toString();
		}
	}

	private boolean isValid(String mainRoutine)
	{
		// 20 or fewer?
		if (mainRoutine.length() <= 20)
		{
			// all commands in main routine are replaced?
			// no L,R or numbers left in mainRoutine?
			if (StringUtils.indexOfAny(mainRoutine, "01234567890LR") == -1)
			{
				return true;
			}
		}
		return false;
	}

	private MovementProgram validProgram(String commandString, List<String> setOf3Functions)
	{
		String mainRoutine = commandString;
		char functionName = 'A';
		for (String functionDef : setOf3Functions)
		{
			mainRoutine = StringUtils.replace(mainRoutine, functionDef, String.valueOf(functionName++));
		}

		if (isValid(mainRoutine))
		{
			return new MovementProgram(mainRoutine, setOf3Functions);
		}
		return null;
	}

	public Set<MovementProgram> allCombinations(List<String> commands, String commandString, Deque<List<String>> funcs,
					int startAt)
	{
		Set<MovementProgram> result = new HashSet<>();

		for (int funcSize = MAX_SIZE; funcSize >= 2; funcSize--)
		{
			if (funcs.size() == 0)
				logger.debug("allCombinations {} funcSize {}, startAt {}", funcs.size(), funcSize, startAt);

			for (int startOfFunction = startAt;
				 startOfFunction + funcSize < commands.size(); startOfFunction++)
			{
				List<String> func = commands.subList(startOfFunction, startOfFunction + funcSize);
				if (Collections.lastIndexOfSubList(commands, func) != startOfFunction)
				{
					funcs.add(func);

					if (funcs.size() < 3)
						result.addAll(allCombinations(commands, commandString, funcs, startOfFunction + funcSize));
					else
					{
						List<String> functionStrings = funcs.stream()
										.map(v -> v.stream().collect(Collectors.joining(",")))
										.collect(Collectors.toList());

						MovementProgram mp = validProgram(commandString, functionStrings);
						if (mp != null)
						{
							logger.debug("found match");
							result.add(mp);
						}
					}

					funcs.removeLast();
				}
			}
		}
		return result;
	}

	static class ScaffholdReader implements IntcodeComputerListener
	{
		private Logger logger = LoggerFactory.getLogger(ScaffholdReader.class);
		private Deque<StringBuilder> map = new LinkedList<>();
		private SimpleGraph<Point, DefaultEdge> scaffholding = new SimpleGraph<>(DefaultEdge.class);
		private Point startPosition;
		private Point endPosition;

		public ScaffholdReader()
		{
			map.add(new StringBuilder());
		}

		@Override
		public void onMessage(String message)
		{
			logger.debug("received [{}]", message);

			char ch[] = Character.toChars(Integer.parseInt(message));
			if (ch[0] == 10)
			{
				map.add(new StringBuilder());
			}
			else
			{
				map.getLast().append(ch);
			}
		}

		@Override
		public void finished()
		{
			int lineNum = 0;

			CharMatcher scaffholdMatcher = CharMatcher.anyOf("#^");
			scaffholding = new SimpleGraph<>(DefaultEdge.class);

			for (StringBuilder line : this.map)
			{
				int pos = scaffholdMatcher.indexIn(line);
				while (pos != -1)
				{
					scaffholding.addVertex(new Point(pos, lineNum));
					pos = scaffholdMatcher.indexIn(line, pos + 1);
				}

				int startPositionX = line.indexOf("^");
				if (startPositionX >= 0)
					startPosition = new Point(startPositionX, lineNum);

				lineNum++;
			}

			Generator.combination(scaffholding.vertexSet())
							.simple(2)
							.stream()
							.filter(v -> isAdjacent(v.get(0), v.get(1)))
							.forEach(v -> scaffholding.addEdge(v.get(0), v.get(1)));

			endPosition = scaffholding.vertexSet().stream()
							.filter(v -> !v.equals(startPosition))
							.filter(v -> scaffholding.edgesOf(v).size() == 1)
							.findFirst()
							.get();

			logger.debug("{}", scaffholding.edgeSet());
		}

		public void printMap()
		{
			for (StringBuilder line : this.map)
			{
				System.out.println(line);
			}
		}

		public void printMap(Deque<Point> highlight)
		{
			int lineNum = 0;

			for (StringBuilder line : this.map)
			{
				for (int x = 0; x < line.length(); x++)
				{
					if (highlight.contains(new Point(x, lineNum)))
					{
						System.out.print("@");
					}
					else
					{
						System.out.print(line.charAt(x));
					}
				}

				System.out.println("");

				lineNum++;
			}
		}

		public List<Point> intersections()
		{
			return scaffholding.vertexSet().stream()
							.filter(v -> scaffholding.edgesOf(v).size() == 4)
							.collect(Collectors.toList());
		}

		public List<List<String>> startToEndRoute()
		{
			List<List<String>> allRouteCommands = new ArrayList<>();
			AllPaths allPaths = new AllPaths(scaffholding, startPosition, endPosition);
			List<Deque<Point>> all = allPaths.getPaths(intersections());
			List<Deque<Point>> hitAll = all.stream()
							.filter(this::containsAllScaffholdingPoints)
							.collect(Collectors.toList());
			logger.debug("total paths {}, paths-with-all-hit {}", all.size(), hitAll.size());

			for (Deque<Point> route : hitAll)
			{
				//				printMap(route);
				//				System.out.println("--------------------------");

				List<String> commands = pathToCommands(route);
				allRouteCommands.add(commands);
			}

			return allRouteCommands;
		}

		enum Direction
		{
			UP, DOWN, LEFT, RIGHT
		}

		private List<String> pathToCommands(Deque<Point> route)
		{
			List<String> commands = new ArrayList<>();
			Direction direction = Direction.UP;
			PeekingIterator<Point> it = Iterators.peekingIterator(route.iterator());
			int stepsInSameDirection = 0;

			while (it.hasNext())
			{
				Point current = it.next();
				if (!it.hasNext())
					continue;
				Point next = it.peek();

				Direction travelIn = directionOfTravel(current, next);
				if (travelIn != direction)
				{
					if (stepsInSameDirection > 0)
					{
						commands.add(String.valueOf(stepsInSameDirection + 1));
						stepsInSameDirection = 0;
					}
					commands.add(turnCommand(direction, travelIn));
					direction = travelIn;

				}
				else
					stepsInSameDirection++;
			}

			if (stepsInSameDirection > 0)
			{
				commands.add(String.valueOf(stepsInSameDirection + 1));
			}

			return commands;
		}

		private boolean containsAllScaffholdingPoints(Collection<Point> points)
		{
			Set<Point> pointsHit = new HashSet<>(points);
			return pointsHit.containsAll(scaffholding.vertexSet());
		}

		private ScaffholdReader.Direction directionOfTravel(Point p1, Point p2)
		{
			if (p2.x > p1.x)
				return ScaffholdReader.Direction.RIGHT;
			else if (p2.x < p1.x)
				return ScaffholdReader.Direction.LEFT;
			else if (p2.y > p1.y)
				return ScaffholdReader.Direction.DOWN;
			else if (p2.y < p1.y)
				return ScaffholdReader.Direction.UP;

			throw new IllegalArgumentException();
		}

		private String turnCommand(Direction facing, Direction wantToBeFacing)
		{
			if (facing == Direction.LEFT && wantToBeFacing == Direction.UP)
				return "R";
			else if (facing == Direction.LEFT && wantToBeFacing == Direction.DOWN)
				return "L";
			else if (facing == Direction.RIGHT && wantToBeFacing == Direction.UP)
				return "L";
			else if (facing == Direction.RIGHT && wantToBeFacing == Direction.DOWN)
				return "R";
			else if (facing == Direction.UP && wantToBeFacing == Direction.LEFT)
				return "L";
			else if (facing == Direction.UP && wantToBeFacing == Direction.RIGHT)
				return "R";
			else if (facing == Direction.DOWN && wantToBeFacing == Direction.LEFT)
				return "R";
			else if (facing == Direction.DOWN && wantToBeFacing == Direction.RIGHT)
				return "L";

			throw new IllegalArgumentException();
		}
	}

	private static boolean isAdjacent(Point p1, Point p2)
	{
		if (p1.x == p2.x)
		{
			if (p1.y + 1 == p2.y)
				return true;
			else if (p1.y - 1 == p2.y)
				return true;
		}
		else if (p1.y == p2.y)
		{
			if (p1.x + 1 == p2.x)
				return true;
			else if (p1.x - 1 == p2.x)
				return true;
		}
		return false;
	}

	static class AllPaths
	{
		private Logger logger = LoggerFactory.getLogger(AllPaths.class);

		private SimpleGraph<Point, DefaultEdge> graph;
		private Point startPoint;
		private Point endPoint;
		private Set<Point> visitedPoints = new HashSet<>();
		private Multiset<Point> visitedIntersections = HashMultiset.create();
		private List<Deque<Point>> startToFinishPaths = new LinkedList<>();

		public AllPaths(SimpleGraph<Point, DefaultEdge> graph, Point startPoint, Point endPoint)
		{
			this.graph = graph;
			this.startPoint = startPoint;
			this.endPoint = endPoint;
		}

		public List<Deque<Point>> getPaths(List<Point> intersectionPoints)
		{
			explore(startPoint, intersectionPoints, new LinkedList<>());
			return startToFinishPaths;
		}

		public void explore(Point from, List<Point> intersections, Deque<Point> path)
		{
			//			logger.debug("exploring {}, edges are {}", from, graph.edgesOf(from));

			path.addLast(from);

			if (from.equals(endPoint))
			{
				//				logger.debug("found endpoint");
				startToFinishPaths.add(new LinkedList<>(path));
			}
			else
			{
				visitedPoints.add(from);
				if (intersections.contains(from))
					visitedIntersections.add(from);

				Set<DefaultEdge> edges = graph.edgesOf(from);
				for (DefaultEdge edge : edges)
				{
					Point dest1 = graph.getEdgeTarget(edge);
					if (!visitedPoints.contains(dest1) ||
									(intersections.contains(dest1) && dest1 != from
													&& visitedIntersections.count(dest1) < 2))
					{
						explore(dest1, intersections, path);
					}

					Point dest2 = graph.getEdgeSource(edge);
					if (!visitedPoints.contains(dest2) ||
									(intersections.contains(dest2) && dest2 != from
													&& visitedIntersections.count(dest2) < 2))
					{
						explore(dest2, intersections, path);
					}
				}

				visitedIntersections.remove(from);
				visitedPoints.remove(from);
			}

			path.removeLast();
		}
	}
}

