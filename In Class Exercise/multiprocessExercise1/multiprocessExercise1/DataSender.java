package multiprocessExercise1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataSender {
	
	public static final String SHARED_FILE_NAME = "multiprocess-exercise1.txt";

	public static void main(String[] args) throws IOException {
		
		// clear out any previous runs
		new File(SHARED_FILE_NAME).delete();
		
		String temporaryFile = "write-sender-data.tmp";
		
		try (FileWriter writer = new FileWriter(temporaryFile)) {
			writeFileContents(writer);
		}
		
		// Don't read the data until we're finished writing it
		new File(temporaryFile).renameTo(new File(SHARED_FILE_NAME));
		
	}

	private static void writeFileContents(FileWriter writer) throws IOException {
		// TODO: write some data to the writer
	}
}
