package advent;

import com.codepoetics.protonpack.StreamUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.springframework.core.io.Resource;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day3
{
	public int calculate(Resource resource) throws IOException
	{
		Scanner in = new Scanner(resource.getInputStream());

		String line1 = in.next();
		List<Instruction> instructionsPath1 = createInstructions(line1);
		List<Line> lines1 = createLines(instructionsPath1, true);

		String line2 = in.next();
		List<Instruction> instructionsPath2 = createInstructions(line2);
		List<Line> lines2 = createLines(instructionsPath2, true);

		Point centralPoint = new Point(lines1.get(0).x1, lines1.get(0).y1);
		List<Point> overlappingPoints = calculateOverlaps(lines1, lines2);

		System.out.println("overlapping points: " + overlappingPoints);

		int minDistance = overlappingPoints.stream()
						.filter(v -> !v.equals(centralPoint))
						.map(v -> manhattenDistance(v, centralPoint))
						.min(Integer::compare)
						.get();

		return minDistance;
	}

	public int calculatePart2(Resource resource) throws IOException
	{
		Scanner in = new Scanner(resource.getInputStream());

		String line1 = in.next();
		List<Instruction> instructionsPath1 = createInstructions(line1);
		List<Line> lines1 = createLines(instructionsPath1, false);

		String line2 = in.next();
		List<Instruction> instructionsPath2 = createInstructions(line2);
		List<Line> lines2 = createLines(instructionsPath2, false);

		List<Point> overlappingPoints = calculateOverlaps(lines1, lines2);

		int steps = overlappingPoints.stream()
						.filter(v -> !v.equals(new Point(0, 0)))
						.map(v -> sumSteps(lines1, lines2, v))
						.min(Integer::compare)
						.get();

		return steps;
	}

	private int sumSteps(List<Line> lines1, List<Line> lines2, Point point)
	{
		int line1Steps = countStepsToPoint(lines1, point);
		int line2Steps = countStepsToPoint(lines2, point);

		System.out.println("sumSteps " + point + ", " + line1Steps + " --- " + line2Steps);

		return line1Steps + line2Steps;
	}

	private int countStepsToPoint(List<Line> lines, Point p)
	{
		List after1 = StreamUtils.takeUntilInclusive(lines.stream(), v -> v.contains(p))
						.collect(Collectors.toList());

		List after2 = StreamUtils.takeUntilInclusive(lines.stream(), v -> v.contains(p))
						.map(v -> stepsTaken(v, p))
						.collect(Collectors.toList());

		int steps = StreamUtils.takeUntilInclusive(lines.stream(), v -> v.contains(p))
						.map(v -> stepsTaken(v, p))
						.collect(Collectors.summingInt(v -> v));

		return steps;
	}

	private int stepsTaken(Line line, Point p)
	{
		if (line.contains(p))
		{
			return line.lengthUpTo(p) - 1;
		}
		else
		{
			return line.length() - 1;
		}
	}

	private int manhattenDistance(Point p1, Point p2)
	{
		return Math.abs(p2.y - p1.y) + Math.abs(p2.x - p1.x);
	}

	private List<Point> calculateOverlaps(List<Line> lines1, List<Line> lines2)
	{
		List<Point> overlappingPoints = new ArrayList<>();

		for (Line l1 : lines1)
		{
			for (Line l2 : lines2)
			{
				overlappingPoints.addAll(l1.overlaps(l2));
			}
		}

		return overlappingPoints;
	}

	private List<Line> createLines(List<Instruction> instructionsPath, boolean normalize)
	{
		List<Line> lines = new ArrayList<>();
		int currentX = 0, currentY = 0;
		int nextX = 0, nextY = 0;

		for (Instruction ins : instructionsPath)
		{
			switch (ins.direction)
			{
				case "L":
					nextX -= ins.amount;
					break;
				case "R":
					nextX += ins.amount;
					break;
				case "U":
					nextY += ins.amount;
					break;
				case "D":
					nextY -= ins.amount;
					break;
			}

			if (normalize)
			{
				lines.add(createNormalizedLine(currentX, currentY, nextX, nextY));
			}
			else
			{
				lines.add(createDirectionalLine(currentX, currentY, nextX, nextY));
			}

			currentX = nextX;
			currentY = nextY;
		}

		return lines;
	}

	private Line createNormalizedLine(int x1, int y1, int x2, int y2)
	{
		return new Line(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2));
	}

	private Line createDirectionalLine(int x1, int y1, int x2, int y2)
	{
		return new Line(x1, y1, x2, y2);
	}

	private List<Instruction> createInstructions(String linesString)
	{
		List<Instruction> lines;

		lines = Splitter.on(',')
						.splitToList(linesString).stream()
						.map(v -> new Instruction(v.substring(0, 1), Integer.parseInt(v.substring(1))))
						.collect(Collectors.toList());

		return lines;
	}

	static class Line
	{
		int x1, y1;
		int x2, y2;

		public Line(int x1, int y1, int x2, int y2)
		{
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}

		public boolean isHoriz()
		{
			return y1 == y2;
		}

		public boolean isVert()
		{
			return x1 == x2;
		}

		public boolean contains(Point p)
		{
			if (this.isHoriz())
			{
				return p.y == y1 && p.x >= Math.min(x1, x2) && p.x <= Math.max(x1, x2);
			}
			else
			{
				return p.x == x1 && p.y >= Math.min(y1, y2) && p.y <= Math.max(y1, y2);
			}
		}

		public List<Point> overlaps(Line l2)
		{
			if (this.isHoriz() && l2.isHoriz())
			{
				if (y1 == l2.y1)
				{
					return IntStream.rangeClosed(Math.max(x1, l2.x1), Math.min(x2, l2.x2))
									.boxed()
									.map(v -> new Point(v, y1))
									.collect(Collectors.toList());
				}
			}
			else if (this.isVert() && l2.isVert())
			{
				if (x1 == l2.x1)
				{
					return IntStream.rangeClosed(Math.max(y1, l2.y1), Math.min(y2, l2.y2))
									.boxed()
									.map(v -> new Point(x1, v))
									.collect(Collectors.toList());
				}
			}
			else if (this.isVert() && l2.isHoriz())
			{
				int horizY = l2.y1;
				int vertX = x1;

				if (horizY >= Math.min(y1, y2) && horizY <= Math.max(y1, y2))
				{
					if (vertX >= Math.min(l2.x1, l2.x2) && vertX <= Math.max(l2.x1, l2.x2))
					{
						return Lists.newArrayList(new Point(vertX, horizY));
					}
				}
			}
			else if (this.isHoriz() && l2.isVert())
			{
				int vertX = l2.x1;
				int horizY = y1;

				if (vertX >= Math.min(x1, x2) && vertX <= Math.max(x1, x2))
				{
					if (horizY >= Math.min(l2.y1, l2.y2) && horizY <= Math.max(l2.y1, l2.y2))
					{
						return Lists.newArrayList(new Point(vertX, horizY));
					}
				}

			}
			return Collections.emptyList();
		}

		public int length()
		{
			if (isVert())
			{
				return Math.abs(y2 - y1) + 1;
			}
			else
			{
				return Math.abs(x2 - x1) + 1;
			}
		}

		public int lengthUpTo(Point p)
		{
			if (this.isHoriz())
			{
				if (x2 < x1)
				{
					return x1 - p.x + 1;
				}
				else
				{
					//ok
					return p.x - x1 + 1;
				}
			}
			else
			{
				if (y2 < y1)
				{
					return y1 - p.y + 1;
				}
				else
				{
					// ok
					return p.y - y1 + 1;
				}
			}
		}
	}

	class Instruction
	{
		String direction;
		int amount;

		public Instruction(String direction, int amount)
		{
			this.direction = direction;
			this.amount = amount;
		}
	}
}
