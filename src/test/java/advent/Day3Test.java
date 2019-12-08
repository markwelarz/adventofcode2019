package advent;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;

import java.awt.Point;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class Day3Test
{
	@Test
	public void part1Example0() throws Exception
	{
		Day3 day3 = new Day3();
		int answer = day3.calculate(new InputStreamResource(IOUtils.toInputStream("R8,U5,L5,D3\n"
						+ "U7,R6,D4,L4", StandardCharsets.UTF_8)));
		assertThat(answer).isEqualTo(6);
	}

	@Test
	public void part1Example1() throws Exception
	{
		Day3 day3 = new Day3();
		int answer = day3.calculate(new InputStreamResource(IOUtils.toInputStream("R75,D30,R83,U83,L12,D49,R71,U7,L72\n"
						+ "U62,R66,U55,R34,D71,R55,D58,R83", StandardCharsets.UTF_8)));
		assertThat(answer).isEqualTo(159);
	}

	@Test
	public void part1Example2() throws Exception
	{
		Day3 day3 = new Day3();
		int answer = day3.calculate(
						new InputStreamResource(IOUtils.toInputStream("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51\n"
										+ "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7", StandardCharsets.UTF_8)));
		assertThat(answer).isEqualTo(135);
	}

	@Test
	public void part1Input() throws Exception
	{
		Day3 day3 = new Day3();
		int answer = day3.calculate(new ClassPathResource("advent/day3input.txt"));
		assertThat(answer).isEqualTo(375);
	}

	@Test
	public void overlapsSameDirectionHoriz()
	{
		Day3.Line l1 = new Day3.Line(1, 3, 10, 3);
		Day3.Line l2 = new Day3.Line(5, 3, 8, 3);
		assertThat(l1.overlaps(l2)).contains(new Point(5, 3), new Point(6, 3), new Point(7, 3), new Point(8, 3));
		assertThat(l2.overlaps(l1)).contains(new Point(5, 3), new Point(6, 3), new Point(7, 3), new Point(8, 3));
	}

	@Test
	public void overlapsSameDirectionVert()
	{
		Day3.Line l1 = new Day3.Line(3, 1, 3, 10);
		Day3.Line l2 = new Day3.Line(3, 5, 3, 8);
		assertThat(l1.overlaps(l2)).contains(new Point(3, 5), new Point(3, 6), new Point(3, 7), new Point(3, 8));
		assertThat(l2.overlaps(l1)).contains(new Point(3, 5), new Point(3, 6), new Point(3, 7), new Point(3, 8));
	}

	@Test
	public void overlapsCross()
	{
		Day3.Line l1 = new Day3.Line(0, 0, 10, 0);
		Day3.Line l2 = new Day3.Line(5, 0, 5, 10);
		assertThat(l1.overlaps(l2)).contains(new Point(5, 0));
		assertThat(l2.overlaps(l1)).contains(new Point(5, 0));
	}

	@Test
	public void doNotOverlap()
	{
		Day3.Line l1 = new Day3.Line(0, 0, 8, 0);
		Day3.Line l2 = new Day3.Line(6, 3, 6, 7);
		assertThat(l1.overlaps(l2)).isEmpty();
		assertThat(l2.overlaps(l1)).isEmpty();
	}

	@Test
	public void part2Example0() throws Exception
	{
		Day3 day3 = new Day3();
		int answer = day3.calculatePart2(
						new InputStreamResource(IOUtils.toInputStream("R8,U5,L5,D3\n"
										+ "U7,R6,D4,L4", StandardCharsets.UTF_8)));
		assertThat(answer).isEqualTo(30);
	}

	@Test
	public void part2Example1() throws Exception
	{
		Day3 day3 = new Day3();
		int answer = day3.calculatePart2(
						new InputStreamResource(IOUtils.toInputStream("R75,D30,R83,U83,L12,D49,R71,U7,L72\n"
										+ "U62,R66,U55,R34,D71,R55,D58,R83", StandardCharsets.UTF_8)));
		assertThat(answer).isEqualTo(610);
	}

	@Test
	public void part2Example2() throws Exception
	{
		Day3 day3 = new Day3();
		int answer = day3.calculatePart2(
						new InputStreamResource(IOUtils.toInputStream("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51\n"
										+ "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7", StandardCharsets.UTF_8)));
		assertThat(answer).isEqualTo(410);
	}

	@Test
	public void part2Input() throws Exception
	{
		Day3 day3 = new Day3();
		int answer = day3.calculatePart2(new ClassPathResource("advent/day3input.txt"));
		assertThat(answer).isEqualTo(14746);
	}

	@Test
	public void lengthUptoRightLine()
	{
		// ###P#######
		// 01234567890
		Day3.Line line = new Day3.Line(0, 0, 10, 0);
		int length = line.lengthUpTo(new Point(3, 0));
		assertThat(length).isEqualTo(4);
	}

	@Test
	public void lengthUptoLeftLine()
	{
		// ######P####
		// 01234567890
		Day3.Line line = new Day3.Line(10, 0, 0, 0);
		int length = line.lengthUpTo(new Point(6, 0));
		assertThat(length).isEqualTo(5);
	}

	@Test
	public void lengthUptoUpLine()
	{
		// ###P#######
		// 01234567890
		Day3.Line line = new Day3.Line(0, 0, 0, 10);
		int length = line.lengthUpTo(new Point(0, 3));
		assertThat(length).isEqualTo(4);
	}

	@Test
	public void lengthUptoDownLine()
	{
		// ######P####
		// 01234567890
		Day3.Line line = new Day3.Line(0, 10, 0, 0);
		int length = line.lengthUpTo(new Point(0, 6));
		assertThat(length).isEqualTo(5);
	}
}
