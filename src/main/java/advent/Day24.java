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
			advanceMinute(0);
			repeated = doesRepeatAPreviousState(0);
		}
	}

	boolean doesRepeatAPreviousState(int level)
	{
		int[][] grid = grids.get(level);
		boolean found = previousGrids.stream().anyMatch(v -> Arrays.deepEquals(v, grid));
		return found;
	}

	public void advanceMinute(int level)
	{
		int[][] newGrid = new int[5][5];
		int[][] grid = grids.get(level);

		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				int numberOfAdjecentBugs = aboveOf(x, y, level) + belowOf(x, y, level) +
								leftOf(x, y, level) + rightOf(x, y, level);
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

	private int leftOf(int x, int y, int level)
	{
		int[][] grid = grids.get(level);

		if (x == 0)
			return 0;

		if (grid[y][x - 1] == 1)
			return 1;
		else
			return 0;
	}

	private int rightOf(int x, int y, int level)
	{
		int[][] grid = grids.get(level);

		if (x >= grid[y].length - 1)
			return 0;

		if (grid[y][x + 1] == 1)
			return 1;
		else
			return 0;
	}

	private int belowOf(int x, int y, int level)
	{
		int[][] grid = grids.get(level);

		if (y >= grid[y].length - 1)
			return 0;

		if (grid[y + 1][x] == 1)
			return 1;
		else
			return 0;
	}

	private int aboveOf(int x, int y, int level)
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
