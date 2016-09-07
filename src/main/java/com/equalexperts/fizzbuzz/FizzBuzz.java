package com.equalexperts.fizzbuzz;

import java.io.PrintStream;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * Prints out the following for a contiguous range of numbers:
 *
 * "fizz" for numbers that are multiples of 3
 * "buzz" for numbers that are multiples of 5
 * "fizzbuzz" for numbers that are multiples of 15
 * "lucky" for numbers which contain 3
 * the number in other cases.
 *
 * Also prints out the report how many times each element was encountered.
 *
 * Created by alex on 09/08/2016.
 */
public class FizzBuzz {

	public static final String FIZZ = "fizz";
	public static final String BUZZ = "buzz";
	public static final String FIZZBUZZ = "fizzbuzz";
	public static final String LUCKY = "lucky";
	public static final String INTEGER = "integer";

	private PrintStream output = System.out;

	public void calculate(int start, int end) {

		validate(start, end);

		Map<String, Long> statistics = processAndPrint(start, end);

		printReport(statistics);
	}

	//package visibility for unit testing
	Map<String, Long> processAndPrint(int start, int end) {
		Map<String, Long> statistics = IntStream.range(start, end + 1)
				.mapToObj(this::transformNumber)
				.peek(val -> output.print(val + " "))
				.map(val -> isNumeric(val) ? INTEGER : val)
				.collect(groupingBy(s -> s, counting()));
		output.println();
		return statistics;
	}

	//package visibility for unit testing
	void printReport(Map<String, Long> statistics) {
		Stream.of(FIZZ, BUZZ, FIZZBUZZ, LUCKY, INTEGER)
				.map(type -> appendCount(type, statistics))
				.forEach(output::println);
	}

	private String appendCount(String type, Map<String, Long> statistics) {
		return String.format("%s: %s", type, statistics.getOrDefault(type, 0L));
	}

	private void validate(int start, int end) {
		if (start >= end) {
			throw new IllegalArgumentException("Start should be smaller than end");
		}
		if (start <= 0 ) {
			throw new IllegalArgumentException("Start should be bigger than 0");
		}
	}

	//package visibility for unit testing
	String transformNumber(int number) {
		if (containsThree(number)) {
			return LUCKY;
		} else if (isMultipleOfThree(number) && isMultipleOfFive(number)) {
			return FIZZBUZZ;
		} else if (isMultipleOfThree(number)) {
			return FIZZ;
		} else if (isMultipleOfFive(number)) {
			return BUZZ;
		} else {
			return String.valueOf(number);
		}
	}

	private boolean containsThree(int number) {
		return String.valueOf(number).contains("3");
	}

	private boolean isMultipleOfThree(int number) {
		return number % 3 == 0;
	}

	private boolean isMultipleOfFive(int number) {
		return number % 5 == 0;
	}

	//package visibility for unit testing
	void setOutput(PrintStream output) {
		this.output = output;
	}
}
