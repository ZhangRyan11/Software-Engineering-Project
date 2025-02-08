import java.util.Scanner;

public class IntegerFactors {
    public static void main(String[] args) {
        // Create scanner object to read input from user
        Scanner scanner = new Scanner(System.in);

        // Ask the user for an integer and save it under a variable 
        System.out.print("Enter an integer: ");
        int num = scanner.nextInt();
        
        //Check that user gave a value greater than 0
        if(num =< 0){
            System.out.print("Enter an int value greater than 0: ");
            //User fails check, get a new value
            int num = scanner.nextInt();
        }
            
        //User passes check, get factors 
        else{

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
        }

        // Close the scanner once the loop is done
        scanner.close();
    }
}
