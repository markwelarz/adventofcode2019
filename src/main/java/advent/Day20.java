package advent;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.awt.Point;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day20
{
	public int part1(Resource input) throws IOException
	{
		Day20.Maze maze = Day20.Maze.create(input);
		int steps = maze.shortestDistanceBetweenStartAndEnd();

		return steps;
	}

	static class Maze
	{
		private Logger logger = LoggerFactory.getLogger(Day20.Maze.class);

		private SimpleGraph<Point, DefaultEdge> maze;
		private Point entrance;
		private Point exit;

		public Maze(SimpleGraph<Point, DefaultEdge> maze, Point entrance, Point exit)
		{
			this.maze = maze;
			this.entrance = entrance;
			this.exit = exit;
		}

		public int shortestDistanceBetweenStartAndEnd()
		{
			DijkstraShortestPath<Point, DefaultEdge> dijkstraAlg = new DijkstraShortestPath<>(maze);
			ShortestPathAlgorithm.SingleSourcePaths<Point, DefaultEdge> iPaths = dijkstraAlg.getPaths(entrance);
			GraphPath<Point, DefaultEdge> path = iPaths.getPath(exit);
			return path.getLength();
		}

		public Set<Point> getPoints()
		{
			return maze.vertexSet();
		}

		public List<String> getConnections()
		{
			return maze.edgeSet().stream().map(DefaultEdge::toString).collect(Collectors.toList());
		}

		public Point getEntrance()
		{
			return entrance;
		}

		public Point getExit()
		{
			return exit;
		}

		public static Day20.Maze create(Resource input) throws IOException
		{
			SimpleGraph<Point, DefaultEdge> maze = new SimpleGraph<>(DefaultEdge.class);
			List<PortalLetter> portalLetters = new ArrayList<>();

			int yPos = 0;
			LineIterator it = IOUtils.lineIterator(input.getInputStream(), StandardCharsets.UTF_8);
			while (it.hasNext())
			{
				String line = it.next();
				for (int xPos = 0; xPos < line.length(); xPos++)
				{
					if (line.charAt(xPos) == '.')
						addPoint(maze, xPos, yPos);
					else if (Character.isUpperCase(line.charAt(xPos)))
						portalLetters.add(new PortalLetter(line.charAt(xPos), new Point(xPos, yPos)));
				}
				yPos++;
			}

			Map<String, Point> portals = joinPortals(maze, portalLetters);
			Point entrance = portals.get("AA");
			Point exit = portals.get("ZZ");

			return new Maze(maze, entrance, exit);
		}

		private static Map<String, Point> joinPortals(SimpleGraph<Point, DefaultEdge> maze,
						List<PortalLetter> portalLetters)
		{
			Map<String, Point> portals = new HashMap<>();

			for (int i = 0; i < portalLetters.size(); i++)
			{
				PortalLetter portalLetterOne = portalLetters.get(i);
				for (int j = i + 1; j < portalLetters.size(); j++)
				{
					PortalLetter portalLetterTwo = portalLetters.get(j);
					if (portalLetterOne != portalLetterTwo)
					{
						String portalName = String.valueOf(portalLetterOne.letter) + portalLetterTwo.letter;
						if (isHorizPortal(portalLetterOne.position, portalLetterTwo.position))
						{
							Point entranceLeft = new Point(portalLetterOne.position.x - 1, portalLetterOne.position.y);
							Point entranceRight = new Point(portalLetterTwo.position.x + 1, portalLetterTwo.position.y);
							handlePortal(maze, portals, portalName, entranceLeft, entranceRight);
						}
						else if (isVertPortal(portalLetterOne.position, portalLetterTwo.position))
						{
							Point entranceTop = new Point(portalLetterOne.position.x, portalLetterOne.position.y - 1);
							Point entranceBottom = new Point(portalLetterTwo.position.x,
											portalLetterTwo.position.y + 1);
							handlePortal(maze, portals, portalName, entranceTop, entranceBottom);
						}
					}
				}
			}

			return portals;
		}

		private static void handlePortal(SimpleGraph<Point, DefaultEdge> maze, Map<String, Point> portals,
						String portalName, Point entrancePoint1, Point entrancePoint2)
		{
			if (maze.containsVertex(entrancePoint1))
			{
				if (portals.containsKey(portalName))
					maze.addEdge(entrancePoint1, portals.get(portalName));
				else
					portals.put(portalName, entrancePoint1);
			}
			else if (maze.containsVertex(entrancePoint2))
			{
				if (portals.containsKey(portalName))
					maze.addEdge(entrancePoint2, portals.get(portalName));
				else
					portals.put(portalName, entrancePoint2);
			}
			else
				throw new IllegalArgumentException();
		}

		private static boolean isHorizPortal(Point p1, Point p2)
		{
			if (p1.y == p2.y)
			{
				if (p1.x + 1 == p2.x)
					return true;
				else if (p1.x - 1 == p2.x)
					return true;
			}
			return false;
		}

		private static boolean isVertPortal(Point p1, Point p2)
		{
			if (p1.x == p2.x)
			{
				if (p1.y + 1 == p2.y)
					return true;
				else if (p1.y - 1 == p2.y)
					return true;
			}

			return false;
		}

		private static void addPoint(SimpleGraph<Point, DefaultEdge> maze, int xPos, int yPos)
		{
			Point mazePoint = new Point(xPos, yPos);
			maze.addVertex(mazePoint);
			addEdges(maze, mazePoint, xPos, yPos - 1); // up
			addEdges(maze, mazePoint, xPos, yPos + 1); // down
			addEdges(maze, mazePoint, xPos - 1, yPos); // left
			addEdges(maze, mazePoint, xPos + 1, yPos); // right
		}

		private static void addEdges(SimpleGraph<Point, DefaultEdge> maze, Point newPoint, int adjX,
						int adjY)
		{
			Point mp = new Point(adjX, adjY);
			if (maze.containsVertex(mp))
				maze.addEdge(newPoint, mp);
		}

		public boolean isConnected(Point p1, Point p2)
		{
			return maze.containsEdge(p1, p2);
		}
	}

	static class PortalLetter
	{
		Character letter;
		Point position;

		public PortalLetter(Character letter, Point position)
		{
			this.letter = letter;
			this.position = position;
		}
	}
}

