package advent;

import com.google.common.base.Splitter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day8
{
	public static final int BLACK = '0';
	public static final int WHITE = '1';
	public static final int TRANSPARENT = '2';

	public int calculatePart1(Resource resource, int width, int height) throws IOException
	{
		String input = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
		List<String> layers = Splitter.fixedLength(width * height).splitToList(input);
		Comparator<String> numberOf0DigitsComparator = Comparator.comparing(v -> StringUtils.countMatches(v, '0'));

		System.out.println(layers);

		String leastZeros = layers.stream()
						.min(numberOf0DigitsComparator)
						.get();

		int answer = StringUtils.countMatches(leastZeros, '1') * StringUtils.countMatches(leastZeros, '2');

		return answer;
	}

	public int calculatePart2(Resource resource, int width, int height) throws IOException
	{
		String input = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
		String combinedLayers = combineLayers(input, width, height);

		List<String> imageLines = Splitter.fixedLength(width).splitToList(combinedLayers);

		imageLines.forEach(System.out::println);

		for (String line : imageLines)
		{
			for (int i = 0; i < line.length(); i++)
			{
				if (line.charAt(i) == BLACK)
					System.out.print("#\t");
				else
					System.out.print("\t");
			}
			System.out.println("");

		}

		return 1;
	}

	public String combineLayers(String input, int width, int height)
	{
		List<String> layers = Splitter.fixedLength(width * height).splitToList(input);
		String combined = layers.stream().collect(Collectors.reducing(this::combine)).get();
		return combined;
	}

	public String combine(String layer1, String layer2)
	{
		System.out.println("param 1 " + layer1 + " param2 " + layer2);
		String combined = "";

		for (int i = 0; i < layer1.length(); i++)
		{
			if (layer1.charAt(i) == TRANSPARENT)
				combined += layer2.charAt(i);
			else
				combined += layer1.charAt(i);
		}

		return combined;
	}

}
