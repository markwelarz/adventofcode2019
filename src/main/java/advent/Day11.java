package advent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.awt.Point;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class Day11
{
	private Logger logger = LoggerFactory.getLogger(Day11.class);

	private LinkedBlockingQueue<String> stdin = new LinkedBlockingQueue<>();
	private LinkedBlockingQueue<String> stdout = new LinkedBlockingQueue<>();

	public int part1() throws ExecutionException, InterruptedException
	{
		Map<Point, String> paints = runRobot("0");
		print(paints);
		return paints.size();
	}

	public int part2() throws ExecutionException, InterruptedException
	{
		Map<Point, String> paints = runRobot("1");
		print(paints);
		return 0;
	}

	private void print(Map<Point, String> paints)
	{
		int xmin = paints.keySet().stream().map(v -> v.x).min(Comparator.naturalOrder()).get();
		int xmax = paints.keySet().stream().map(v -> v.x).max(Comparator.naturalOrder()).get();
		int ymin = paints.keySet().stream().map(v -> v.y).min(Comparator.naturalOrder()).get();
		int ymax = paints.keySet().stream().map(v -> v.y).max(Comparator.naturalOrder()).get();

		String grid[][] = new String[ymax + 1][xmax + 1];

		for (String line[] : grid)
		{
			Arrays.fill(line, "\u2588");
		}

		for (Map.Entry<Point, String> e : paints.entrySet())
		{
			if (e.getKey().y >= 0)
				grid[e.getKey().y][e.getKey().x] = e.getValue();
		}

		for (String line[] : grid)
		{
			for (String c : line)
			{
				if (c.equals("0"))
					System.out.print(" ");
				else
					System.out.print("\u2588");
			}
			System.out.println();
		}
	}

	private Map<Point, String> runRobot(String initialColour) throws ExecutionException, InterruptedException
	{
		Robot r = new Robot();
		Monitor m = new Monitor(initialColour);
		ExecutorService pool = Executors.newFixedThreadPool(2);

		Future<?> f1 = pool.submit(r);
		Future<?> f2 = pool.submit(m);

		f1.get();
		logger.debug("robot has completed");

		f2.cancel(true);

		return m.colours;
	}

	class Robot implements Runnable
	{
		private Logger logger = LoggerFactory.getLogger(Robot.class);

		@Override
		public void run()
		{
			try
			{
				logger.debug("robot started");

				ClassPathResource paintingProgram = new ClassPathResource("advent/day11input.txt");
				Day9Computer day9Computer = new Day9Computer(stdin, stdout);

				day9Computer.readProgramIntoMemory(paintingProgram);
				day9Computer.runProgram();
			}
			catch (IOException | InterruptedException e)
			{
				e.printStackTrace();
			}

			logger.debug("robot ended");
		}
	}

	enum Direction
	{
		UP, DOWN, LEFT, RIGHT
	}

	class RobotMoves
	{
		Direction direction = Direction.UP;
		Point position = new Point(10, 10);

		public void turnLeft()
		{
			switch (direction)
			{
				case UP:
					direction = Direction.LEFT;
					break;
				case DOWN:
					direction = Direction.RIGHT;
					break;
				case LEFT:
					direction = Direction.DOWN;
					break;
				case RIGHT:
					direction = Direction.UP;
					break;
			}
		}

		public void turnRight()
		{
			switch (direction)
			{
				case UP:
					direction = Direction.RIGHT;
					break;
				case DOWN:
					direction = Direction.LEFT;
					break;
				case LEFT:
					direction = Direction.UP;
					break;
				case RIGHT:
					direction = Direction.DOWN;
					break;
			}
		}

		public void move()
		{
			switch (direction)
			{
				case UP:
					position = new Point(position.x, position.y - 1);
					break;
				case DOWN:
					position = new Point(position.x, position.y + 1);
					break;
				case LEFT:
					position = new Point(position.x - 1, position.y);
					break;
				case RIGHT:
					position = new Point(position.x + 1, position.y);
					break;
			}
		}

		public Point getPosition()
		{
			return this.position;
		}
	}

	class Monitor implements Runnable
	{
		private Logger logger = LoggerFactory.getLogger(Monitor.class);

		public Map<Point, String> colours = new HashMap<>();

		private String initialColour;

		public Monitor(String initialColour)
		{
			this.initialColour = initialColour;
			colours.put(new Point(10, 10), initialColour);
		}

		@Override
		public void run()
		{
			try
			{
				logger.debug("monitor started");

				RobotMoves robotMoves = new RobotMoves();

				stdin.offer(initialColour);

				while (true)
				{
					logger.debug("at position {}, colour is {}", robotMoves.getPosition(),
									colours.get(robotMoves.getPosition()));

					logger.debug("awaiting robot output");
					String colour = stdout.take();
					logger.debug("robot says paint this [{}]", colour);
					colours.put(robotMoves.getPosition(), colour);
					String turn = stdout.take();
					if (turn.equals("0"))
					{
						robotMoves.turnLeft();
					}
					else if (turn.equals("1"))
					{
						robotMoves.turnRight();
					}
					else
					{
						throw new IllegalArgumentException();
					}

					robotMoves.move();
					stdin.put(colours.getOrDefault(robotMoves.getPosition(), "0"));
				}
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			logger.debug("monitor ended");
		}
	}
}
