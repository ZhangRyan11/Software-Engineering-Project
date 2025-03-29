package project.annotations;

import java.io.File;
import java.io.PrintWriter;

public class MyNetworkAPIImpl implements MyNetworkAPI {
    @Override
    public void doTask(String outputPath) {
        // Example implementation: write a message to the output file
        try (PrintWriter writer = new PrintWriter(new File(outputPath))) {
            writer.println("Task completed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
