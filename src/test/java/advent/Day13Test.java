package advent;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.awt.Point;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

public class Day13Test
{
	@Test
	public void part1() throws Exception
	{
		Day13 day13 = new Day13();
		LinkedBlockingQueue<String> output = day13.part1(new ClassPathResource("advent/day13input.txt"));
		int blocks = day13.draw(output);
		assertThat(blocks).isEqualTo(270);
	}

	@Test
	public void part2() throws Exception
	{
		Day13 day13 = new Day13();
		int score = day13.part2(new ClassPathResource("advent/day13input.txt"));
		assertThat(score).isEqualTo(12535);
	}

	@Nested
	class AITests
	{
		/*
		 01234567890
		0    ...
		1   ....
		2....+..
		3    .+.
		4    ..+
		5....#
		 */
		@Test
		public void aiTest1()
		{
			Day13.AI ai = new Day13.AI(4);
			Point paddle = new Point(4, 5);
			ai.advance(new Point(3, 1), paddle);
			ai.advance(new Point(4, 2), paddle);
			int predicatedPosition = ai.calculatePositionAtBaseline();
			assertThat(predicatedPosition).isEqualTo(6);
		}

		/*
		 01234567890
		0   ....
		1  ...+.
		2....+..
		3  .+.
		4..+
		5....#
		 */
		@Test
		public void aiTest2()
		{
			Day13.AI ai = new Day13.AI(4);
			Point paddle = new Point(4, 5);
			ai.advance(new Point(5, 1), paddle);
			ai.advance(new Point(4, 2), paddle);
			int predicatedPosition = ai.calculatePositionAtBaseline();
			assertThat(predicatedPosition).isEqualTo(2);
		}
	}
}
