package advent;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16
{
	private Logger logger = LoggerFactory.getLogger(Day16.class);

	public String applyFft(int phases, String inputSignal, Function<List<Integer>, String> transformAnswer)
					throws Exception
	{
		List<Integer> input = inputSignal.chars()
						.map(v -> Character.digit(v, 10))
						.boxed()
						.collect(Collectors.toList());

		List<Integer> result = null;
		for (int p = 1; p <= phases; p++)
		{
			logger.debug("phase {}", p);
			result = fft(input);
			input = result;
		}

		return transformAnswer.apply(result);
	}

	public String applyFftPart1(int phases, String inputSignal) throws Exception
	{
		String result = applyFft(phases, inputSignal,
						v -> v.subList(0, 8).stream().map(String::valueOf).collect(Collectors.joining()));
		return result;
	}

	public String applyFftPart2(int phases, String inputSignal) throws Exception
	{
		String repeatedInput = StringUtils.repeat(inputSignal, 10000);
		String result = applyFft(phases, repeatedInput, v -> messageFromOffset(inputSignal, v));
		return result;
	}

	private String messageFromOffset(String originalInputSignal, List<Integer> transformed)
	{
		int messageOffset = Integer.parseInt(StringUtils.left(originalInputSignal, 7));
		String answer = transformed.subList(messageOffset, messageOffset + 8)
						.stream()
						.map(String::valueOf)
						.collect(Collectors.joining());
		return answer;
	}

	public List<Integer> fft(List<Integer> inputSignal) throws Exception
	{
		List<Integer> prefixSum = prefixSum(inputSignal);

		List<Integer> sequence = IntStream.rangeClosed(1, inputSignal.size())
						.parallel()
						.map(v -> doOneBandSize(inputSignal, v, prefixSum))
						.boxed()
						.collect(Collectors.toList());

		//logger.debug("phase complete: {}", sequence.stream().map(String::valueOf).collect(Collectors.joining()));

		return sequence;
	}

	private List<Integer> prefixSum(List<Integer> inputSignal)
	{
		List<Integer> prefixSum = new ArrayList<>(inputSignal.size());
		prefixSum.add(inputSignal.get(0));
		for (int i = 1; i < inputSignal.size(); i++)
		{
			prefixSum.add(prefixSum.get(i - 1) + inputSignal.get(i));
		}
		return prefixSum;
	}

	private int doOneBandSize(List<Integer> inputSignal, int position, List<Integer> prefixSum)
	{
		//		if (position % 100_000 == 0)
		//			logger.debug("doing position {} of {}", position, inputSignal.size());

		int band = 0;
		int sum = 0;
		int startPosition = position;
		if (band == 0)
			startPosition--;

		for (int i = startPosition; i < inputSignal.size(); i += 2 * position, band++)
		{
			int endSubsequence = Math.min(i + position, inputSignal.size());

			int digitSum;
			if (i > 0)
				digitSum = prefixSum.get(endSubsequence - 1) - prefixSum.get(i - 1);
			else
				digitSum = prefixSum.get(endSubsequence - 1);

			if (band % 2 == 0)
				sum += digitSum;
			else
				sum -= digitSum;
		}

		sum = Math.abs(sum) % 10;

		return sum;
	}
}
