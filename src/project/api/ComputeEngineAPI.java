import java.util.Scanner;

public class ComputeEngineAPI {
    public static void main(String[] args) {
        // Create scanner object to read input from user
        Scanner scanner = new Scanner(System.in);

        //Creating placeholder for user input
        int num = 0;

        // Prompt for input until a valid value is entered
        while (true) {
            try {
                // Ask the user for an int and save it under the variable num (override 0) 
                System.out.print("Enter an integer greater than 0: ");
                num = scanner.nextInt();

                // If the number is 0 or less, throw an exception
                if (num <= 0) {
                    throw new IllegalArgumentException("Invalid value. Integer must be greater than 0.");
                }
                
                // When value is valid, break out of the loop
                break;

            } catch (IllegalArgumentException e) {
                // Catch the exception and print an error message
                System.out.println(e.getMessage());
            }
        }

        // Calculate the square of the given number to see how far to check for factors
        int square = num * num;

        // Print the factors of the number up to its square
        System.out.println("Factors of " + num + " up to its square (" + square + "): ");

        // Go through integers from 1 to the square of the given number
        for (int i = 1; i <= square; i++) {
            // Check if i is a factor of the number
            if (num % i == 0) {
                System.out.println(i + ", ");
            }
        }

        // Close the scanner once the loop is done
        scanner.close();
    }
}
