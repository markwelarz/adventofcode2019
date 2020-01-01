package advent;

import advent.intcode.Day23Computer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Day23
{
	private static Logger logger = LoggerFactory.getLogger(Day23.class);

	private Resource program;
	private List<NetworkNode> computers = new ArrayList<>();
	private ExecutorService executorService;
	private LinkedBlockingQueue<String> allOutput = new LinkedBlockingQueue<>();
	// only needed for writing
	private Lock writeOutputQueueMutex = new ReentrantLock();
	long lastNatYDelivered = 0;
	int natDeliveredCount = 0;
	long natX = -99, natY = -99;

	public Day23(Resource program, int numberOf)
	{
		this.program = program;
		for (int i = 0; i < numberOf; i++)
		{
			LinkedBlockingQueue<String> in = new LinkedBlockingQueue<>();
			PacketReadIO io = new PacketReadIO(in, allOutput);
			computers.add(new NetworkNode(new Day23Computer(io), io, i));
		}

		executorService = Executors.newFixedThreadPool(numberOf);
	}

	public long part1() throws InterruptedException
	{
		computers.forEach(NetworkNode::start);
		boolean done = false;

		while (!done)
		{
			int computer = Integer.parseInt(allOutput.take());
			long x = Long.parseLong(allOutput.take());
			long y = Long.parseLong(allOutput.take());

			if (computer >= 0 && computer < 50)
			{
				logger.debug("sending ({},{}) to {}", x, y, computer);

				NetworkNode node = computers.get(computer);
				node.sendPacket(x, y);
			}
			else
			{
				logger.error("cannot send ({},{}) to {}", x, y, computer);
				executorService.shutdownNow();
				done = true;
				return y;
			}
		}

		return -99;
	}

	public long part2() throws InterruptedException
	{
		computers.forEach(NetworkNode::start);
		boolean done = false;

		while (!done)
		{
			String computerString = allOutput.poll(600, TimeUnit.MILLISECONDS);
			if (computerString == null)
			{
				if (detectIdleness().isPresent())
					return detectIdleness().get();
				continue;
			}
			int computer = Integer.parseInt(computerString);
			long x = Long.parseLong(allOutput.take());
			long y = Long.parseLong(allOutput.take());

			if (computer >= 0 && computer < 50)
			{
				logger.debug("sending ({},{}) to {}", x, y, computer);

				NetworkNode node = computers.get(computer);
				node.sendPacket(x, y);
			}
			else if (computer == 255)
			{
				logger.debug("NAT ({},{}) to {}", x, y, computer);

				natX = x;
				natY = y;
			}
			else
			{
				logger.error("cannot send ({},{}) to {}", x, y, computer);
				executorService.shutdownNow();
				done = true;
				return y;
			}
		}

		return -99;
	}

	private Optional<Long> detectIdleness()
	{
		if (computers.stream().allMatch(v -> v.isIdle()))
		{
			logger.debug("NAT detected idle network");

			computers.get(0).sendPacket(natX, natY);
			natDeliveredCount++;

			if (natDeliveredCount >= 2 && lastNatYDelivered == natY)
			{
				logger.debug("NAT delivered twice in a row {}", natY);
				return Optional.of(natY);
			}

			lastNatYDelivered = natY;
		}
		else
			logger.debug("Not idle enough");

		return Optional.empty();
	}

	class PacketReadIO implements Day23Computer.IO
	{
		private Queue<String> stdout;
		private Queue<String> stdin;
		private Queue<String> buffer = new LinkedList<>();
		private AtomicInteger emptyReads = new AtomicInteger();

		public PacketReadIO(Queue<String> stdin, Queue<String> stdout)
		{
			this.stdin = stdin;
			this.stdout = stdout;
		}

		@Override
		public synchronized String read()
		{
			String top = stdin.poll();
			if (top == null)
			{
				emptyReads.incrementAndGet();
				return "-1";
			}
			else
			{
				emptyReads.set(0);
				return top;
			}
		}

		@Override
		public void write(String value)
		{
			try
			{
				writeOutputQueueMutex.lockInterruptibly();
				buffer.add(value);
				if (buffer.size() == 3)
				{
					stdout.add(buffer.remove());
					stdout.add(buffer.remove());
					stdout.add(buffer.remove());
				}
			}
			catch (InterruptedException e)
			{
				logger.error("", e);
				throw new RuntimeException(e);
			}
			finally
			{
				writeOutputQueueMutex.unlock();
			}
		}

		public synchronized void sendToInput(String value)
		{
			stdin.add(value);
		}

		public synchronized void sendToInput(String value1, String value2)
		{
			stdin.add(value1);
			stdin.add(value2);
		}

		public int getEmptyReads()
		{
			return emptyReads.get();
		}
	}

	class NetworkNode implements Runnable
	{
		private Day23Computer computer;
		private PacketReadIO io;
		private int address;

		public NetworkNode(Day23Computer computer, PacketReadIO io, int address)
		{
			this.computer = computer;
			this.io = io;
			this.address = address;
		}

		public void start()
		{
			io.sendToInput(String.valueOf(address));
			executorService.submit(this);
		}

		@Override
		public void run()
		{
			try
			{
				computer.loadAndExecute(program);
			}
			catch (IOException | InterruptedException e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		public void sendPacket(long x, long y)
		{
			io.sendToInput(String.valueOf(x), String.valueOf(y));
		}

		public boolean isIdle()
		{
			int emptyReads = io.getEmptyReads();
			logger.debug("computer {} has been idle for {} reads", address, emptyReads);
			return emptyReads > 5000;
		}
	}
}
