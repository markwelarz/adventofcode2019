package advent;

import org.paukov.combinatorics3.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Day7Part2
{
	private static Logger logger = LoggerFactory.getLogger(Day7Part2.class);

	private Day7Computer ampA;
	private Day7Computer ampB;
	private Day7Computer ampC;
	private Day7Computer ampD;
	private Day7Computer ampE;
	private LinkedBlockingQueue<String> eToA = new LinkedBlockingQueue<>();
	private LinkedBlockingQueue<String> aToB = new LinkedBlockingQueue<>();
	private LinkedBlockingQueue<String> bToC = new LinkedBlockingQueue<>();
	private LinkedBlockingQueue<String> cToD = new LinkedBlockingQueue<>();
	private LinkedBlockingQueue<String> dToE = new LinkedBlockingQueue<>();

	public Day7Part2(Resource program) throws IOException
	{
		this.ampA = new Day7Computer(eToA, aToB);
		this.ampA.readProgramIntoMemory(program);
		this.ampB = new Day7Computer(aToB, bToC);
		this.ampB.readProgramIntoMemory(program);
		this.ampC = new Day7Computer(bToC, cToD);
		this.ampC.readProgramIntoMemory(program);
		this.ampD = new Day7Computer(cToD, dToE);
		this.ampD.readProgramIntoMemory(program);
		this.ampE = new Day7Computer(dToE, eToA);
		this.ampE.readProgramIntoMemory(program);
	}

	public static int maxOutputPart2(Resource program)
	{
		int max = Generator.permutation(5, 6, 7, 8, 9)
						.simple().stream()
						.map(v -> chainInputOutput(program, v))
						.mapToInt(v -> v)
						.max()
						.getAsInt();

		return max;
	}

	public static int chainInputOutput(Resource program, List<Integer> phaseSettings)
	{
		try
		{
			Day7Part2 day7Part2 = new Day7Part2(program);
			return day7Part2.chainInputOutput(phaseSettings.get(0),
							phaseSettings.get(1), phaseSettings.get(2),
							phaseSettings.get(3), phaseSettings.get(4));
		}
		catch (InterruptedException | IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public int chainInputOutput(int phaseA, int phaseB, int phaseC, int phaseD, int phaseE)
					throws InterruptedException

	{
		eToA.clear();
		aToB.clear();
		bToC.clear();
		cToD.clear();
		dToE.clear();
		eToA.clear();

		eToA.add(String.valueOf(phaseA));
		eToA.add("0");
		aToB.add(String.valueOf(phaseB));
		bToC.add(String.valueOf(phaseC));
		cToD.add(String.valueOf(phaseD));
		dToE.add(String.valueOf(phaseE));

		ExecutorService es = Executors.newFixedThreadPool(5);

		es.submit(() -> runOnAmplifier(ampA));
		es.submit(() -> runOnAmplifier(ampB));
		es.submit(() -> runOnAmplifier(ampC));
		es.submit(() -> runOnAmplifier(ampD));
		es.submit(() -> runOnAmplifier(ampE));

		es.shutdown();
		es.awaitTermination(5, TimeUnit.HOURS);

		String result = eToA.remove();
		System.out.println("result is " + result);

		return Integer.parseInt(result);
	}

	private void runOnAmplifier(Day7Computer ampA)
	{
		try
		{
			ampA.runProgram();
			logger.debug("runOnAmplifier finished");
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
