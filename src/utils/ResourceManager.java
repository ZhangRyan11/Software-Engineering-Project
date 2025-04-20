package utils;

import java.io.Closeable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for properly managing resource closing.
 * This helps prevent file handle leaks that can cause build issues.
 */
public class ResourceManager {
    private static final Logger logger = Logger.getLogger(ResourceManager.class.getName());
    
    /**
     * Safely closes a resource, logging any exceptions rather than throwing them.
     * 
     * @param resource The resource to close
     * @param resourceName Name of the resource for logging purposes
     */
    public static void closeQuietly(Closeable resource, String resourceName) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to close " + resourceName, e);
            }
        }
    }
    
    /**
     * Safely closes multiple resources in order.
     * 
     * @param resources Array of resources to close
     */
    public static void closeAllQuietly(Closeable... resources) {
        if (resources != null) {
            for (int i = 0; i < resources.length; i++) {
                closeQuietly(resources[i], "Resource #" + i);
            }
        }
    }
    
    /**
     * Utility method to ensure a stream is properly closed in a try-with-resources block.
     * 
     * @param <T> The type of closeable resource
     * @param <R> The return type of the operation
     * @param resource The resource to use
     * @param operation The operation to perform on the resource
     * @return The result of the operation
     * @throws Exception if an error occurs during the operation
     */
    public static <T extends Closeable, R> R withResource(T resource, ResourceOperation<T, R> operation) throws Exception {
        try (T res = resource) {
            return operation.apply(res);
        }
    }
    
    /**
     * Functional interface for operations on resources.
     */
    @FunctionalInterface
    public interface ResourceOperation<T extends Closeable, R> {
        R apply(T resource) throws Exception;
    }
}
