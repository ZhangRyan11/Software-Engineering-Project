package api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import project.annotations.UserComputeAPIPrototype;


public class ExceptionsIntegrationTest {
    
    @Test
    public void testExceptionHandlingAcrossComponents() {
        UserComputeAPIPrototype api = new UserComputeAPIPrototype();
        
        // Configure with invalid path to trigger exception
        api.setInputSource("nonexistent/file.txt");
        api.setOutputDestination("output.txt");
        
        // Verify exception is caught and converted to error message
        String result = api.processRequest();
        assertTrue(result.startsWith("ERROR:"));
        assertFalse("Exception should not propagate", result.contains("ValidationException"));
    }
}