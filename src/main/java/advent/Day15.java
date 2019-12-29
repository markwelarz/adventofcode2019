package advent;

import org.apache.commons.lang3.Validate;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.awt.Point;
import java.io.IOException;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class Day15
{
	private Logger logger = LoggerFactory.getLogger(Day15.class);

	private static final int WALL = 0;
	private static final int MOVED_OK = 1;
	private static final int FOUND_OXYGEN_SYSTEM = 2;

	private static final int NORTH = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;
	private static final int EAST = 4;

	LinkedBlockingQueue<String> stdin = new LinkedBlockingQueue<>();
	LinkedBlockingQueue<String> stdout = new LinkedBlockingQueue<>();
	Day9Computer day9Computer;
	Map<Point, Integer> allSquaresVisited = new HashMap<>();
	Deque<Point> exploreQueue = new LinkedList<>();
	Point startPos = new Point(25, 25);
	Point oxygenPos = null;

	public Day15(Resource program) throws IOException
	{
		this.day9Computer = new Day9Computer(stdin, stdout);
		this.day9Computer.readProgramIntoMemory(program);
	}

	public int part1() throws IOException, InterruptedException
	{
		Graph<Point, DefaultEdge> maze = createMaze();

		DijkstraShortestPath<Point, DefaultEdge> dijkstraAlg = new DijkstraShortestPath<>(maze);
		ShortestPathAlgorithm.SingleSourcePaths<Point, DefaultEdge> iPaths = dijkstraAlg.getPaths(startPos);
		GraphPath<Point, DefaultEdge> path = iPaths.getPath(oxygenPos);
		draw((List) path.getVertexList());

		return path.getLength();
	}

	public int part2() throws IOException, InterruptedException
	{
		Graph<Point, DefaultEdge> maze = createMaze();
		BreadthFirstIterator<Point, DefaultEdge> iterator = new BreadthFirstIterator<>(maze, oxygenPos);
		int maxDepth = 0;
		Collection<Object> longestPath = null;

		while (iterator.hasNext())
		{
			Point p = iterator.next();
			if (iterator.getDepth(p) > maxDepth)
			{
				maxDepth = iterator.getDepth(p);
			}
		}

		return maxDepth;
	}

	public Graph<Point, DefaultEdge> createMaze() throws IOException, InterruptedException
	{
		ExecutorService pool = Executors.newSingleThreadExecutor();
		Future<?> robotDroidFuture = pool.submit(new RepairDroid());
		Deque<Integer> commandHistory = new LinkedList<>();
		int steps = 0;
		SimpleGraph<Point, DefaultEdge> maze = new SimpleGraph<>(DefaultEdge.class);

		Point currentPos = startPos;
		this.addAdjacentPointsToExplore(exploreQueue, currentPos);
		allSquaresVisited.put(currentPos, MOVED_OK);
		maze.addVertex(currentPos);

		while (!exploreQueue.isEmpty())
		{
			Point toExplore = exploreQueue.pop();
			if (allSquaresVisited.containsKey(toExplore))
				continue;

			logger.debug("on step {}, next point to explore is {}, current is {}", steps, toExplore, currentPos);

			currentPos = retraceToAdjacentPoint(currentPos, toExplore, commandHistory);

			// command to move to toExplore
			int command = commandToMoveTo(currentPos, toExplore);
			this.stdin.offer(String.valueOf(command));
			int droidReply = Integer.parseInt(this.stdout.take());

			logger.debug("droid reply was {}", droidReply);

			if (droidReply == FOUND_OXYGEN_SYSTEM)
			{
				oxygenPos = toExplore;
			}

			if (droidReply == MOVED_OK || droidReply == FOUND_OXYGEN_SYSTEM)
			{
				maze.addVertex(toExplore);
				maze.addEdge(currentPos, toExplore);
				currentPos = toExplore;
				addAdjacentPointsToExplore(exploreQueue, toExplore);
				maze.addVertex(currentPos);
				commandHistory.push(command);
			}

			allSquaresVisited.put(toExplore, droidReply);

			logger.debug("drawing");
			//			draw(null);
			steps++;
		}

		logger.debug("start position={}, oxygen position={}", startPos, oxygenPos);

		return maze;
	}

	private void addAdjacentPointsToExplore(Deque<Point> exploreQueue, Point currentPoint)
	{
		for (int direction : new int[] { NORTH, SOUTH, EAST, WEST })
		{
			Point newPoint = applyCommand(currentPoint, direction);
			if (!allSquaresVisited.containsKey(newPoint) && !exploreQueue.contains(newPoint))
				exploreQueue.push(newPoint);
		}
	}

	private Point retraceToAdjacentPoint(Point startingAt, Point goingTo, Queue<Integer> stepHistory)
					throws InterruptedException
	{
		Point currentPos = startingAt;

		// retrace to adjacent point
		while (!isAdjacent(currentPos, goingTo) && !currentPos.equals(goingTo))
		{
			int originalCommand = stepHistory.remove();
			int reverseCommand = reverseCommand(originalCommand);
			this.stdin.offer(String.valueOf(reverseCommand));
			int droidReply = Integer.parseInt(this.stdout.take());
			Validate.isTrue(droidReply == MOVED_OK);
			currentPos = applyCommand(currentPos, reverseCommand);
		}

		return currentPos;
	}

	private Point applyCommand(Point currentPos, int command)
	{
		switch (command)
		{
			case NORTH:
				return new Point(currentPos.x, currentPos.y - 1);
			case SOUTH:
				return new Point(currentPos.x, currentPos.y + 1);
			case WEST:
				return new Point(currentPos.x - 1, currentPos.y);
			case EAST:
				return new Point(currentPos.x + 1, currentPos.y);
		}
		throw new IllegalArgumentException();
	}

	private int commandToMoveTo(Point currentPos, Point nextPoint)
	{
		if (currentPos.x == nextPoint.x)
		{
			if (currentPos.y + 1 == nextPoint.y)
				return SOUTH;
			else if (currentPos.y - 1 == nextPoint.y)
				return NORTH;
		}
		else if (currentPos.y == nextPoint.y)
		{
			if (currentPos.x + 1 == nextPoint.x)
				return EAST;
			else if (currentPos.x - 1 == nextPoint.x)
				return WEST;
		}

		throw new IllegalArgumentException();
	}

	private int reverseCommand(int command)
	{
		switch (command)
		{
			case NORTH:
				return SOUTH;
			case SOUTH:
				return NORTH;
			case WEST:
				return EAST;
			case EAST:
				return WEST;
		}
		throw new IllegalArgumentException();
	}

	private boolean isAdjacent(Point p1, Point p2)
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

	private void draw(Collection<Object> visitedPoints)
	{
		String grid[][] = new String[50][50];

		for (Map.Entry<Point, Integer> e : allSquaresVisited.entrySet())
		{
			if (e.getValue() == MOVED_OK)
			{
				if (visitedPoints != null && visitedPoints.contains(e.getKey()))
					grid[e.getKey().y][e.getKey().x] = "+";
				else
					grid[e.getKey().y][e.getKey().x] = " ";
			}
			else if (e.getValue() == WALL)
			{
				grid[e.getKey().y][e.getKey().x] = "\u2591";
			}
			else if (e.getValue() == FOUND_OXYGEN_SYSTEM)
			{
				grid[e.getKey().y][e.getKey().x] = "O";
			}
		}

		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				if (grid[y][x] != null)
					System.out.print(grid[y][x]);
				else
					System.out.print("\u2588");
			}
			System.out.println();
		}
	}

	class RepairDroid implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				day9Computer.runProgram();
			}
			catch (Exception e)
			{
				logger.error("robot droid failed", e);
				throw new RuntimeException(e);
			}
		}
	}
}
