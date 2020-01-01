package advent;

import advent.intcode.Day9Computer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.awt.Point;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Day13
{
	private Logger logger = LoggerFactory.getLogger(Day13.class);

	private static final int EMPTY = 0;
	private static final int WALL = 1;
	private static final int BLOCK = 2;
	private static final int PADDLE = 3;
	private static final int BALL = 4;

	LinkedBlockingQueue<String> stdin = new LinkedBlockingQueue<>();
	LinkedBlockingQueue<String> stdout = new LinkedBlockingQueue<>();
	Day9Computer day9Computer;

	public static LinkedBlockingQueue<String> part1(Resource program) throws IOException, InterruptedException
	{
		LinkedBlockingQueue<String> stdin = new LinkedBlockingQueue<>();
		LinkedBlockingQueue<String> stdout = new LinkedBlockingQueue<>();
		Day9Computer day9Computer = new Day9Computer(stdin, stdout);
		day9Computer.loadAndExecute(program);

		return stdout;
	}

	public int part2(Resource program)
					throws IOException, InterruptedException, ExecutionException
	{
		day9Computer = new Day9Computer(stdin, stdout);
		day9Computer.readProgramIntoMemory(program);

		Game game = new Game();
		Player player = new Player();
		ExecutorService pool = Executors.newFixedThreadPool(2);

		Future<?> f1 = pool.submit(game);
		Future<?> f2 = pool.submit(player);

		f1.get();
		logger.debug("robot has completed");

		f2.cancel(true);

		return player.score;
	}

	public int draw(LinkedBlockingQueue<String> drawInstructions)
	{
		int screen[][] = new int[40][40];
		Iterator<String> it = drawInstructions.iterator();
		while (it.hasNext())
		{
			int x = Integer.parseInt(it.next());
			int y = Integer.parseInt(it.next());
			int tileId = Integer.parseInt(it.next());
			screen[x][y] = tileId;
		}

		int counter = 0;

		for (int i = 0; i < screen.length; i++)
		{
			for (int j = 0; j < screen[i].length; j++)
			{
				if (screen[i][j] == BLOCK)
					counter++;
			}
		}

		return counter;
	}

	class Game implements Runnable
	{
		private Logger logger = LoggerFactory.getLogger(Game.class);

		@Override
		public void run()
		{
			try
			{
				day9Computer.getMemory().put(0L, 2L);
				day9Computer.runProgram();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	class Player implements Runnable
	{
		private Logger logger = LoggerFactory.getLogger(Player.class);

		private int score = 0;

		@Override
		public void run()
		{
			try
			{
				int screen[][] = new int[20][38];
				int turn = 1;
				AI ai = new AI(17);
				Point ball = null, paddle = null;

				while (true)
				{
					logger.debug("starting turn {}", turn++);
					while (true)
					{
						String output1 = stdout.poll(50, TimeUnit.MILLISECONDS);
						if (output1 == null)
							break;
						int x = Integer.parseInt(output1);
						int y = Integer.parseInt(stdout.take());
						int tileId = Integer.parseInt(stdout.take());
						if (x == -1 && y == 0)
						{
							logger.debug("setting score");
							score = tileId;
						}
						else
						{
							screen[y][x] = tileId;
							if (tileId == BALL)
								ball = new Point(x, y);
							else if (tileId == PADDLE)
								paddle = new Point(x, y);
						}
					}

					logger.debug("finished drawing, score is now {}", score);
					print(screen);

					if (ball != null || paddle != null)
						ai.advance(ball, paddle);

					String joystickInstruction = ai.moveJoystick();
					stdin.offer(joystickInstruction);
				}
			}
			catch (Exception e)
			{
				logger.error("", e);
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		public int getScore()
		{
			return score;
		}

		private void print(int screen[][])
		{
			int lineNum = 0;

			System.out.print(" ");
			for (int i = 0; i < screen[0].length; i++)
				System.out.print(StringUtils.right(String.valueOf(i), 1));
			System.out.println();

			for (int line[] : screen)
			{
				System.out.print(StringUtils.right(String.valueOf(lineNum), 1));

				for (int pixel : line)
				{
					switch (pixel)
					{
						case EMPTY:
							System.out.print(" ");
							break;
						case WALL:
							System.out.print("#");
							break;
						case BLOCK:
							System.out.print("*");
							break;
						case PADDLE:
							System.out.print("-");
							break;
						case BALL:
							System.out.print("o");
							break;
					}
				}
				System.out.println();
				lineNum++;
			}
		}
	}

	static class AI
	{
		private Logger logger = LoggerFactory.getLogger(AI.class);

		private Point ball = null;
		private int vertDirection = 0;
		private int horizDirection = 0;
		private Point paddle = null;
		private int baseline;

		public AI(int baseline)
		{
			this.baseline = baseline;
		}

		public void advance(Point newBallPosition, Point newPaddlePosition)
		{
			if (ball != null)
			{
				if (newBallPosition.y < ball.y)
				{
					vertDirection = 1;
				}
				else if (newBallPosition.y > ball.y)
				{
					vertDirection = -1;
				}
				if (newBallPosition.x > ball.x)
				{
					horizDirection = 1;
				}
				else if (newBallPosition.x < ball.x)
				{
					horizDirection = -1;
				}
			}
			ball = newBallPosition;
			paddle = newPaddlePosition;
		}

		public int calculatePositionAtBaseline()
		{
			int yDiff = baseline - ball.y;
			if (horizDirection == 1)
				return ball.x + yDiff;
			else if (horizDirection == -1)
				return ball.x - yDiff;

			return paddle.x;
		}

		public String moveJoystick()
		{
			if (vertDirection >= 0)
			{
				logger.debug("ball moving up, track it");
				if (ball.x > paddle.x)
					return "1";
				else if (ball.x < paddle.x)
					return "-1";
				else
					return "0";
			}
			else
			{
				int aimToBe = calculatePositionAtBaseline();
				logger.debug("ball moving down, move towards {}", aimToBe);
				if (aimToBe > paddle.x)
					return "1";
				else if (aimToBe < paddle.x)
					return "-1";
				else
					return "0";
			}
		}
	}
}
