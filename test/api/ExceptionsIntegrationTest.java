package api;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import project.annotations.UserComputeAPIPrototype;
import project.annotations.ValidationException;

public class ExceptionsIntegrationTest {
    
    @Test
    public void testExceptionHandlingAcrossComponents() {
        UserComputeAPIPrototype api = new UserComputeAPIPrototype();
        
        try {
            // Configure with invalid path to trigger exception
            api.setInputSource("nonexistent/file.txt");
            api.setOutputDestination("output.txt");
            
            // Verify exception is caught and converted to error message
            String result = api.processRequest();
            assertTrue(result.startsWith("ERROR:"));
            assertFalse("Exception should not propagate", result.contains("ValidationException"));
        } catch (ValidationException e) {
            fail("ValidationException should be handled internally: " + e.getMessage());
        }
    }
}