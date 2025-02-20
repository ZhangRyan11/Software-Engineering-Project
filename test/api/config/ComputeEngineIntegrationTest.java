package api.config;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import api.ComputeEngineAPI;

import java.util.*;

public class ComputeEngineIntegrationTest {

    // Testing 5 and comparing to expected output
    @Test
    public void testSimpleSquare() {
        int num = 5;
        int expectedSquare = num * num;
        ComputeEngineAPI toTest = new ComputeEngineAPI();
        int actualSquare = toTest.calculateSquare(num);
        Assert.assertEquals(expectedSquare, actualSquare);
    }

    // Testing 6 and comparing to expected output
    @Test
    public void testFactorsOfSmallNumber() {
        int num = 6;
        ComputeEngineAPI toTest = new ComputeEngineAPI();
        int square = toTest.calculateSquare(num);
    // Manually calculated factors of 6 up to 36
        String expectedFactors = "1 2 3 6 ";
        String actualFactors = getFactorsAsString(num, square);
        Assert.assertEquals(expectedFactors, actualFactors);
    }

    // Testing 15 and comparing to expected output
    @Test
    public void testFactorsOfLargeNumber() {
        int num = 15;
        ComputeEngineAPI toTest = new ComputeEngineAPI();
        int square = toTest.calculateSquare(num);
    // Manually calculated factors of 15 up to 225
        String expectedFactors = "1 3 5 15 ";
        String actualFactors = getFactorsAsString(num, square);
        Assert.assertEquals(expectedFactors, actualFactors);
    }

    // Testing random values 1 - 100 and comparing to expected output
    @Test
    public void testFuzzySquare() {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        System.out.println("Running with seed: " + seed);
        ComputeEngineAPI toTest = new ComputeEngineAPI();

        for (int i = 0; i < 100; i++) {
    // Getting random numbers between 1 and 100
            int val = random.nextInt(100) + 1;
            int expectedSquare = val * val;
            int actualSquare = toTest.calculateSquare(val);
            Assert.assertEquals(expectedSquare, actualSquare);
        }
    }

    // New test for ComputeEngineAPI with given inputs [1, 10, 25]
    @Test
    public void testComputeEngineWithInitialInput() {
    // Given input values
        List<Integer> inputNumbers = Arrays.asList(1, 10, 25);

    // Get the expected output
        for (int num : inputNumbers) {
    // Calculate the square of each number
            int square = num * num;
            System.out.println("Square of " + num + " is " + square);

    // Find and print factors
            System.out.print("Factors of " + num + " up to its square (" + square + "): ");
            for (int i = 1; i <= square; i++) {
                if (num % i == 0) {
                    System.out.print(i + " ");
                }
            }
            System.out.println();
        }

    // Call to test numbers
        ComputeEngineAPI toTest = new ComputeEngineAPI();

        for (int num : inputNumbers) {
    // Calculate square
            int square = toTest.calculateSquare(num);
            System.out.println("Square of " + num + " is " + square);

    // Find and print factors
            System.out.print("Factors of " + num + " up to its square (" + square + "): ");
            System.out.println(getFactorsAsString(num, square));
        }
    }

    // Making the factors into a string
    private String getFactorsAsString(int num, int square) {
        String result = "";
        for (int i = 1; i <= square; i++) {
            if (num % i == 0) {
    // Put the factors into a string
                result += i + " ";
            }
        }
        return result;
    }
}
