package api;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class OptimizedFileDataStorage implements DataStorage, StorageAPI {
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
    public void writeDataContent(String destination, String data) throws IOException {
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
    
    /**
     * Implements the StorageAPI interface writeData method.
     * 
     * @param destination Path to write data to
     * @param data Data to write
     * @return true if successful, false otherwise
     */
    @Override
    public boolean writeData(String destination, String data) {
        try {
            writeDataContent(destination, data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Implements the StorageAPI interface readData method.
     *
     * @param request StorageRequest containing the source and delimiter
     * @return StorageResponse with the read data
     */
    @Override
    public StorageResponse readData(StorageRequest request) {
        // Simplified implementation - adapt as needed based on your actual requirements
        try {
            String content = readData(request.getSource(), new String[]{request.getDelimiter()});
            return new StorageResponseImpl(parse(content, request.getDelimiter()), true);
        } catch (IOException e) {
            return new StorageResponseImpl(java.util.Collections.emptyList(), false);
        }
    }
    
    private java.util.List<Integer> parse(String content, String delimiter) {
        java.util.List<Integer> numbers = new java.util.ArrayList<>();
        String[] values = delimiter.isEmpty() ? new String[]{content} : content.split(delimiter);
        
        for (String value : values) {
            if (!value.trim().isEmpty()) {
                try {
                    numbers.add(Integer.parseInt(value.trim()));
                } catch (NumberFormatException e) {
                    // Skip non-integer values
                }
            }
        }
        return numbers;
    }
}
