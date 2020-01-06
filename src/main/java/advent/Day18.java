package advent;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.paukov.combinatorics3.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.awt.Point;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day18
{
	private Logger logger = LoggerFactory.getLogger(Day18.class);

	public void part1(Resource input) throws IOException
	{
		Maze maze = Maze.create(input);
		SimpleDirectedWeightedGraph<Key, Integer> routeGraph = createRouteGraph();
		MazePoint startPoint = maze.getStartPoint();

		Key startKey = new Key('@');
		routeGraph.addVertex(startKey);

		logger.debug("starting at");
		moveFrom(maze, routeGraph, startPoint, startKey, new HashSet<>());
	}

	static class VisitedSubtree
	{
		char from;
		Set<Character> holdingKeys;

		public VisitedSubtree(char from, Set<Character> holdingKeys)
		{
			this.from = from;
			this.holdingKeys = holdingKeys;
		}

		@Override
		public boolean equals(Object o)
		{
			if (this == o)
				return true;

			if (o == null || getClass() != o.getClass())
				return false;

			VisitedSubtree that = (VisitedSubtree) o;

			return new EqualsBuilder()
							.append(from, that.from)
							.append(holdingKeys, that.holdingKeys)
							.isEquals();
		}

		@Override
		public int hashCode()
		{
			return new HashCodeBuilder(17, 37)
							.append(from)
							.append(holdingKeys)
							.toHashCode();
		}
	}

	Map<VisitedSubtree, Integer> cache = new HashMap<>();

	public void moveFrom(Maze maze, SimpleDirectedWeightedGraph<Key, Integer> routeGraph, MazePoint fromPoint,
					Key fromKey, Set<Character> holdingKeys)
	{
		logger.debug("moving from {} and holding keys {}", fromKey, holdingKeys);

		RouteInfo routeInfo = new RouteInfo();
		routeInfo.gotKeys = holdingKeys;
		Set<Character> accessibleKeys = maze.directlyAccessibleKeys(fromPoint, routeInfo);

		// remove holding keys
		Set<Character> unvisitedKeys = new HashSet<>(accessibleKeys);
		unvisitedKeys.removeAll(holdingKeys);

		if (!cache.containsKey(new VisitedSubtree(fromKey.key, holdingKeys)))
		{
			int perm = 1;
			List<List<Character>> keySeqPermutations = Generator.permutation(unvisitedKeys)
							.simple().stream().collect(Collectors.toList());
			for (List<Character> keySeq : keySeqPermutations)
			{
				logger.debug("trying permutation {} of {} : {}", perm++, keySeqPermutations.size(), keySeq);

				Set<Character> keysCopy = new HashSet<>(holdingKeys);
				for (char key : keySeq)
				{
					MazePoint nextKeyPoint = maze.findKeyLocation(key);
					int steps = maze.shortestRouteBetween(fromPoint, nextKeyPoint);

					Key destKey = new Key(key);
					routeGraph.addVertex(destKey);
					routeGraph.addEdge(fromKey, destKey, steps);
					keysCopy.add(destKey.key);

					moveFrom(maze, routeGraph, nextKeyPoint, destKey, keysCopy);
				}
			}

			cache.put(new VisitedSubtree(fromKey.key, unvisitedKeys), 1);
		}
		else
		{
			logger.debug("cache hit");
		}
		holdingKeys.addAll(accessibleKeys);
	}

	private SimpleDirectedWeightedGraph<Key, Integer> createRouteGraph()
	{
		SimpleDirectedWeightedGraph<Key, Integer> routeGraph = new SimpleDirectedWeightedGraph<>(
						Integer.class);
		return routeGraph;
	}

	static class Key
	{
		char key;

		public Key(char key)
		{
			this.key = key;
		}

		@Override
		public String toString()
		{
			return new ToStringBuilder(this)
							.append("key", key)
							.toString();
		}
	}

	static class RouteInfo
	{
		Set<MazePoint> visited = new HashSet<>();
		Set<Character> gotKeys = new HashSet<>();
	}

	static class Maze
	{
		private Logger logger = LoggerFactory.getLogger(Maze.class);

		private SimpleGraph<MazePoint, DefaultEdge> maze;

		public Maze(SimpleGraph<MazePoint, DefaultEdge> maze)
		{
			this.maze = maze;
		}

		public Set<Character> directlyAccessibleKeys(MazePoint from, RouteInfo routeInfo)
		{
			//			logger.debug("directlyAccessibleKeys from: {}", from);

			Set<Character> keysFound = new HashSet<>();
			if (from.containsKey() && !routeInfo.gotKeys.contains(from.contains))
			{
				keysFound.add(from.contains);
				return keysFound;
			}

			routeInfo.visited.add(from);
			Set<DefaultEdge> paths = maze.edgesOf(from);
			for (DefaultEdge path : paths)
			{
				MazePoint dest = edgeDest(from, path);
				if (!routeInfo.visited.contains(dest))
				{
					if (dest.canPass(routeInfo.gotKeys))
						keysFound.addAll(directlyAccessibleKeys(dest, routeInfo));
				}
			}
			return keysFound;
		}

		private MazePoint edgeDest(MazePoint source, DefaultEdge path)
		{
			if (maze.getEdgeTarget(path) != source)
				return maze.getEdgeTarget(path);
			else if (maze.getEdgeSource(path) != source)
				return maze.getEdgeSource(path);

			throw new IllegalArgumentException();
		}

		public Set<MazePoint> getPoints()
		{
			return maze.vertexSet();
		}

		public List<String> getConnections()
		{
			return maze.edgeSet().stream().map(DefaultEdge::toString).collect(Collectors.toList());
		}

		public static Maze create(Resource input) throws IOException
		{
			SimpleGraph<MazePoint, DefaultEdge> maze = new SimpleGraph<>(DefaultEdge.class);

			int yPos = 0;
			LineIterator it = IOUtils.lineIterator(input.getInputStream(), StandardCharsets.UTF_8);
			while (it.hasNext())
			{
				String line = it.next();
				for (int xPos = 0; xPos < line.length(); xPos++)
				{
					if (line.charAt(xPos) != '#')
						addPoint(maze, xPos, yPos, line.charAt(xPos));
				}
				yPos++;
			}

			return new Maze(maze);
		}

		private static void addPoint(SimpleGraph<MazePoint, DefaultEdge> maze, int xPos, int yPos, char contains)
		{
			MazePoint mazePoint = new MazePoint(contains, xPos, yPos);
			maze.addVertex(mazePoint);
			addEdges(maze, mazePoint, xPos, yPos - 1); // up
			addEdges(maze, mazePoint, xPos, yPos + 1); // down
			addEdges(maze, mazePoint, xPos - 1, yPos); // left
			addEdges(maze, mazePoint, xPos + 1, yPos); // right
		}

		private static void addEdges(SimpleGraph<MazePoint, DefaultEdge> maze, MazePoint newPoint, int adjX, int adjY)
		{
			MazePoint mp = findMazePoint(maze, adjX, adjY);
			if (mp != null)
				maze.addEdge(newPoint, mp);
		}

		public boolean isConnected(Point p1, Point p2)
		{
			MazePoint mp1 = findMazePoint(maze, p1.x, p1.y);
			MazePoint mp2 = findMazePoint(maze, p2.x, p2.y);
			return maze.containsEdge(mp1, mp2);
		}

		private static MazePoint findMazePoint(SimpleGraph<MazePoint, DefaultEdge> maze, int x, int y)
		{
			return maze.vertexSet().stream().filter(v -> v.xPos == x && v.yPos == y).findFirst().orElse(null);
		}

		private MazePoint findKeyLocation(char key)
		{
			return maze.vertexSet().stream().filter(v -> v.contains == key).findFirst().get();
		}

		public MazePoint findMazePointAt(int x, int y)
		{
			return findMazePoint(maze, x, y);
		}

		public MazePoint getStartPoint()
		{
			return maze.vertexSet().stream().filter(v -> v.contains == '@').findFirst().get();
		}

		public int shortestRouteBetween(MazePoint start, MazePoint nextKey)
		{
			DijkstraShortestPath<MazePoint, DefaultEdge> dijkstraAlg = new DijkstraShortestPath<>(maze);
			ShortestPathAlgorithm.SingleSourcePaths<MazePoint, DefaultEdge> iPaths = dijkstraAlg.getPaths(start);
			GraphPath<MazePoint, DefaultEdge> path = iPaths.getPath(nextKey);
			return path.getLength();
		}
	}

	static class MazePoint
	{
		private int xPos, yPos;
		private char contains;

		public MazePoint(char contains, int xPos, int yPos)
		{
			this.xPos = xPos;
			this.yPos = yPos;
			this.contains = contains;
		}

		public boolean canPass(Set<Character> holdingKeys)
		{
			if (Character.isUpperCase(contains) && !holdingKeys.contains(Character.toLowerCase(contains)))
				return false;
			else
				return true;
		}

		public boolean containsKey()
		{
			return Character.isLowerCase(contains);
		}

		@Override
		public String toString()
		{
			return new ToStringBuilder(this)
							.append("xPos", xPos)
							.append("yPos", yPos)
							.append("contains", contains)
							.toString();
		}
	}
}
