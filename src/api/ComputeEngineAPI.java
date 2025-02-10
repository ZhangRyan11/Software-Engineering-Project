package project.api;
import java.util.Scanner;

public class ComputeEngineAPI {
    public static void main(String[] args) {
        // Create scanner object to read input from user
        Scanner scanner = new Scanner(System.in);

        // Run the code for the method that gets the user's input and returns "num"
        int num = getUserInput(scanner);

        // Run the code for the method that calculates the square of "num"
        int square = calculateSquare(num);

        // Run the code for the method that finds factors up to the square of "num"
        findFactors(num, square);

        // Run the code for the method to close scanner once all else is done
        closeResources(scanner);
    }

    // Method to get a user input int 
    public static int getUserInput(Scanner scanner) {
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
            }   
            catch (IllegalArgumentException e) {
                // Catch the exception and print an error message
                System.out.println(e.getMessage());
            }
        }
        return num; 
    }

    // Method to calculate the square of the number
    public static int calculateSquare(int num) {
        // Calculate the square of the given number to see how far to check for factors
        return num * num;
    }

    // Method to find and print factors of the number up to its square
    public static void findFactors(int num, int square) {
        // Print the factors of the number up to its square
        System.out.println("Factors of " + num + " up to its square (" + square + "): ");

        // Go through integers from 1 to the square of the given number
        for (int i = 1; i <= square; i++) {
            // Check if i is a factor of the number
            if (num % i == 0) {
                System.out.println(i + ", ");
            }
        }
    }

    // Method to close Scanner
    public static void closeResources(Scanner scanner) {
        // Close the scanner once everything is done
        scanner.close();
    }
}
