import java.io.IOException;

public class UsbDriveReader {
   private final UsbDrive drive;
  
   public UsbDriveReader(UsbDrive drive) {
       this.drive = drive;
   }
   public String readData() {
       String result = "";
       while (!drive.fullyRead() && checkTestingHook()) {
           try {
               result += drive.readChar();
           } catch (IOException e) {
               // we've read as much as we can, 
               // break out of the loop
               break;
           }
       }
       return result;
   }
   
   protected boolean checkTestingHook() {
       return true;
   }
}
