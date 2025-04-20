package api;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class OptimizedFileDataStorage implements DataStorage {
    // Optimized buffer size (8MB)
    private static final int BUFFER_SIZE = 8 * 1024 * 1024;
    
    @Override
    public String readData(String source, String[] delimiters) throws IOException {
        if (source == null || source.trim().isEmpty()) {
            throw new IllegalArgumentException("Source path cannot be null or empty");
        }
        
        Path path = Paths.get(source);
        if (!Files.exists(path)) {
            throw new IOException("Source file does not exist: " + source);
        }
        
        // For read operations, Files.readString is already highly optimized
        // We'll use it directly which utilizes internal optimizations in JDK
        return Files.readString(path);
    }

    @Override
    public void writeData(String destination, String data) throws IOException {
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination path cannot be null or empty");
        }
        
        if (data == null) {
            data = ""; // Handle null data gracefully
        }
        
        Path path = Paths.get(destination);
        // Ensure parent directories exist
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        
        // Convert to bytes once, outside any loops
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        
        // For better write performance, use direct NIO channel
        try (FileChannel channel = FileChannel.open(path, 
                StandardOpenOption.WRITE, 
                StandardOpenOption.CREATE, 
                StandardOpenOption.TRUNCATE_EXISTING)) {
            
            // Use direct ByteBuffer for better performance
            ByteBuffer buffer = ByteBuffer.allocateDirect(
                    Math.min(BUFFER_SIZE, bytes.length));
            
            int position = 0;
            while (position < bytes.length) {
                int remaining = bytes.length - position;
                int chunkSize = Math.min(buffer.capacity(), remaining);
                
                buffer.clear();
                buffer.put(bytes, position, chunkSize);
                buffer.flip();
                
                // Write chunk
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
                
                position += chunkSize;
            }
        }
    }
}
