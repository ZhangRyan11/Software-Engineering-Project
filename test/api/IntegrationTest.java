package api;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntegrationTest {
    private InMemoryStorageAPI storage;

    @Before
    public void setUp() {
        storage = new InMemoryStorageAPI();
    }

    @Test
    public void testInMemoryStorage() {
        // Setup test data
        storage.getInputConfig().addInput(1);
        storage.getInputConfig().addInput(2);
        storage.getInputConfig().addInput(3);

        // Perform storage operations
        StorageResponse response = storage.readData(storage.getInputConfig());
        storage.writeData("memory", "Test Output 1");
        storage.writeData("memory", "Test Output 2");

        // Verify results
        assertEquals(2, storage.getOutputConfig().getOutputData().size());
        assertEquals("Test Output 1", storage.getOutputConfig().getOutputData().get(0));
        assertEquals("Test Output 2", storage.getOutputConfig().getOutputData().get(1));
    }
}
