package advent;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day6
{
	private Map<String, String> childToParent = new HashMap<>();

	public int calculate(Resource resource) throws IOException
	{
		Scanner scanner = new Scanner(resource.getInputStream());

		while (scanner.hasNext())
		{
			String line = scanner.next();
			String planets[] = StringUtils.split(line, ")");
			childToParent.put(planets[1], planets[0]);
		}

		int count = 0;
		for (Map.Entry<String, String> e : childToParent.entrySet())
		{
			count += stepsToCOB(e.getKey());
		}

		return count;
	}

	public int calculatePart2(Resource resource) throws IOException
	{
		Scanner scanner = new Scanner(resource.getInputStream());

		while (scanner.hasNext())
		{
			String line = scanner.next();
			String planets[] = StringUtils.split(line, ")");
			childToParent.put(planets[1], planets[0]);
		}

		List<String> sanSteps = stepsToCOB2("SAN");
		List<String> youSteps = stepsToCOB2("YOU");

		System.out.println(sanSteps);
		System.out.println(youSteps);

		int sanI = sanSteps.size() - 1;
		int youI = youSteps.size() - 1;
		boolean same = true;

		while (same)
		{
			sanI--;
			youI--;

			System.out.println(
							"same? " + youSteps.get(youI) + "[" + youI + "] " + sanSteps.get(sanI) + "[" + sanI + "]");

			same = youSteps.get(youI).equals(sanSteps.get(sanI));
		}

		System.out.println("sanI " + sanI);
		System.out.println("youI " + youI);

		return sanI + youI + 2;
	}

	public int stepsToCOB(String from)
	{
		int count = 0;
		String planet = from;
		while (!planet.equals("COM"))
		{
			planet = childToParent.get(planet);
			count++;
		}

		return count;
	}

	public List<String> stepsToCOB2(String from)
	{
		List<String> steps = new ArrayList<>();
		String planet = from;
		while (!planet.equals("COM"))
		{
			planet = childToParent.get(planet);
			steps.add(planet);
		}

		return steps;
	}
}
