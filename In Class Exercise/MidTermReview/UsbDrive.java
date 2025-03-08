import java.io.IOException;

public interface UsbDrive {
   boolean fullyRead();
   char readChar() throws IOException;
}

