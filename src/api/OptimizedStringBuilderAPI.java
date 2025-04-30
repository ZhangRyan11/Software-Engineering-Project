package api;

import project.annotations.NetworkAPIPrototype;
import project.annotations.ValidationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@NetworkAPIPrototype
public class OptimizedStringBuilderAPI implements UserComputeAPI {
    private static final Map<String, String> STRING_CACHE = new ConcurrentHashMap<>(100);
    private static final Map<String, String> RESULT_CACHE = new ConcurrentHashMap<>(100);
    
    // Pre-allocate result string with estimated capacity
    private static final int ESTIMATED_RESULT_SIZE = 2048; 
    
    private String inputSource = "default_input";
    private String outputDestination = "default_output";
    private String delimiters = ",";

    @Override
    public void setInputSource(String source) throws ValidationException {
        if (source == null || source.trim().isEmpty()) {
            throw new ValidationException("Input source cannot be null or empty");
        }
        this.inputSource = STRING_CACHE.computeIfAbsent(source, String::intern);
    }

    @Override
    public void setOutputDestination(String destination) throws ValidationException {
        if (destination == null || destination.trim().isEmpty()) {
            throw new ValidationException("Output destination cannot be null or empty");
        }
        this.outputDestination = STRING_CACHE.computeIfAbsent(destination, String::intern);
    }

    @Override
    public void setDelimiters(String delimiters) {
        this.delimiters = STRING_CACHE.computeIfAbsent(delimiters != null ? delimiters : ",", String::intern);
    }

    @Override
    public String processRequest() {
        // Use cache key for efficiency
        String cacheKey = getCacheKey();
        
        // Check cache first to avoid recomputation
        return RESULT_CACHE.computeIfAbsent(cacheKey, k -> {
            // Pre-allocate StringBuilder with estimated size to avoid resizing
            StringBuilder result = new StringBuilder(ESTIMATED_RESULT_SIZE);
            
            // Single append for fixed strings instead of multiple appends
            result.append("Processing request for input: ")
                  .append(inputSource)
                  .append(", output: ")
                  .append(outputDestination)
                  .append(", delimiters: ")
                  .append(delimiters);
                  
            // Add additional content as needed
            for (int i = 0; i < 10; i++) {
                result.append("\nProcessing step ").append(i);
            }
            
            return result.toString();
        });
    }

    private String getCacheKey() {
        return STRING_CACHE.computeIfAbsent(
            inputSource + "|" + outputDestination + "|" + delimiters,
            String::intern
        );
    }
    
    // Flush cache for testing or when memory becomes limited
    public void flushCache() {
        RESULT_CACHE.clear();
    }
}
