package advent.intcode;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class Day7ComputerTest
{
	@Mock
	LinkedBlockingQueue<String> stdout;

	@Test
	public void t1() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(stdout, input(""));
		day7Computer.loadAndExecute(
						new InputStreamResource(IOUtils.toInputStream("1002,4,3,4,33", StandardCharsets.UTF_8)));

		assertThat(day7Computer.getMemory().get(4)).isEqualTo(99);
	}

	@Test
	public void t2() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("5"), stdout);
		day7Computer.loadAndExecute(
						new ClassPathResource("advent/day5input.txt"));

		verify(stdout).offer("8346937");
	}

	@Test
	public void t3() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("8"), stdout);
		day7Computer.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,9,8,9,10,9,4,9,99,-1,8", StandardCharsets.UTF_8)));
		verify(stdout).offer("1");
	}

	@Test
	public void t4() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("7"), stdout);
		day7Computer.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,9,8,9,10,9,4,9,99,-1,8", StandardCharsets.UTF_8)));
		verify(stdout).offer("0");
	}

	@Test
	public void t5() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("4"), stdout);
		day7Computer.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,9,7,9,10,9,4,9,99,-1,8", StandardCharsets.UTF_8)));
		verify(stdout).offer("1");
	}

	@Test
	public void t6() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("9"), stdout);
		day7Computer.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,9,7,9,10,9,4,9,99,-1,8", StandardCharsets.UTF_8)));
		verify(stdout).offer("0");
	}

	@Test
	public void t7() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("8"), stdout);
		day7Computer.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,3,1108,-1,8,3,4,3,99", StandardCharsets.UTF_8)));
		verify(stdout).offer("1");
	}

	@Test
	public void t8() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("6"), stdout);
		day7Computer.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,3,1108,-1,8,3,4,3,99", StandardCharsets.UTF_8)));
		verify(stdout).offer("0");
	}

	@Test
	public void t9() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("6"), stdout);
		day7Computer.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,3,1107,-1,8,3,4,3,99", StandardCharsets.UTF_8)));
		verify(stdout).offer("1");
	}

	@Test
	public void t10() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("9"), stdout);
		day7Computer.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,3,1107,-1,8,3,4,3,99", StandardCharsets.UTF_8)));
		verify(stdout).offer("0");
	}

	@Test
	public void t11() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("0"), stdout);
		day7Computer.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9",
														StandardCharsets.UTF_8)));
		verify(stdout).offer("0");
	}

	@Test
	public void t12() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("5"), stdout);
		day7Computer.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9",
														StandardCharsets.UTF_8)));
		verify(stdout).offer("1");
	}

	@Test
	public void t13() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("0"), stdout);
		day7Computer.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,3,1105,-1,9,1101,0,0,12,4,12,99,1",
														StandardCharsets.UTF_8)));
		verify(stdout).offer("0");
	}

	@Test
	public void t14() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("2"), stdout);
		day7Computer.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,3,1105,-1,9,1101,0,0,12,4,12,99,1",
														StandardCharsets.UTF_8)));
		verify(stdout).offer("1");
	}

	@Test
	public void t16() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("9"), stdout);
		day7Computer.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,"
																		+ "1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,"
																		+ "999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99",
														StandardCharsets.UTF_8)));
		verify(stdout).offer("1001");

	}

	@Test
	public void t17() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("8"), stdout);
		day7Computer.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,"
																		+ "1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,"
																		+ "999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99",
														StandardCharsets.UTF_8)));
		verify(stdout).offer("1000");
	}

	@Test
	public void t18() throws Exception
	{
		Day7Computer day7Computer = new Day7Computer(input("2"), stdout);
		day7Computer.loadAndExecute(
						new InputStreamResource(
										IOUtils.toInputStream("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,"
																		+ "1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,"
																		+ "999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99",
														StandardCharsets.UTF_8)));
		verify(stdout).offer("999");
	}

	private Resource stringResource(String code)
	{
		return new InputStreamResource(IOUtils.toInputStream(code, StandardCharsets.UTF_8));
	}

	private LinkedBlockingQueue<String> input(String input)
	{
		return Queues.newLinkedBlockingQueue(Lists.newArrayList(input));
	}

	@Nested
	class AddTests
	{
		@Test
		public void param1ImmediateParam2Immediate() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(null, null);
			day7Computer.loadAndExecute(stringResource("01101,2,4,9,99,7,13,17,23,7"));
			assertThat(day7Computer.getMemory().get(9)).isEqualTo(6);
		}

		@Test
		public void param1ImmediateParam2Position() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(null, null);
			day7Computer.loadAndExecute(stringResource("00101,2,4,9,99,7,13,17,23,44"));
			assertThat(day7Computer.getMemory().get(9)).isEqualTo(101);
		}

		@Test
		public void param1PositionParam2Position() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(null, null);
			day7Computer.loadAndExecute(stringResource("00001,2,4,9,99,7,13,17,23,44"));
			assertThat(day7Computer.getMemory().get(9)).isEqualTo(103);
		}

		@Test
		public void param1PositionParam2Immediate() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(null, null);
			day7Computer.loadAndExecute(stringResource("01001,2,4,9,99,7,13,17,23,44"));
			assertThat(day7Computer.getMemory().get(9)).isEqualTo(4 + 4);
		}

		@Test
		public void ignoreModeOfResultParam() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(null, null);
			day7Computer.loadAndExecute(stringResource("11101,2,4,9,99,7,13,17,23,7"));
			assertThat(day7Computer.getMemory().get(9)).isEqualTo(6);
		}
	}

	@Nested
	class MultiplyTests
	{
		@Test
		public void param1ImmediateParam2Immediate() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(null, null);
			day7Computer.loadAndExecute(stringResource("01102,2,4,9,99,7,13,17,23,7"));
			assertThat(day7Computer.getMemory().get(9)).isEqualTo(2 * 4);
		}

		@Test
		public void param1ImmediateParam2Position() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(null, null);
			day7Computer.loadAndExecute(stringResource("00102,2,4,9,99,7,13,17,23,44"));
			assertThat(day7Computer.getMemory().get(9)).isEqualTo(2 * 99);
		}

		@Test
		public void param1PositionParam2Position() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(null, null);
			day7Computer.loadAndExecute(stringResource("00002,2,4,9,99,7,13,17,23,44"));
			assertThat(day7Computer.getMemory().get(9)).isEqualTo(4 * 99);
		}

		@Test
		public void param1PositionParam2Immediate() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(null, null);
			day7Computer.loadAndExecute(stringResource("01002,2,4,9,99,7,13,17,23,44"));
			assertThat(day7Computer.getMemory().get(9)).isEqualTo(4 * 4);
		}

		@Test
		public void ignoreModeOfResultParam() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(null, null);
			day7Computer.loadAndExecute(stringResource("11102,2,4,9,99,7,13,17,23,7"));
			assertThat(day7Computer.getMemory().get(9)).isEqualTo(2 * 4);
		}
	}

	@Nested
	class InputInstruction
	{
		@Test
		public void inputSavesToAddress() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(input("5"), null);
			day7Computer.loadAndExecute(stringResource("00103,7,99,7,13,17,23,0"));
			assertThat(day7Computer.getMemory().get(7)).isEqualTo(5);
		}

		@Test
		public void inputIgnoreModeOfOutputParam() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(input("5"), null);
			day7Computer.loadAndExecute(stringResource("01103,7,99,7,13,17,23,0"));
			assertThat(day7Computer.getMemory().get(7)).isEqualTo(5);
		}
	}

	@Nested
	class OutputInstruction
	{
		@Test
		public void param1Position() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(null, stdout);
			day7Computer.loadAndExecute(stringResource("00004,7,99,7,13,17,0,23"));
			verify(stdout).offer("23");
		}

		@Test
		public void param1Immediate() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(null, stdout);
			day7Computer.loadAndExecute(stringResource("00104,7,99,7,13,17,23,0"));
			verify(stdout).offer("7");
		}
	}

	@Nested
	class JumpIfTrueInstruction
	{
		@Test
		public void param1Immediateparam2ImmediateShouldSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01105,4,9,77,77,77,77,77,77,99"));
		}

		@Test
		public void param1Immediateparam2ImmediateShouldNotSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01105,0,9,99,77,77,77,77,77,77,77"));
		}

		@Test
		public void param1Positionparam2ImmediateShouldSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01005,9,10,77,77,77,77,77,9,99"));
		}

		@Test
		public void param1Positionparam2ImmediateShouldNotSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01005,9,9,99,77,77,77,77,77,0,77"));
		}

		@Test
		public void param1Positionparam2PositionShouldSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00005,9,10,77,77,77,77,77,77,77,99"));
		}

		@Test
		public void param1Positionparam2PositionShouldNotSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00005,9,9,99,77,77,77,77,77,0,77"));
		}

		@Test
		public void param1Immediateparam2PositionShouldSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00105,9,10,77,77,77,77,77,77,77,99"));
		}

		@Test
		public void param1Immediateparam2PositionShouldNotSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00105,0,9,99,77,77,77,77,77,0,77"));
		}
	}

	@Nested
	class JumpIfFalseInstruction
	{
		@Test
		public void param1Immediateparam2ImmediateShouldSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01106,0,9,77,77,77,77,77,77,99"));
		}

		@Test
		public void param1Immediateparam2ImmediateShouldNotSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01106,4,9,99,77,77,77,77,77,77"));
		}

		@Test
		public void param1Positionparam2ImmediateShouldSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01006,8,9,99,77,77,77,77,9"));
		}

		@Test
		public void param1Positionparam2ImmediateShouldNotSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01006,9,10,77,77,77,77,77,77,0,99"));
		}

		@Test
		public void param1Positionparam2PositionShouldSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00006,9,10,77,77,77,77,77,77,0,99"));
		}

		@Test
		public void param1Positionparam2PositionShouldNotSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00006,9,9,99,77,77,77,77,77,77"));
		}

		@Test
		public void param1Immediateparam2PositionShouldSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00106,0,10,77,77,77,77,77,77,77,99"));
		}

		@Test
		public void param1Immediateparam2PositionShouldNotSet() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00106,7,9,99,77,77,77,77,77,0,77"));
		}
	}

	@Nested
	class LessThanInstruction
	{
		@Test
		public void param1ImmediateParam2ImmediateReturns1() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01107,3,8,5,99,5"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(1);
		}

		@Test
		public void param1ImmediateParam2ImmediateReturns0() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01107,10,9,5,99,5"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(0);
		}

		@Test
		public void param1ImmediateParam2PositionReturns1() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00107,3,3,5,99,5,6,7,8,9"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(1);
		}

		@Test
		public void param1ImmediateParam2PositionReturns0() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00107,8,5,5,99,5,6,7,8,9"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(0);
		}

		@Test
		public void param1PositionParam2ImmediateReturns1() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01007,9,10,5,99,5,6,7,8,9"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(1);
		}

		@Test
		public void param1PositionParam2ImmediateReturns0() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01007,8,5,5,99,5,6,7,8,3"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(0);
		}

		@Test
		public void param1PositionParam2PositionReturns1() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00007,3,9,5,99,5,6,7,8,9"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(1);
		}

		@Test
		public void param1PositionParam2PositionReturns0() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00007,8,5,5,99,5,6,7,8,3"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(0);
		}

		@Test
		public void param1ImmediateParam2ImmediateOutputImmediate() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("11107,3,8,5,99,5"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(1);
		}

		@Test
		public void param1ImmediateParam2ImmediateOutputPosition() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01107,10,9,5,99,10,6,7,8,9,10"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(0);
		}

	}

	@Nested
	class EqualToInstruction
	{
		@Test
		public void param1ImmediateParam2ImmediateReturns1() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01108,3,3,5,99,5"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(1);
		}

		@Test
		public void param1ImmediateParam2ImmediateReturns0() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01108,3,4,5,99,5"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(0);
		}

		@Test
		public void param1ImmediateParam2PositionReturns1() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00108,5,3,5,99,5,6,7,8,9"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(1);
		}

		@Test
		public void param1ImmediateParam2PositionReturns0() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00108,8,5,5,99,5,6,7,8,9"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(0);
		}

		@Test
		public void param1PositionParam2ImmediateReturns1() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01008,3,5,5,99,5,6,7,8,9"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(1);
		}

		@Test
		public void param1PositionParam2ImmediateReturns0() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01008,3,6,5,99,5,6,7,8,3"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(0);
		}

		@Test
		public void param1PositionParam2PositionReturns1() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00008,3,9,5,99,5,6,7,8,5"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(1);
		}

		@Test
		public void param1PositionParam2PositionReturns0() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("00008,8,5,5,99,5,6,7,8,3"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(0);
		}

		@Test
		public void param1ImmediateParam2ImmediateOutputImmediate() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("11108,3,3,5,99,5"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(1);
		}

		@Test
		public void param1ImmediateParam2ImmediateOutputPosition() throws Exception
		{
			Day7Computer day7Computer = new Day7Computer(stdout, null);
			day7Computer.loadAndExecute(stringResource("01108,10,9,5,99,10,6,7,8,9,10"));
			assertThat(day7Computer.getMemory().get(5)).isEqualTo(0);
		}
	}
}
