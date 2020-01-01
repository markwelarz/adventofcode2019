package advent;

import advent.intcode.Day7Computer;
import org.paukov.combinatorics3.Generator;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Day7Part1
{
	public int maxOutputPart1(Resource program) throws IOException
	{
		int max = Generator.permutation(0, 1, 2, 3, 4)
						.simple().stream()
						.map(v -> chainInputOutputOnce(program, v))
						.mapToInt(v -> v)
						.max()
						.getAsInt();

		return max;
	}

	public int chainInputOutputOnce(Resource program, List<Integer> phaseSettings)
	{
		try
		{
			return chainInputOutputOnce(program, phaseSettings.get(0),
							phaseSettings.get(1), phaseSettings.get(2),
							phaseSettings.get(3), phaseSettings.get(4));
		}
		catch (IOException | InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}

	public int chainInputOutputOnce(Resource program, int phaseA, int phaseB, int phaseC, int phaseD, int phaseE)
					throws IOException, InterruptedException
	{
		String output;

		System.out.println("##### AmpA");
		output = runOnAmplifier(program, phaseA, "0");
		System.out.println("##### AmpB");
		output = runOnAmplifier(program, phaseB, output);
		System.out.println("##### AmpC");
		output = runOnAmplifier(program, phaseC, output);
		System.out.println("##### AmpD");
		output = runOnAmplifier(program, phaseD, output);
		System.out.println("##### AmpE");
		output = runOnAmplifier(program, phaseE, output);

		System.out.println("result is " + output);

		return Integer.parseInt(output);
	}

	private String runOnAmplifier(Resource program, int phaseSetting, String input)
					throws IOException, InterruptedException
	{
		LinkedBlockingQueue<String> stdout = new LinkedBlockingQueue<>();
		LinkedBlockingQueue<String> stdin = new LinkedBlockingQueue<>();
		stdin.add(String.valueOf(phaseSetting));
		stdin.add(input);

		Day7Computer ampA = new Day7Computer(stdin, stdout);
		ampA.loadAndExecute(program);
		String output = stdout.remove();
		return output;
	}
}
