package advent;

import com.google.common.base.Splitter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NavigableMap;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Day9Computer
{
	private static Logger logger = LoggerFactory.getLogger(Day9Computer.class);

	private NavigableMap<Long, Long> memory = new TreeMap<>();
	private LinkedBlockingQueue<String> stdout;
	private LinkedBlockingQueue<String> stdin;
	private long instructionCounter = 0;
	private long relativeBase = 0;

	public Day9Computer(LinkedBlockingQueue<String> stdin, LinkedBlockingQueue<String> stdout)
	{
		this.stdin = stdin;
		this.stdout = stdout;
	}

	public void loadAndExecute(Resource input) throws IOException, InterruptedException
	{
		readProgramIntoMemory(input);
		runProgram();
	}

	public void readProgramIntoMemory(Resource input) throws IOException
	{
		String inputString = IOUtils.toString(input.getInputStream(), StandardCharsets.UTF_8);
		List<Long> program = Splitter.on(",").splitToList(inputString)
						.stream().map(Long::parseLong)
						.collect(Collectors.toList());

		for (int i = 0; i < program.size(); i++)
		{
			this.memory.put((long) i, program.get(i));
		}

		//		outputProgram();
	}

	public void outputProgram()
	{
		int pc = 0;
		while (pc < memory.size())
		{
			Instruction instruction = readInstruction();
			System.out.print("[" + pc + "] " + "[" + instruction.name() + "] ");
			for (long i = 0; i < instruction.size(); i++)
			{
				System.out.print(memory.get(i + pc) + ",");
			}
			pc += instruction.size();
			instructionCounter += instruction.size();
			System.out.println();
		}
	}

	public void runProgram() throws InterruptedException
	{
		try
		{
			while (instructionCounter < memory.size())
			{
				Instruction instruction = readInstruction();
				instruction.execute(memory);
			}
		}
		catch (HaltException he)
		{
		}
	}

	private long getParamValue(char instructionMode, long paramNumber)
	{
		long paramValue = readValueAt(instructionCounter + paramNumber);
		if (instructionMode == '0')
		{
			paramValue = memory.getOrDefault(paramValue, 0L);
		}
		else if (instructionMode == '2')
		{
			paramValue = memory.getOrDefault(relativeBase + paramValue, 0L);
		}
		return paramValue;
	}

	private long getParamValue2(char instructionMode, long paramNumber)
	{
		long paramValue = readValueAt(instructionCounter + paramNumber);
		if (instructionMode == '2')
		{
			paramValue = memory.getOrDefault(relativeBase + paramValue, 0L);
		}
		return paramValue;
	}

	private Supplier<Long> paramSupplier(char instructionMode, int paramNumber)
	{
		return () -> getParamValue(instructionMode, paramNumber);
	}

	private Supplier<Long> paramSupplier2(char instructionMode, int paramNumber)
	{
		return () -> getParamValue2(instructionMode, paramNumber);
	}

	public Instruction readInstruction()
	{
		long opcode = memory.get(instructionCounter);
		String opcodepadded = StringUtils.leftPad(String.valueOf(opcode), 5, '0');

		Supplier<Long> param1Mode = paramSupplier(opcodepadded.charAt(2), 1);
		Supplier<Long> param2Mode = paramSupplier(opcodepadded.charAt(1), 2);
		Supplier<Long> param3Mode = paramSupplier(opcodepadded.charAt(0), 3);

		Supplier<Long> param1Immediate = paramSupplier2(opcodepadded.charAt(2), 1);
		Supplier<Long> param2Immediate = paramSupplier2(opcodepadded.charAt(1), 2);
		Supplier<Long> param3Immediate = paramSupplier2(opcodepadded.charAt(0), 3);

		switch (opcodepadded.substring(3))
		{
			case "01":
				return new AddInstruction(param1Mode, param2Mode, param3Immediate);
			case "02":
				return new MultInstruction(param1Mode, param2Mode, param3Immediate);
			case "99":
				return new HaltInstruction();
			case "03":
				return new InputInstruction(param1Immediate);
			case "04":
				return new OutputInstruction(param1Mode);
			case "05":
				return new JumpIfTrueInstruction(param1Mode, param2Mode);
			case "06":
				return new JumpIfFalseInstruction(param1Mode, param2Mode);
			case "07":
				return new LessThanInstruction(param1Mode, param2Mode, param3Immediate);
			case "08":
				return new EqualInstruction(param1Mode, param2Mode, param3Immediate);
			case "09":
				return new AdjustRelativeBase(param1Mode);
			default:
				throw new IllegalStateException();
		}
	}

	long readValueAt(long location)
	{
		return memory.getOrDefault(location, 0L);
	}

	public NavigableMap<Long, Long> getMemory()
	{
		return this.memory;
	}

	public long getInstructionCounter()
	{
		return instructionCounter;
	}

	public long getRelativeBase()
	{
		return relativeBase;
	}

	public void setRelativeBase(long relativeBase)
	{
		this.relativeBase = relativeBase;
	}

	interface Instruction
	{
		void execute(NavigableMap<Long, Long> memory) throws InterruptedException;

		int size();

		String name();
	}

	abstract class BiArgumentsWithResultInstruction implements Instruction
	{
		private Supplier<Long> param1Value;
		private Supplier<Long> param2Value;
		private Supplier<Long> writeLocationParam;

		public BiArgumentsWithResultInstruction(Supplier<Long> param1Value,
						Supplier<Long> param2Value,
						Supplier<Long> writeLocationParam)
		{
			this.param1Value = param1Value;
			this.param2Value = param2Value;
			this.writeLocationParam = writeLocationParam;
		}

		@Override
		public void execute(NavigableMap<Long, Long> memory)
		{
			long param1 = param1Value.get();
			long param2 = param2Value.get();
			long writeLocation = writeLocationParam.get();
			long result = doOperation(param1, param2);
			memory.put(writeLocation, result);
			logger.debug(" [addmul] [pc={}] writeLocation=" + writeLocation
							+ ",param1=" + param1 + ",param2=" + param2
							+ ",result=" + result, instructionCounter);

			instructionCounter += 4;
		}

		@Override
		public int size()
		{
			return 4;
		}

		public abstract long doOperation(long param1, long param2);
	}

	class AddInstruction extends BiArgumentsWithResultInstruction
	{
		public AddInstruction(Supplier<Long> param1Value,
						Supplier<Long> param2Value, Supplier<Long> writeLocationParam)
		{
			super(param1Value, param2Value, writeLocationParam);
		}

		@Override
		public long doOperation(long param1, long param2)
		{
			return param1 + param2;
		}

		@Override
		public String name()
		{
			return "add";
		}
	}

	class MultInstruction extends BiArgumentsWithResultInstruction
	{
		public MultInstruction(Supplier<Long> param1Value,
						Supplier<Long> param2Value, Supplier<Long> writeLocationParam)
		{
			super(param1Value, param2Value, writeLocationParam);
		}

		@Override
		public long doOperation(long param1, long param2)
		{
			return param1 * param2;
		}

		@Override
		public String name()
		{
			return "mul";
		}
	}

	class InputInstruction implements Instruction
	{
		private Supplier<Long> writeLocationParam;

		public InputInstruction(Supplier<Long> writeLocationParam)
		{
			this.writeLocationParam = writeLocationParam;
		}

		@Override
		public void execute(NavigableMap<Long, Long> memory) throws InterruptedException
		{
			String inputEvent = stdin.take();
			Scanner scanner = new Scanner(new StringReader(inputEvent));
			long value = scanner.nextInt();
			long writeLocation = writeLocationParam.get();
			memory.put(writeLocation, value);
			logger.debug("[input] [pc={}] writeLocation=" + writeLocation + ", value=" + value, instructionCounter);

			instructionCounter += 2;
		}

		@Override
		public int size()
		{
			return 2;
		}

		@Override
		public String name()
		{
			return "inp";
		}
	}

	class OutputInstruction implements Instruction
	{
		private Supplier<Long> param1Value;

		public OutputInstruction(Supplier<Long> param1Value)
		{
			this.param1Value = param1Value;
		}

		@Override
		public void execute(NavigableMap<Long, Long> memory)
		{
			long param1 = param1Value.get();
			logger.debug("[output] [pc={}] value=" + param1, instructionCounter);
			stdout.offer(String.valueOf(param1));
			instructionCounter += 2;
		}

		@Override
		public int size()
		{
			return 2;
		}

		@Override
		public String name()
		{
			return "out";
		}
	}

	private class AdjustRelativeBase implements Instruction
	{
		private Supplier<Long> param1Value;

		public AdjustRelativeBase(Supplier<Long> param1Value)
		{
			this.param1Value = param1Value;
		}

		@Override
		public void execute(NavigableMap<Long, Long> memory)
		{
			long param1 = param1Value.get();
			logger.debug("[adjust relative-base] [pc={}] by=" + param1 + ", newValue=" + (param1 + relativeBase),
							instructionCounter);
			relativeBase += param1;
			instructionCounter += 2;
		}

		@Override
		public int size()
		{
			return 2;
		}

		@Override
		public String name()
		{
			return "arb";
		}
	}

	class HaltInstruction implements Instruction
	{
		@Override
		public void execute(NavigableMap<Long, Long> memory)
		{
			logger.debug("[halt] [pc={}]", instructionCounter);
			throw new HaltException();
		}

		@Override
		public int size()
		{
			return 1;
		}

		@Override
		public String name()
		{
			return "hal";
		}
	}

	abstract class AbstractJumpInstruction2 implements Instruction
	{
		private Supplier<Long> param1Value;
		private Supplier<Long> param2Value;

		public AbstractJumpInstruction2(Supplier<Long> param1Value, Supplier<Long> param2Value)
		{
			this.param1Value = param1Value;
			this.param2Value = param2Value;
		}

		@Override
		public void execute(NavigableMap<Long, Long> memory)
		{
			long param1 = param1Value.get();
			long param2 = param2Value.get();
			boolean jump = check(param1, param2);

			logger.debug("[" + instructionName() + "] [pc={}] param1=" + param1 + ",param2="
							+ param2 + ",jump=" + jump, instructionCounter);

			if (jump)
				instructionCounter = param2;
			else
				instructionCounter += 3;
		}

		public abstract boolean check(long param1Value, long param2Value);

		public abstract String instructionName();

		@Override
		public int size()
		{
			return 3;
		}
	}

	public class JumpIfTrueInstruction extends AbstractJumpInstruction2
	{
		public JumpIfTrueInstruction(Supplier<Long> param1Value, Supplier<Long> param2Value)
		{
			super(param1Value, param2Value);
		}

		@Override
		public boolean check(long param1Value, long param2Value)
		{
			return param1Value != 0;
		}

		@Override
		public String instructionName()
		{
			return "jit";
		}

		@Override
		public String name()
		{
			return "jit";
		}
	}

	public class JumpIfFalseInstruction extends AbstractJumpInstruction2
	{
		public JumpIfFalseInstruction(Supplier<Long> param1Value,
						Supplier<Long> param2Value)
		{
			super(param1Value, param2Value);
		}

		@Override
		public boolean check(long param1Value, long param2Value)
		{
			return param1Value == 0;
		}

		@Override
		public String instructionName()
		{
			return "jif";
		}

		@Override
		public String name()
		{
			return "jif";
		}
	}

	abstract class AbstractCompInstruction3 implements Instruction
	{
		private Supplier<Long> param1Value;
		private Supplier<Long> param2Value;
		private Supplier<Long> writeLocationParam;

		public AbstractCompInstruction3(Supplier<Long> param1Value,
						Supplier<Long> param2Value, Supplier<Long> writeLocationParam)
		{
			this.param1Value = param1Value;
			this.param2Value = param2Value;
			this.writeLocationParam = writeLocationParam;
		}

		@Override
		public void execute(NavigableMap<Long, Long> memory)
		{
			long param1 = param1Value.get();
			long param2 = param2Value.get();
			long writeLocation = writeLocationParam.get();
			boolean store = check(param1, param2);

			logger.debug("[comp?] [pc={}] param1=" + param1
							+ ",param2=" + param2 + ",param3=" + writeLocation + ",store=" + store, instructionCounter);

			if (store)
			{
				memory.put(writeLocation, 1L);
			}
			else
			{
				memory.put(writeLocation, 0L);
			}

			instructionCounter += 4;
		}

		public abstract boolean check(long param1Value, long param2Value);

		@Override
		public int size()
		{
			return 4;
		}
	}

	class LessThanInstruction extends AbstractCompInstruction3
	{
		public LessThanInstruction(Supplier<Long> param1Value, Supplier<Long> param2Value,
						Supplier<Long> param3Value)
		{
			super(param1Value, param2Value, param3Value);
		}

		@Override
		public boolean check(long param1Value, long param2Value)
		{
			return param1Value < param2Value;
		}

		@Override
		public String name()
		{
			return "lt";
		}
	}

	class EqualInstruction extends AbstractCompInstruction3
	{
		public EqualInstruction(Supplier<Long> param1Value, Supplier<Long> param2Value,
						Supplier<Long> param3Value)
		{
			super(param1Value, param2Value, param3Value);
		}

		@Override
		public boolean check(long param1Value, long param2Value)
		{
			return param1Value == param2Value;
		}

		@Override
		public String name()
		{
			return "eq";
		}
	}

	class HaltException extends RuntimeException
	{

	}
}
