package api;
import java.util.Scanner;

public interface UserInterfaceAPI {
    
    // Method to get a user input integer
    public static int getUserInput(Scanner scanner) {
        int num = 0; // Initialize to prevent uninitialized errors

        while (true) {
            try {
                System.out.print("Enter an integer greater than 0: ");
                
                // Read user input
                num = scanner.nextInt();

                // Ensure the input is greater than 0
                if (num <= 0) {
                    throw new IllegalArgumentException("Invalid value. Integer must be greater than 0.");
                }
                
                break; // Exit loop if valid input
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) { 
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next(); // Consume invalid input
            }
        }
        return num;
    }
}
