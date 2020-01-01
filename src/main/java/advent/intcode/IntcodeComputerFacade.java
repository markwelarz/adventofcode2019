package advent.intcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class IntcodeComputerFacade
{
	private Logger logger = LoggerFactory.getLogger(IntcodeComputerFacade.class);

	private LinkedBlockingQueue<String> stdin = new LinkedBlockingQueue<>();
	private LinkedBlockingQueue<String> stdout = new LinkedBlockingQueue<>();
	private Day9Computer day9Computer;
	private List<IntcodeComputerListener> listeners = new ArrayList<>();
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private Future<?> runningProgram;
	private int timeoutSeconds;

	public IntcodeComputerFacade(Resource program, int timeoutSeconds) throws IOException
	{
		this.day9Computer = new Day9Computer(stdin, stdout);
		this.day9Computer.readProgramIntoMemory(program);
		this.timeoutSeconds = timeoutSeconds;
	}

	public void addListener(IntcodeComputerListener listener)
	{
		listeners.add(listener);
	}

	public void run() throws InterruptedException
	{
		runningProgram = executor.submit(() -> {
			try
			{
				day9Computer.runProgram();
			}
			catch (Exception e)
			{
				logger.error("program failed", e);
				throw new RuntimeException(e);
			}
		});

		while (!runningProgram.isDone())
		{
			String outputFromComputer = stdout.poll(timeoutSeconds, TimeUnit.SECONDS);
			if (outputFromComputer == null)
			{
				logger.debug("intcomputer has timedout");
				break;
			}
			listeners.forEach(v -> v.onMessage(outputFromComputer));
		}

		stdout.stream().forEach(v -> {
			listeners.forEach(v1 -> v1.onMessage(v));
		});

		listeners.forEach(IntcodeComputerListener::finished);
	}

	public void setMemory(long address, long value)
	{
		day9Computer.getMemory().put(address, value);
	}

	public void send(long data)
	{
		stdin.offer(String.valueOf(data));
	}
}

