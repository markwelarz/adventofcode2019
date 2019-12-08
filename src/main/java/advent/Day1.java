package advent;

import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Day1
{
	public int part1() throws IOException, URISyntaxException
	{
		URI uri = ResourceUtils.getURL("classpath:advent/day1input.txt").toURI();

		int sum = Files.lines(Paths.get(uri))
						.map(Integer::parseInt)
						.collect(Collectors.summingInt(v -> v / 3 - 2));
		return sum;
	}

	public int part2() throws IOException, URISyntaxException
	{
		URI uri = ResourceUtils.getURL("classpath:advent/day1input.txt").toURI();

		int sum = Files.lines(Paths.get(uri))
						.map(Integer::parseInt)
						.collect(Collectors.summingInt(this::calculateFuel));
		return sum;
	}

	public int calculateFuel(int mass)
	{
		int fuelMass = mass / 3 - 2;

		fuelMass = Math.max(fuelMass, 0);

		if (fuelMass > 0)
			fuelMass += calculateFuel(fuelMass);

		return fuelMass;
	}
}
