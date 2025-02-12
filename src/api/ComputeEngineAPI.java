package api;
import java.util.Scanner;

public class ComputeEngineAPI implements UserInterfaceAPI{
    public static void main(String[] args) {
        // Create scanner object to read input from user
        Scanner scanner = new Scanner(System.in);
        
        // Get number from UserInterfaceAPI
        int num = UserInterfaceAPI.getUserInput(scanner); 


        // Run the code for the method that calculates the square of number variable 
        int square = calculateSquare(num);

        // Run the code for the method that finds factors up to the square of number variable
        findFactors(num, square);

        // Run the code for the method to close scanner once all else is done
        closeResources(scanner);
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
                System.out.print(i + " ");
            }
        }
    }

    // Method to close Scanner
    public static void closeResources(Scanner scanner) {
        // Close the scanner once everything is done
        scanner.close();
    }
}
