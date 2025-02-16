package api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class StorageAPITest {

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
        StorageResponse mockResponse = mock(StorageResponse.class);
        when(storageAPI.readData(mockRequest)).thenReturn(mockResponse);

        // Act
        storageAPI.readData(mockRequest);

        // Assert
        verify(storageAPI, times(1)).readData(mockRequest);
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
