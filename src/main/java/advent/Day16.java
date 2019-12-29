package advent;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class Day16
{
	private Logger logger = LoggerFactory.getLogger(Day16.class);

	public String applyFft(int phases, String inputSignal, Function<String, String> transformAnswer)
	{
		StringBuilder input = new StringBuilder(inputSignal);
		StringBuilder result = new StringBuilder();
		for (int p = 1; p <= phases; p++)
		{
			logger.debug("phase {}", p);
			result = fft(input);
			input = result;
		}

		return transformAnswer.apply(result.toString());
	}

	public String applyFftPart1(int phases, String inputSignal)
	{
		String result = applyFft(phases, inputSignal, v -> StringUtils.left(v, 8));
		return result;
	}

	public String applyFftPart2(int phases, String inputSignal)
	{
		String repeatedInput = StringUtils.repeat(inputSignal, 10000);
		String result = applyFft(phases, repeatedInput, v -> messageFromOffset(inputSignal, v));
		return result;
	}

	private String messageFromOffset(String originalInputSignal, String message)
	{
		int messageOffset = Integer.parseInt(StringUtils.left(originalInputSignal, 8));
		String answer = StringUtils.mid(message, messageOffset, 8);
		return answer;
	}

	public StringBuilder fft(StringBuilder inputSignal)
	{
		//  0, 1, 0, -1
		//  0,0, 1,1, 0,0, -1,-1
		//  0,0,0, 1,1,1, 0,0,0, -1,-1,-1

		StringBuilder sequence = new StringBuilder(inputSignal.length());

		for (int position = 1; position <= inputSignal.length(); position++)
		{
			if (position % 1000 == 0)
				logger.debug("doing position {} of {}", position, inputSignal.length());

			int band = 0;
			int sum = 0;
			int startPosition = position;
			if (band == 0)
				startPosition--;

			for (int i = startPosition; i < inputSignal.length(); i += 2 * position, band++)
			{
				int endSubsequence = Math.min(i + position, inputSignal.length());
				//				logger.debug("subsequence is from {} to {}", i, endSubsequence);
				int digitSum = inputSignal.subSequence(i, endSubsequence).chars()
								.map(v -> Character.digit(v, 10))
								.sum();

				if (band % 2 == 0)
					sum += digitSum;
				else
					sum -= digitSum;
			}

			sum = Math.abs(sum) % 10;
			sequence.append(sum);
			//			logger.debug("after position {}, sum={}, sequence={}", position, sum, sequence);
		}
		return new StringBuilder(sequence);
	}
}
