package api;
import java.util.Scanner;

public interface UserInterfaceAPITest {
    // Method to get a user input integer 
    public static int getUserInput(Scanner scanner) {
        int num; // No recursive call

        
        while (true) {
            try {
                // Ask the user for an integer
                System.out.print("Enter an integer greater than 0: ");
                num = scanner.nextInt();

                // If the number is 0 or less, throw an exception
                if (num <= 0) {
                    throw new IllegalArgumentException("Invalid value. Integer must be greater than 0.");
                }
                
                break; // Exit loop when valid input is given
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
