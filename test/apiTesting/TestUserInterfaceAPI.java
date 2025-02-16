package apiTesting;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Scanner;

import org.junit.Test;

public class TestUserInterfaceAPI {

    /**
     * Test that a valid input on the first try returns the expected value.
     */
    @Test
    public void testValidInputFirstTry() {
        // Create a mock Scanner.
        Scanner mockScanner = mock(Scanner.class);
        // Simulate a valid input (5) on the first call.
        when(mockScanner.nextInt()).thenReturn(5);
        
        // Invoke the API method.
        int result = UserInterfaceAPI.getUserInput(mockScanner);
        
        // Assert the returned value is as expected.
        assertEquals("Expected input value is 5", 5, result);
        // Verify nextInt() was called exactly once.
        verify(mockScanner, times(1)).nextInt();
    }

    /**
     * Test that an invalid input followed by a valid input returns the expected valid value.
     */
    @Test
    public void testInvalidThenValidInput() {
        Scanner mockScanner = mock(Scanner.class);
        // First, simulate an invalid input (-1), then a valid input (10).
        when(mockScanner.nextInt()).thenReturn(-1, 10);
        
        int result = UserInterfaceAPI.getUserInput(mockScanner);
        
        assertEquals("Expected valid input of 10", 10, result);
        // Verify nextInt() was called twice.
        verify(mockScanner, times(2)).nextInt();
    }

    /**
     * Test that multiple invalid inputs followed by a valid input return the expected value.
     */
    @Test
    public void testMultipleInvalidThenValidInput() {
        Scanner mockScanner = mock(Scanner.class);
        // Simulate three invalid inputs (-5, 0, -3) followed by a valid input (15).
        when(mockScanner.nextInt()).thenReturn(-5, 0, -3, 15);
        
        int result = UserInterfaceAPI.getUserInput(mockScanner);
        
        assertEquals("Expected valid input of 15", 15, result);
        // Verify nextInt() was called four times.
        verify(mockScanner, times(4)).nextInt();
    }
}
