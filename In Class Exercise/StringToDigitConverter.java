import java.util.*;

public class StringToDigitConverter {

    public static int convertStringToDigit(String input) throws IllegalArgumentException {
        if (input == null || input.length() != 1) {
            throw new IllegalArgumentException("Invalid input: must be a single character.");
        }
        
        char ch = input.charAt(0);
        if (!Character.isDigit(ch)) {
            throw new IllegalArgumentException("Invalid input: character is not a digit.");
        }
        
        return ch - '0';  // Convert the char digit to its integer value
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a single digit (0-9). Type 'exit' to quit.");
        
        while (true) {
            System.out.print("Input: ");
            String input = scanner.nextLine().trim();
            
            // Allow the user to exit the program gracefully.
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting program.");
                break;
            }
            
            try {
                int digit = convertStringToDigit(input);
                System.out.println("Converted number: " + digit);
            } catch (IllegalArgumentException e) {
                // Handle invalid input gracefully.
                System.out.println(e.getMessage());
            } catch (Exception e) {
                // Catch any unforeseen exceptions to ensure the main method never exits abnormally.
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
}
