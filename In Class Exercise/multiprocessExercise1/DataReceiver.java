package multiprocessExercise1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DataReceiver {

	public static void main(String[] args) throws Exception {

		File file = new File(DataSender.SHARED_FILE_NAME);
		while (!file.exists()) {
			Thread.sleep(100);
		}

		// clean up our data when we're done
		file.deleteOnExit();

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			readFileContents(reader);
		}
	}

	private static void readFileContents(BufferedReader reader) throws IOException {
		 String line = reader.readLine();
		  		        System.out.println(line);
		    }
	}

