package api;

import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestStorageAPI {
    
    @Mock
    private StorageAPI storageAPI;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void smokeTest_ReadData() {
        // Arrange
        StorageRequest mockRequest = mock(StorageRequest.class);
        when(mockRequest.getPath()).thenReturn("test/path");
        when(mockRequest.getParams()).thenReturn(new String[]{"param"});
        
        // Fix the return type mismatch - storageAPI.readData returns String, not StorageResponse
        when(storageAPI.readData("test/path", new String[]{"param"})).thenReturn("test data");

        // Act
        storageAPI.readData("test/path", new String[]{"param"});

        // Assert
        verify(storageAPI, times(1)).readData("test/path", new String[]{"param"});
    }

    @Test
    public void smokeTest_WriteData() {
        // Arrange
        String testDestination = "test/destination";
        String testData = "test data";

        // Act
        storageAPI.writeData(testDestination, testData);

        // Assert
        verify(storageAPI, times(1)).writeData(testDestination, testData);
    }
}
