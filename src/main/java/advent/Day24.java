package advent;

import com.google.common.math.IntMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day24
{
	private static Logger logger = LoggerFactory.getLogger(Day24.class);

	private List<int[][]> previousGrids = new ArrayList<>();
	private Map<Integer, int[][]> grids = new HashMap<>();

	public Day24()
	{

	}

	public void parseInput(Resource input) throws IOException
	{
		int[][] grid = new int[5][];
		Scanner scanner = new Scanner(input.getInputStream());
		int lineNum = 0;
		while (scanner.hasNext())
		{
			String lineText = scanner.nextLine();
			int line[] = lineText.chars().map(v -> v == '#' ? 1 : 0).toArray();
			grid[lineNum++] = line;
		}

		grids.put(0, grid);
	}

	public void part1(Resource input) throws IOException
	{
		int minute = 0;
		boolean repeated = false;

		parseInput(input);

		while (!repeated)
		{
			minute++;

			logger.debug("advancing to {}", minute);
			advanceMinutePart1(0);
			repeated = doesRepeatAPreviousState(0);
		}
	}

	boolean doesRepeatAPreviousState(int level)
	{
		int[][] grid = grids.get(level);
		boolean found = previousGrids.stream().anyMatch(v -> Arrays.deepEquals(v, grid));
		return found;
	}

	public void advanceMinutePart1(int level)
	{
		int[][] newGrid = new int[5][5];
		int[][] grid = grids.get(level);

		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{

				int numberOfAdjecentBugs = aboveOfPart1(x, y, level) + belowOfPArt1(x, y, level) +
								leftOfPart1(x, y, level) + rightOfPart1(x, y, level);
				if (grid[y][x] == 1 && numberOfAdjecentBugs == 1)
				{
					newGrid[y][x] = 1;
				}
				else if (grid[y][x] == 0 && (numberOfAdjecentBugs == 1 || numberOfAdjecentBugs == 2))
				{
					newGrid[y][x] = 1;
				}
			}
		}

		previousGrids.add(grid);
		grids.put(level, newGrid);
	}

	public int part2(Resource input, int minutes) throws IOException
	{
		parseInput(input);

		for (int minute = 1; minute <= minutes; minute++)
		{
			Map<Integer, int[][]> newMinuteGrids = new HashMap<>();

			logger.debug("doing minute {}", minute);

			for (Map.Entry<Integer, int[][]> e : grids.entrySet())
			{
				logger.debug("advancing level {}", e.getKey());
				advanceMinutePart2(e.getKey(), e.getValue(), newMinuteGrids, true);
			}

			grids = newMinuteGrids;

			//			grids.entrySet().forEach(e -> {
			//				logger.debug("printing level {}", e.getKey());
			//				printGrid(e.getValue());
			//			});
		}

		int bugs = grids.values().stream()
						.map(this::countBugs)
						.mapToInt(v -> v).sum();
		return bugs;
	}

	int[][] getOrPopulateGridLevel(int level, Map<Integer, int[][]> workingGridLevels, boolean expandLevel)
	{
		int[][] grid = grids.get(level);
		if (grid == null)
		{
			grid = new int[5][5];
			if (expandLevel)
			{
				Map<Integer, int[][]> newLevels = new HashMap<>();
				advanceMinutePart2(level, grid, newLevels, false);
				workingGridLevels.put(level, newLevels.get(level));
			}
		}
		return grid;
	}

	public void advanceMinutePart2(int level, int[][] grid, Map<Integer, int[][]> workingGridLevels,
					boolean expandLevel)
	{
		int[][] newGrid = new int[5][5];

		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				if (!(x == 2 && y == 2))
				{
					int numberOfAdjecentBugs = aboveOfPart2(x, y, grid, level, workingGridLevels, expandLevel) +
									belowOfPart2(x, y, grid, level, workingGridLevels, expandLevel) +
									leftOfPart2(x, y, grid, level, workingGridLevels, expandLevel) +
									rightOfPart2(x, y, grid, level, workingGridLevels, expandLevel);

					if (grid[y][x] == 1 && numberOfAdjecentBugs == 1)
					{
						newGrid[y][x] = 1;
					}
					else if (grid[y][x] == 0 && (numberOfAdjecentBugs == 1 || numberOfAdjecentBugs == 2))
					{
						newGrid[y][x] = 1;
					}
				}
			}
		}

		workingGridLevels.put(level, newGrid);
	}

	private int leftOfPart2(int x, int y, int[][] grid, int level, Map<Integer, int[][]> workingGridLevels,
					boolean expandLevel)
	{

		if (x == 0)
		{
			int[][] containingGrid = getOrPopulateGridLevel(level - 1, workingGridLevels, expandLevel);
			//			logger.debug("leftOfPart2 level={} returns {}", level, containingGrid[2][1]);
			return containingGrid[2][1];
		}
		else if (x == 3 && y == 2)
		{
			int[][] containingGrid = getOrPopulateGridLevel(level + 1, workingGridLevels, expandLevel);
			int result = containingGrid[0][4] + containingGrid[1][4] + containingGrid[2][4] + containingGrid[3][4]
							+ containingGrid[4][4];
			//			logger.debug("leftOfPart2 level={} returns {}", level, result);
			return result;
		}

		if (grid[y][x - 1] == 1)
		{
			//			logger.debug("leftOfPart2 level={} returns {}", level, 1);
			return 1;
		}
		else
		{
			//			logger.debug("leftOfPart2 level={} returns {}", level, 0);
			return 0;
		}
	}

	private int rightOfPart2(int x, int y, int[][] grid, int level, Map<Integer, int[][]> workingGridLevels,
					boolean expandLevel)
	{

		if (x == 1 && y == 2)
		{
			int[][] containingGrid = getOrPopulateGridLevel(level + 1, workingGridLevels, expandLevel);
			int result = containingGrid[0][0] + containingGrid[1][0] + containingGrid[2][0] + containingGrid[3][0]
							+ containingGrid[4][0];
			//			logger.debug("rightOfPart2 level={} returns {}", level, result);
			return result;
		}
		else if (x == 4)
		{
			int[][] containingGrid = getOrPopulateGridLevel(level - 1, workingGridLevels, expandLevel);
			//			logger.debug("rightOfPart2 level={} returns {}", level, containingGrid[2][3]);
			return containingGrid[2][3];
		}

		if (grid[y][x + 1] == 1)
		{
			//			logger.debug("rightOfPart2 level={} returns {}", level, 1);
			return 1;
		}
		else
		{
			//			logger.debug("rightOfPart2 level={} returns {}", level, 0);
			return 0;
		}
	}

	private int belowOfPart2(int x, int y, int[][] grid, int level, Map<Integer, int[][]> workingGridLevels,
					boolean expandLevel)
	{
		if (y == 4)
		{
			int[][] containingGrid = getOrPopulateGridLevel(level - 1, workingGridLevels, expandLevel);
			//			logger.debug("rightOfPart2 level={} returns {}", level, containingGrid[3][2]);
			return containingGrid[3][2];
		}
		else if (x == 2 && y == 1)
		{
			int[][] containingGrid = getOrPopulateGridLevel(level + 1, workingGridLevels, expandLevel);
			int result = containingGrid[0][0] + containingGrid[0][1] + containingGrid[0][2] + containingGrid[0][3]
							+ containingGrid[0][4];
			//			logger.debug("rightOfPart2 level={} returns {}", level, result);
			return result;
		}

		if (grid[y + 1][x] == 1)
		{
			//			logger.debug("belowOfPart2 level={} returns {}", level, 1);
			return 1;
		}
		else
		{
			//			logger.debug("belowOfPart2 level={} returns {}", level, 0);
			return 0;
		}
	}

	private int aboveOfPart2(int x, int y, int[][] grid, int level, Map<Integer, int[][]> workingGridLevels,
					boolean expandLevel)
	{
		if (y == 0)
		{
			int[][] containingGrid = getOrPopulateGridLevel(level - 1, workingGridLevels, expandLevel);
			//			logger.debug("aboveOfPart2 level={} returns {}", level, containingGrid[1][2]);
			return containingGrid[1][2];
		}
		else if (x == 2 && y == 3)
		{
			int[][] containingGrid = getOrPopulateGridLevel(level + 1, workingGridLevels, expandLevel);
			int result = containingGrid[4][0] + containingGrid[4][1] + containingGrid[4][2] + containingGrid[4][3]
							+ containingGrid[4][4];
			//			logger.debug("aboveOfPart2 level={} returns {}", level, result);
			return result;
		}

		if (grid[y - 1][x] == 1)
		{
			//			logger.debug("aboveOfPart2 level={} returns {}", level, 1);
			return 1;
		}
		else
		{
			//			logger.debug("aboveOfPart2 level={} returns {}", level, 0);
			return 0;
		}
	}

	private int leftOfPart1(int x, int y, int level)
	{
		int[][] grid = grids.get(level);

		if (x == 0)
			return 0;

		if (grid[y][x - 1] == 1)
			return 1;
		else
			return 0;
	}

	private int rightOfPart1(int x, int y, int level)
	{
		int[][] grid = grids.get(level);

		if (x >= grid[y].length - 1)
			return 0;

		if (grid[y][x + 1] == 1)
			return 1;
		else
			return 0;
	}

	private int belowOfPArt1(int x, int y, int level)
	{
		int[][] grid = grids.get(level);

		if (y >= grid[y].length - 1)
			return 0;

		if (grid[y + 1][x] == 1)
			return 1;
		else
			return 0;
	}

	private int aboveOfPart1(int x, int y, int level)
	{
		int[][] grid = grids.get(level);

		if (y == 0)
			return 0;

		if (grid[y - 1][x] == 1)
			return 1;
		else
			return 0;
	}

	public int[][] getGrid(int level)
	{
		return grids.get(level);
	}

	public void printGrid(int level)
	{
		int[][] grid = grids.get(level);
		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				System.out.print(grid[y][x] == 1 ? "#" : ".");
			}
			System.out.println();
		}
	}

	public void printGrid(int[][] grid)
	{
		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				System.out.print(grid[y][x] == 1 ? "#" : ".");
			}
			System.out.println();
		}
	}

	int countBugs(int grid[][])
	{
		int counter = 0;
		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				if (!(x == 2 && y == 2))
				{
					if (grid[y][x] == 1)
					{
						counter++;
					}
				}
			}
		}
		return counter;
	}

	public int biodiversityRating(int level)
	{
		int[][] grid = grids.get(level);
		int tileNumber = 0;
		int rating = 0;

		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				if (grid[y][x] == 1)
					rating += IntMath.pow(2, tileNumber);
				tileNumber++;
			}
		}

		return rating;
	}
}
