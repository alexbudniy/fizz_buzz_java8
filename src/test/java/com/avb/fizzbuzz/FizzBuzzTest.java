package com.avb.fizzbuzz;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.avb.fizzbuzz.FizzBuzz.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by alex on 09/08/2016.
 */
@RunWith(JUnitParamsRunner.class)
public class FizzBuzzTest {

	private static final List<String> FIRST_TWENTY_RESULTS = Arrays.asList("1", "2", FizzBuzz.LUCKY, "4", FizzBuzz.BUZZ,
			FIZZ, "7", "8", FIZZ, FizzBuzz.BUZZ, "11", FIZZ, FizzBuzz.LUCKY, "14",
			FizzBuzz.FIZZBUZZ, "16", "17", FIZZ, "19", FizzBuzz.BUZZ);

	private static final String EXPECTED_OUTPUT_ONE_TWENTY = String.join(" ", FIRST_TWENTY_RESULTS) + " \n";

	private static Map<String, Long> expectedStatistics = new HashMap<>();

	private static final String EXPECTED_REPORT_ONE_TWENTY = "fizz: 4\n"
			+ "buzz: 3\n"
			+ "fizzbuzz: 1\n"
			+ "lucky: 2\n"
			+ "integer: 10\n";

	@BeforeClass
	public static void setUp() {
		expectedStatistics.put(FIZZ,4L);
		expectedStatistics.put(BUZZ,3L);
		expectedStatistics.put(FIZZBUZZ,1L);
		expectedStatistics.put(LUCKY,2L);
		expectedStatistics.put(INTEGER,10L);
	}

	private FizzBuzz fizzBuzz = new FizzBuzz();

	@Test
	@Parameters({"1", "2", "4", "7", "11", "14"})
	public void testReturnsNumberForNumberNotDivisibleByThreeOrFive(int number) {
		assertEquals(fizzBuzz.transformNumber(number), String.valueOf(number));
	}

	@Test
	@Parameters({"6", "9", "12", "18", "21", "24"})
	public void testReturnFizzForNumberDivisibleByThree(int number) {
		assertEquals(fizzBuzz.transformNumber(number), FIZZ);
	}

	@Test
	@Parameters({"5", "10", "20", "25", "40", "50"})
	public void testReturnBuzzForNumberDivisibleByFive(int number) {
		assertEquals(fizzBuzz.transformNumber(number), FizzBuzz.BUZZ);
	}

	@Test
	@Parameters({"15", "45", "60"})
	public void testReturnsFizzBuzzForNumberDivisibleByThreeAndFive(int number) {
		assertEquals(fizzBuzz.transformNumber(number), FizzBuzz.FIZZBUZZ);
	}

	@Test
	@Parameters({"3", "13", "23", "30", "31", "33"})
	public void testReturnsLuckyForNumberContainingThree(int number) {
		assertEquals(fizzBuzz.transformNumber(number), FizzBuzz.LUCKY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParameters_negativeStartThrowsException() {
		fizzBuzz.calculate(-2, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParameters_zeroStartThrowsException() {
		fizzBuzz.calculate(0, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParameters_startBeforeEndThrowsException() {
		fizzBuzz.calculate(3, 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParameters_startEqualsEndThrowsException() {
		fizzBuzz.calculate(2, 2);
	}

	@Test
	public void testProcessAndPrintCalculatesStatisticsOneToTwenty() {
		Map<String, Long> actualStatistics = fizzBuzz.processAndPrint(1, 20);
		assertThat(actualStatistics).isEqualTo(expectedStatistics);
	}

	@Test
	public void testProcessAndPrintPrintsCorrectValuesOneToTwenty() {

		FizzBuzz fizzBuzzSpy = Mockito.spy(FizzBuzz.class);

		ByteArrayOutputStream outSpy = new ByteArrayOutputStream();
		fizzBuzzSpy.setOutput(new PrintStream(outSpy));

		fizzBuzzSpy.processAndPrint(1, 20);

		assertThat(outSpy.toString()).isEqualToIgnoringWhitespace(EXPECTED_OUTPUT_ONE_TWENTY);
	}

	@Test
	public void testPrintReportPrintsCorrectReportOneToTwenty() {
		ByteArrayOutputStream outSpy = new ByteArrayOutputStream();
		fizzBuzz.setOutput(new PrintStream(outSpy));

		fizzBuzz.printReport(expectedStatistics);
		assertThat(outSpy.toString()).isEqualTo(EXPECTED_REPORT_ONE_TWENTY);
	}

	@Test
	public void testVerifyCalculatePrintsCorrectOutput() {
		FizzBuzz fizzBuzzSpy = Mockito.spy(FizzBuzz.class);

		ByteArrayOutputStream outSpy = new ByteArrayOutputStream();
		fizzBuzzSpy.setOutput(new PrintStream(outSpy));

		fizzBuzzSpy.calculate(1, 20);
		String actualOutput = outSpy.toString();
		assertThat(actualOutput).contains(EXPECTED_OUTPUT_ONE_TWENTY);
		assertThat(actualOutput).contains(EXPECTED_REPORT_ONE_TWENTY);

		verify(fizzBuzzSpy).processAndPrint(1, 20);
		for (int i = 1; i < 20; i++) {
			verify(fizzBuzzSpy).transformNumber(i);
		}
		verify(fizzBuzzSpy).printReport(any());
	}
}