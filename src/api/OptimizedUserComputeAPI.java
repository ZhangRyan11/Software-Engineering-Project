package api;

import project.annotations.NetworkAPIPrototype;
import project.annotations.ValidationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@NetworkAPIPrototype
public class OptimizedUserComputeAPI implements UserComputeAPI {
    private static final Map<String, String> STRING_CACHE = new ConcurrentHashMap<>(100);
    private static final Map<String, String> RESULT_CACHE = new ConcurrentHashMap<>(100);
    
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
        String cacheKey = getCacheKey();
        return RESULT_CACHE.computeIfAbsent(cacheKey, k -> {
            StringBuilder result = new StringBuilder(1024);
            // Fast processing without delays
            result.append("Processing request for ").append(inputSource);
            return result.toString();
        });
    }

    private String getCacheKey() {
        return STRING_CACHE.computeIfAbsent(
            inputSource + "|" + outputDestination + "|" + delimiters,
            String::intern
        );
    }
}
