package api;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import project.annotations.UserComputeAPIPrototype;

import java.io.File;
import java.io.IOException;

public class UserComputeAPITest {
    private UserComputeAPIPrototype api;
    private File testFile;
    
    //Initializes API instance and creates a temporary test file.
    @Before
    public void setup() throws IOException {
        api = new UserComputeAPIPrototype();
        testFile = File.createTempFile("test", ".txt");
        testFile.deleteOnExit();
    }

    //Test case to verify that setting a null input source throws ValidationException.
    @Test
    public void testNullInputSource() {
        assertThrows(ValidationException.class, () -> {
            api.setInputSource(null);
        });
    }
    
     //Test case to verify that setting an empty or whitespace-only input source
    @Test
    public void testEmptyInputSource() {
    	assertThrows(ValidationException.class, () -> {
            api.setInputSource("   ");
        });
    }

     //Test case to verify that setting a valid file path as input source is accepted.
    @Test
    public void testValidInputSource() {
        api.setInputSource(testFile.getAbsolutePath());
    }

    //Test case to verify error handling during processing
    @Test
    public void testProcessRequestErrorHandling(){
        String result = api.processRequest();
        assertTrue(result.startsWith("ERROR: "));
    }

}