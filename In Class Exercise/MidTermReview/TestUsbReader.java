import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TestUsbReader {

    @Test
    public void testExceptionHandling() throws IOException {
        UsbDrive drive = Mockito.mock(UsbDrive.class);
        when(drive.fullyRead()).thenReturn(false);
        when(drive.readChar()).thenThrow(new IOException());
        
        UsbDriveReader reader = new UsbDriveReader(drive);
        
        // CHeck that read doesn't throw
//        Assertions.assertThrows(IOException.class, 
//                () -> reader.readData());
        reader.readData();
    }
}
