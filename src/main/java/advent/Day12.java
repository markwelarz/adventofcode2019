package advent;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.paukov.combinatorics3.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day12
{
	private Logger logger = LoggerFactory.getLogger(Day12.class);

	private List<Moon> moons;

	public void readInput(String input)
	{
		moons = parse(input);
	}

	public int doSteps(int steps)
	{
		for (int i = 1; i <= steps; i++)
		{
			Generator.combination(moons).simple(2)
							.stream()
							.forEach(v -> applyGravity(v.get(0), v.get(1)));
			moons.forEach(v -> applyVelocity(v));

			//			logger.debug("step\t{}\t{}\t{}", i, moons, calculateTotalEnergy());
		}

		int totalEnergy = calculateTotalEnergy();
		logger.debug("total energy is {}", totalEnergy);

		return totalEnergy;
	}

	public List<List<Moon>> collectSteps(int steps)
	{
		List<List<Moon>> moonsPerStep = new ArrayList<>();

		for (int i = 1; i <= steps; i++)
		{
			Generator.combination(moons).simple(2)
							.stream()
							.forEach(v -> applyGravity(v.get(0), v.get(1)));
			moons.forEach(v -> applyVelocity(v));

			//logger.debug("step\t{}\t{}\t{}", i, moons, calculateTotalEnergy());

			List<Moon> ms = moons.stream().map(v -> new Moon(v)).collect(Collectors.toList());

			moonsPerStep.add(ms);
		}

		return moonsPerStep;
	}

	public int calculateTotalEnergy()
	{
		int totalEnergy = moons.stream()
						.map(v -> (Math.abs(v.x) + Math.abs(+v.y) + Math.abs(v.z)) * (Math.abs(v.velocityX)
										+ Math.abs(v.velocityY) + Math.abs(v.velocityZ)))
						.collect(Collectors.summingInt(v -> v));

		return totalEnergy;
	}

	public void applyVelocity(Moon a)
	{
		a.move();
	}

	public void applyGravity(Moon a, Moon b)
	{
		//		logger.debug("moon {} and {}", a, b);

		if (a.x > b.x)
		{
			a.velocityX--;
			b.velocityX++;
		}
		else if (a.x < b.x)
		{
			a.velocityX++;
			b.velocityX--;
		}
		if (a.y > b.y)
		{
			a.velocityY--;
			b.velocityY++;
		}
		else if (a.y < b.y)
		{
			a.velocityY++;
			b.velocityY--;
		}
		if (a.z > b.z)
		{
			a.velocityZ--;
			b.velocityZ++;
		}
		else if (a.z < b.z)
		{
			a.velocityZ++;
			b.velocityZ--;
		}

		//		logger.debug("velocity of {} is {},{},{}", a, a.velocityX, a.velocityY, a.velocityZ);
		//		logger.debug("velocity of {} is {},{},{}", b, b.velocityX, b.velocityY, b.velocityZ);
	}

	public List<Moon> parse(String input)
	{
		List<Moon> moons = new ArrayList<>();
		Scanner sc = new Scanner(input);
		while (sc.hasNext())
		{
			String coordx = StringUtils.substringBetween(sc.next(), "<x=", ",");
			String coordy = StringUtils.substringBetween(sc.next(), "=", ",");
			String coordz = StringUtils.substringBetween(sc.next(), "=", ">");

			moons.add(new Moon(Integer.parseInt(coordx),
							Integer.parseInt(coordy),
							Integer.parseInt(coordz)));
		}

		return moons;
	}

	public List<Moon> getMoons()
	{
		return moons;
	}

	public RepeatingSequence repeatingForMoon(List<List<Moon>> statePerStep, String moon, int skip)
	{
		int xLength = this.repeatingPattern(statePerStep, v -> v.x, skip, 1);
		int yLength = this.repeatingPattern(statePerStep, v -> v.y, skip, 1);
		int zLength = this.repeatingPattern(statePerStep, v -> v.z, skip, 1);
		int velocityXLength = this.repeatingPattern(statePerStep, v -> v.velocityX, skip, 1);
		int velocityYLength = this.repeatingPattern(statePerStep, v -> v.velocityY, skip, 1);
		int velocityZLength = this.repeatingPattern(statePerStep, v -> v.velocityZ, skip, 1);

		logger.debug("moon {} repeating x={}, y={}, z={}, vx={}, vy={}, vz={}", moon, xLength, yLength, zLength,
						velocityXLength, velocityYLength, velocityZLength);

		return new RepeatingSequence(xLength, yLength, zLength);
	}

	static class RepeatingSequence
	{
		long x, y, z;

		public RepeatingSequence(long x, long y, long z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public List<Long> asList()
		{
			return List.of(x, y, z);
		}
	}

	public long stepsToRepeat()
	{
		List<List<Moon>> statePerStep = collectSteps(1000000); // a big number(?!)
		List<Long> aMoonSeqs = repeatingForMoon(statePerStep, "A", 0).asList();
		List<Long> bMoonSeqs = repeatingForMoon(statePerStep, "B", 1).asList();
		List<Long> cMoonSeqs = repeatingForMoon(statePerStep, "C", 2).asList();
		List<Long> dMoonSeqs = repeatingForMoon(statePerStep, "D", 3).asList();

		long steps = Streams.concat(aMoonSeqs.stream(), bMoonSeqs.stream(), cMoonSeqs.stream(), dMoonSeqs.stream())
						.collect(Collectors.reducing((a, b) -> ArithmeticUtils.lcm(a, b))).get();

		return steps;
	}

	public int repeatingPattern(List<List<Moon>> statePerStep, Function<Moon, Integer> field, int skip, int limit)
	{
		List<Integer> xOfMoonA = statePerStep.stream()
						.flatMap(v -> v.stream().skip(skip).limit(limit))
						.map(field)
						.collect(Collectors.toList());

		boolean matches = false;
		int i = 0;
		while (i < xOfMoonA.size() && !matches)
		{
			i++;
			List<List<Integer>> partioned = Lists.partition(xOfMoonA, i);
			matches = allListsEqual(partioned);
		}

		return i;
	}

	boolean allListsEqual(List<List<Integer>> partitioned)
	{
		List<Integer> sizes = partitioned.stream().map(v -> v.size()).collect(Collectors.toList());
		int equalSize;
		if (sizes.get(0) != sizes.get(sizes.size() - 1))
			equalSize = partitioned.size() - 1;
		else
			equalSize = partitioned.size();

		boolean eq = true;
		for (int i = 1; i < equalSize; i++)
		{
			if (partitioned.get(i - 1).equals(partitioned.get(i)))
				eq = eq && true;
			else
				eq = false;
		}

		return eq;
	}

}

class Moon
{
	int velocityX;
	int velocityY;
	int velocityZ;
	int x, y, z;

	public Moon(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.velocityX = 0;
		this.velocityY = 0;
		this.velocityZ = 0;
	}

	public Moon(Moon other)
	{
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		this.velocityX = other.velocityX;
		this.velocityY = other.velocityY;
		this.velocityZ = other.velocityZ;
	}

	public void move()
	{
		x += velocityX;
		y += velocityY;
		z += velocityZ;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
						.append("velocityX", velocityX)
						.append("velocityY", velocityY)
						.append("velocityZ", velocityZ)
						.append("x", x)
						.append("y", y)
						.append("z", z)
						.toString();
	}
}

