package project.test;

import org.junit.Test;


public class IntegrationTest {
    
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