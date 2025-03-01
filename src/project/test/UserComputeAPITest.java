package project.test;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import project.annotations.UserComputeAPIPrototype;
import project.exceptions.ValidationException;
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
    @Test(expected = ValidationException.class)
    public void testNullInputSource() {
        api.setInputSource(null);
    }
    
     // Test case to verify that setting an empty or whitespace-only input source
    @Test(expected = ValidationException.class)
    public void testEmptyInputSource() {
        api.setInputSource("   ");
    }
}