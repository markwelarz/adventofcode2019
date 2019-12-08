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
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Day7Computer
{
	private static Logger logger = LoggerFactory.getLogger(Day7Computer.class);

	private List<Integer> memory;
	private LinkedBlockingQueue<String> stdout;
	private LinkedBlockingQueue<String> stdin;
	private int instructionCounter = 0;

	public Day7Computer(LinkedBlockingQueue<String> stdin, LinkedBlockingQueue<String> stdout)
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
		this.memory = Splitter.on(",").splitToList(inputString)
						.stream().map(Integer::parseInt)
						.collect(Collectors.toList());
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

	private int getParamValue(char instructionMode, int paramNumber)
	{
		int paramValue = readValueAt(instructionCounter + paramNumber);
		if (instructionMode == '0')
		{
			paramValue = memory.get(paramValue);
		}
		return paramValue;
	}

	private Supplier<Integer> paramSupplier(char instructionMode, int paramNumber)
	{
		return () -> getParamValue(instructionMode, paramNumber);
	}

	public Instruction readInstruction()
	{
		int opcode = memory.get(instructionCounter);
		String opcodepadded = StringUtils.leftPad(String.valueOf(opcode), 5, '0');

		Supplier<Integer> param1Mode = paramSupplier(opcodepadded.charAt(2), 1);
		Supplier<Integer> param2Mode = paramSupplier(opcodepadded.charAt(1), 2);
		Supplier<Integer> param3Mode = paramSupplier(opcodepadded.charAt(0), 3);

		Supplier<Integer> param1Immediate = () -> readValueAt(instructionCounter + 1);
		Supplier<Integer> param2Immediate = () -> readValueAt(instructionCounter + 2);
		Supplier<Integer> param3Immediate = () -> readValueAt(instructionCounter + 3);

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
			default:
				throw new IllegalStateException();
		}
	}

	int readValueAt(int location)
	{
		return memory.get(location);
	}

	public List<Integer> getMemory()
	{
		return this.memory;
	}

	public int getInstructionCounter()
	{
		return instructionCounter;
	}

	interface Instruction
	{
		void execute(List<Integer> memory) throws InterruptedException;
	}

	abstract class BiArgumentsWithResultInstruction implements Instruction
	{
		private Supplier<Integer> param1Value;
		private Supplier<Integer> param2Value;
		private Supplier<Integer> writeLocationParam;

		public BiArgumentsWithResultInstruction(Supplier<Integer> param1Value,
						Supplier<Integer> param2Value,
						Supplier<Integer> writeLocationParam)
		{
			this.param1Value = param1Value;
			this.param2Value = param2Value;
			this.writeLocationParam = writeLocationParam;
		}

		@Override
		public void execute(List<Integer> memory)
		{
			int param1 = param1Value.get();
			int param2 = param2Value.get();
			int writeLocation = writeLocationParam.get();
			int result = doOperation(param1, param2);
			memory.set(writeLocation, result);
			logger.debug(" [add] writeLocation=" + writeLocation
							+ ",param1=" + param1 + ",param2=" + param2
							+ ",result=" + result);

			instructionCounter += 4;
		}

		public abstract int doOperation(int param1, int param2);
	}

	class AddInstruction extends BiArgumentsWithResultInstruction
	{
		public AddInstruction(Supplier<Integer> param1Value,
						Supplier<Integer> param2Value, Supplier<Integer> writeLocationParam)
		{
			super(param1Value, param2Value, writeLocationParam);
		}

		@Override
		public int doOperation(int param1, int param2)
		{
			return param1 + param2;
		}
	}

	class MultInstruction extends BiArgumentsWithResultInstruction
	{
		public MultInstruction(Supplier<Integer> param1Value,
						Supplier<Integer> param2Value, Supplier<Integer> writeLocationParam)
		{
			super(param1Value, param2Value, writeLocationParam);
		}

		@Override
		public int doOperation(int param1, int param2)
		{
			return param1 * param2;
		}
	}

	class InputInstruction implements Instruction
	{
		private Supplier<Integer> writeLocationParam;

		public InputInstruction(Supplier<Integer> writeLocationParam)
		{
			this.writeLocationParam = writeLocationParam;
		}

		@Override
		public void execute(List<Integer> memory) throws InterruptedException
		{
			String inputEvent = stdin.take();
			Scanner scanner = new Scanner(new StringReader(inputEvent));
			int value = scanner.nextInt();
			int writeLocation = writeLocationParam.get();
			memory.set(writeLocation, value);
			logger.debug("[input] writeLocation=" + writeLocation + ", value=" + value);

			instructionCounter += 2;
		}
	}

	class OutputInstruction implements Instruction
	{
		private Supplier<Integer> param1Value;

		public OutputInstruction(Supplier<Integer> param1Value)
		{
			this.param1Value = param1Value;
		}

		@Override
		public void execute(List<Integer> memory)
		{
			int param1 = param1Value.get();
			logger.debug("[output] value=" + param1);
			stdout.offer(String.valueOf(param1));
			instructionCounter += 2;
		}
	}

	class HaltInstruction implements Instruction
	{
		@Override
		public void execute(List<Integer> memory)
		{
			logger.debug("[halt]");
			throw new HaltException();
		}
	}

	abstract class AbstractJumpInstruction2 implements Instruction
	{
		private Supplier<Integer> param1Value;
		private Supplier<Integer> param2Value;

		public AbstractJumpInstruction2(Supplier<Integer> param1Value, Supplier<Integer> param2Value)
		{
			this.param1Value = param1Value;
			this.param2Value = param2Value;
		}

		@Override
		public void execute(List<Integer> memory)
		{
			int param1 = param1Value.get();
			int param2 = param2Value.get();
			boolean jump = check(param1, param2);

			logger.debug("[ji?] param1=" + param1 + ",param2="
							+ param2 + ",jump=" + jump);

			if (jump)
				instructionCounter = param2;
			else
				instructionCounter += 3;
		}

		public abstract boolean check(int param1Value, int param2Value);
	}

	public class JumpIfTrueInstruction extends AbstractJumpInstruction2
	{
		public JumpIfTrueInstruction(Supplier<Integer> param1Value, Supplier<Integer> param2Value)
		{
			super(param1Value, param2Value);
		}

		@Override
		public boolean check(int param1Value, int param2Value)
		{
			return param1Value != 0;
		}
	}

	public class JumpIfFalseInstruction extends AbstractJumpInstruction2
	{
		public JumpIfFalseInstruction(Supplier<Integer> param1Value,
						Supplier<Integer> param2Value)
		{
			super(param1Value, param2Value);
		}

		@Override
		public boolean check(int param1Value, int param2Value)
		{
			return param1Value == 0;
		}
	}

	abstract class AbstractCompInstruction3 implements Instruction
	{
		private Supplier<Integer> param1Value;
		private Supplier<Integer> param2Value;
		private Supplier<Integer> writeLocationParam;

		public AbstractCompInstruction3(Supplier<Integer> param1Value,
						Supplier<Integer> param2Value, Supplier<Integer> writeLocationParam)
		{
			this.param1Value = param1Value;
			this.param2Value = param2Value;
			this.writeLocationParam = writeLocationParam;
		}

		@Override
		public void execute(List<Integer> memory)
		{
			int param1 = param1Value.get();
			int param2 = param2Value.get();
			int writeLocation = writeLocationParam.get();
			boolean store = check(param1, param2);

			logger.debug("[comp?] param1=" + param1
							+ ",param2=" + param2 + ",param3=" + writeLocation + ",store=" + store);

			if (store)
			{
				memory.set(writeLocation, 1);
			}
			else
			{
				memory.set(writeLocation, 0);
			}

			instructionCounter += 4;
		}

		public abstract boolean check(int param1Value, int param2Value);
	}

	class LessThanInstruction extends AbstractCompInstruction3
	{
		public LessThanInstruction(Supplier<Integer> param1Value, Supplier<Integer> param2Value,
						Supplier<Integer> param3Value)
		{
			super(param1Value, param2Value, param3Value);
		}

		@Override
		public boolean check(int param1Value, int param2Value)
		{
			return param1Value < param2Value;
		}
	}

	class EqualInstruction extends AbstractCompInstruction3
	{
		public EqualInstruction(Supplier<Integer> param1Value, Supplier<Integer> param2Value,
						Supplier<Integer> param3Value)
		{
			super(param1Value, param2Value, param3Value);
		}

		@Override
		public boolean check(int param1Value, int param2Value)
		{
			return param1Value == param2Value;
		}
	}

	class HaltException extends RuntimeException
	{

	}
}
