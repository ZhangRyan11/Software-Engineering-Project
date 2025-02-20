package api;
import java.util.Scanner;

public interface UserInterfaceAPI{
	// Method to get a user input integer 
    public static int getUserInput(Scanner scanner) {
        
        // Run the code for the method that gets the user's input and returns "number"
        int num = getUserInput(scanner);
        
        // Prompt for input until a valid value is entered
        while (true) {
            try {
                // Ask the user for an integer and save it under the variable number (override 0)
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
	
}