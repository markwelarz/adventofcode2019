package advent;

import com.google.common.base.Splitter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day5
{
	private List<Integer> memory;

	public void loadAndExecute(Resource input) throws IOException
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

	public void runProgram()
	{
		try
		{
			int i = 0;
			while (i < memory.size())
			{
				//				System.out.println("memory " + memory + ", pc=" + i + ", value at=" + memory.get(i));
				Instruction instruction = readInstruction(i);
				int jumpTo = instruction.execute(memory);
				if (jumpTo == -99)
					i += instruction.instructionSize();
				else
					i = jumpTo;
			}
		}
		catch (HaltException e)
		{
		}
	}

	public Instruction readInstruction(int instructionCounter)
	{
		int opcode = memory.get(instructionCounter);
		String opcodepadded = StringUtils.leftPad(String.valueOf(opcode), 5, '0');
		ValueGetter positionGetter = (m, v) -> m.get(v);
		ValueGetter immediateGetter = (m, v) -> v;
		ValueGetter param1Getter = opcodepadded.charAt(2) == '0' ? positionGetter : immediateGetter;
		ValueGetter param2Getter = opcodepadded.charAt(1) == '0' ? positionGetter : immediateGetter;
		ValueGetter param3Getter = opcodepadded.charAt(0) == '0' ? positionGetter : immediateGetter;

		switch (opcodepadded.substring(3))
		{
			case "01":
				return new AddInstruction(instructionCounter, param1Getter, readValueAt(instructionCounter + 1),
								param2Getter, readValueAt(instructionCounter + 2),
								readValueAt(instructionCounter + 3));
			case "02":
				return new MultInstruction(instructionCounter, param1Getter, readValueAt(instructionCounter + 1),
								param2Getter, readValueAt(instructionCounter + 2),
								readValueAt(instructionCounter + 3));
			case "99":
				return new HaltInstruction();
			case "03":
				return new InputInstruction(instructionCounter, param1Getter, readValueAt(instructionCounter + 1));
			case "04":
				return new OutputInstruction(instructionCounter, param1Getter, readValueAt(instructionCounter + 1));
			case "05":
				return new JumpIfTrueInstruction(instructionCounter, param1Getter, readValueAt(instructionCounter + 1),
								param2Getter, readValueAt(instructionCounter + 2));
			case "06":
				return new JumpIfFalseInstruction(instructionCounter, param1Getter, readValueAt(instructionCounter + 1),
								param2Getter, readValueAt(instructionCounter + 2));
			case "07":
				return new LessThanInstruction(instructionCounter, param1Getter, readValueAt(instructionCounter + 1),
								param2Getter, readValueAt(instructionCounter + 2), param3Getter,
								readValueAt(instructionCounter + 3));
			case "08":
				return new EqualInstruction(instructionCounter, param1Getter, readValueAt(instructionCounter + 1),
								param2Getter, readValueAt(instructionCounter + 2), param3Getter,
								readValueAt(instructionCounter + 3));

			default:
				throw new IllegalStateException();
		}
	}

	int readValueAt(int location)
	{
		return memory.get(location);
	}

	String instructionToString(int position, Instruction instruction)
	{
		return memory.subList(position, position + instruction.instructionSize()).toString();
	}

	interface ValueGetter
	{
		int get(List<Integer> memory, int paramValue);
	}

	static interface Instruction
	{
		int execute(List<Integer> memory);

		int instructionSize();
	}

	class AddInstruction implements Instruction
	{
		private int instructionCounter;
		private ValueGetter param1Getter;
		private int param1Value;
		private ValueGetter param2Getter;
		private int param2Value;
		private int writeLocation;

		public AddInstruction(int instructionCounter, ValueGetter param1Getter, int param1Value,
						ValueGetter param2Getter, int param2Value,
						int writeLocation)
		{
			this.instructionCounter = instructionCounter;
			this.param1Getter = param1Getter;
			this.param1Value = param1Value;
			this.param2Getter = param2Getter;
			this.param2Value = param2Value;
			this.writeLocation = writeLocation;
		}

		@Override
		public int execute(List<Integer> memory)
		{
			int param1 = param1Getter.get(memory, param1Value);
			int param2 = param2Getter.get(memory, param2Value);
			int result = param1 + param2;
			memory.set(writeLocation, result);
			System.out.println(instructionToString(instructionCounter, this) + " [add] writeLocation=" + writeLocation
							+ ",param1=" + param1 + ",param2=" + param2
							+ ",result=" + result);

			return -99;
		}

		@Override
		public int instructionSize()
		{
			return 4;
		}
	}

	class MultInstruction implements Instruction
	{
		private int instructionCounter;
		private ValueGetter param1Getter;
		private int param1Value;
		private ValueGetter param2Getter;
		private int param2Value;
		private int writeLocation;

		public MultInstruction(int instructionCounter, ValueGetter param1Getter, int param1Value,
						ValueGetter param2Getter, int param2Value,
						int writeLocation)
		{
			this.instructionCounter = instructionCounter;
			this.param1Getter = param1Getter;
			this.param1Value = param1Value;
			this.param2Getter = param2Getter;
			this.param2Value = param2Value;
			this.writeLocation = writeLocation;
		}

		@Override
		public int execute(List<Integer> memory)
		{
			int param1 = param1Getter.get(memory, param1Value);
			int param2 = param2Getter.get(memory, param2Value);
			int result = param1 * param2;
			memory.set(writeLocation, result);
			System.out.println(
							instructionToString(instructionCounter, this) + " [multiply] writeLocation=" + writeLocation
											+ ",param1=" + param1 + ",param2=" + param2
											+ ",result=" + result);
			return -99;
		}

		@Override
		public int instructionSize()
		{
			return 4;
		}
	}

	class InputInstruction implements Instruction
	{
		private int instructionCounter;
		private ValueGetter param1Getter;
		private int writeLocation;

		public InputInstruction(int instructionCounter, ValueGetter param1Getter, int writeLocation)
		{
			this.instructionCounter = instructionCounter;
			this.param1Getter = param1Getter;
			this.writeLocation = writeLocation;
		}

		@Override
		public int execute(List<Integer> memory)
		{
			Scanner sc = new Scanner(System.in);
			int value = sc.nextInt();
			memory.set(writeLocation, value);
			System.out.println("[input] writeLocation=" + writeLocation + ", value=" + value);
			return -99;
		}

		@Override
		public int instructionSize()
		{
			return 2;
		}
	}

	class OutputInstruction implements Instruction
	{
		private int instructionCounter;
		private ValueGetter param1Getter;
		private int param1Value;

		public OutputInstruction(int instructionCounter, ValueGetter param1Getter, int param1Value)
		{
			this.instructionCounter = instructionCounter;
			this.param1Getter = param1Getter;
			this.param1Value = param1Value;
		}

		@Override
		public int execute(List<Integer> memory)
		{
			System.out.println(instructionToString(instructionCounter, this) + " [output] location=" + param1Value
							+ ",value=" + param1Getter.get(memory, param1Value));
			return -99;
		}

		@Override
		public int instructionSize()
		{
			return 2;
		}
	}

	class HaltInstruction implements Instruction
	{
		@Override
		public int execute(List<Integer> memory)
		{
			System.out.println("[halt]");
			throw new HaltException();
		}

		@Override
		public int instructionSize()
		{
			return 1;
		}
	}

	abstract class AbstractJumpInstruction2 implements Instruction
	{
		private int instructionCounter;
		private ValueGetter param1Getter;
		private int param1Value;
		private ValueGetter param2Getter;
		private int param2Value;

		public AbstractJumpInstruction2(int instructionCounter, ValueGetter param1Getter, int param1Value,
						ValueGetter param2Getter, int param2Value)
		{
			this.instructionCounter = instructionCounter;
			this.param1Getter = param1Getter;
			this.param1Value = param1Value;
			this.param2Getter = param2Getter;
			this.param2Value = param2Value;
		}

		@Override
		public int execute(List<Integer> memory)
		{
			int param1 = param1Getter.get(memory, param1Value);
			int param2 = param2Getter.get(memory, param2Value);
			boolean jump = check(param1, param2);

			System.out.println(
							instructionToString(instructionCounter, this) + " [ji?] param1=" + param1 + ",param2="
											+ param2 + ",jump=" + jump);

			if (jump)
			{
				return param2;
			}

			return -99;
		}

		@Override
		public int instructionSize()
		{
			return 3;
		}

		public abstract boolean check(int param1Value, int param2Value);

	}

	public class JumpIfTrueInstruction extends AbstractJumpInstruction2
	{
		public JumpIfTrueInstruction(int instructionCounter, ValueGetter param1Getter, int param1Value,
						ValueGetter param2Getter, int param2Value)
		{
			super(instructionCounter, param1Getter, param1Value, param2Getter, param2Value);
		}

		@Override
		public boolean check(int param1Value, int param2Value)
		{
			return param1Value != 0;
		}
	}

	public class JumpIfFalseInstruction extends AbstractJumpInstruction2
	{
		public JumpIfFalseInstruction(int instructionCounter, ValueGetter param1Getter, int param1Value,
						ValueGetter param2Getter, int param2Value)
		{
			super(instructionCounter, param1Getter, param1Value, param2Getter, param2Value);
		}

		@Override
		public boolean check(int param1Value, int param2Value)
		{
			return param1Value == 0;
		}
	}

	abstract class AbstractCompInstruction3 implements Instruction
	{
		private int instructionCounter;
		private ValueGetter param1Getter;
		private int param1Value;
		private ValueGetter param2Getter;
		private int param2Value;
		private ValueGetter param3Getter;
		private int param3Value;

		public AbstractCompInstruction3(int instructionCounter, ValueGetter param1Getter, int param1Value,
						ValueGetter param2Getter, int param2Value, ValueGetter param3Getter, int param3Value)
		{
			this.instructionCounter = instructionCounter;
			this.param1Getter = param1Getter;
			this.param1Value = param1Value;
			this.param2Getter = param2Getter;
			this.param2Value = param2Value;
			this.param3Getter = param3Getter;
			this.param3Value = param3Value;
		}

		@Override
		public int execute(List<Integer> memory)
		{
			int param1 = param1Getter.get(memory, param1Value);
			int param2 = param2Getter.get(memory, param2Value);
			int param3 = param3Value; // this is the memory value to set to
			boolean store = check(param1, param2);

			System.out.println(
							instructionToString(instructionCounter, this) + " [comp?] param1=" + param1 + ",param2="
											+ param2 + ",param3=" + param3 + ",store=" + store);

			if (store)
			{
				memory.set(param3, 1);
			}
			else
			{
				memory.set(param3, 0);
			}
			return -99;
		}

		@Override
		public int instructionSize()
		{
			return 4;
		}

		public abstract boolean check(int param1Value, int param2Value);
	}

	class LessThanInstruction extends AbstractCompInstruction3
	{
		public LessThanInstruction(int instructionCounter, ValueGetter param1Getter, int param1Value,
						ValueGetter param2Getter, int param2Value, ValueGetter param3Getter, int param3Value)
		{
			super(instructionCounter, param1Getter, param1Value, param2Getter, param2Value, param3Getter, param3Value);
		}

		@Override
		public boolean check(int param1Value, int param2Value)
		{
			return param1Value < param2Value;
		}
	}

	class EqualInstruction extends AbstractCompInstruction3
	{
		public EqualInstruction(int instructionCounter, ValueGetter param1Getter, int param1Value,
						ValueGetter param2Getter, int param2Value, ValueGetter param3Getter, int param3Value)
		{
			super(instructionCounter, param1Getter, param1Value, param2Getter, param2Value, param3Getter, param3Value);
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
