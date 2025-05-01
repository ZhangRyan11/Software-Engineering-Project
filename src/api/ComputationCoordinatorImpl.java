package api;

import java.util.List;
import java.util.stream.Collectors;

public class ComputationCoordinatorImpl implements ComputationCoordinator {
    private final ComputationAPI computeEngine;
    private final DataStorage dataStorage;

    public ComputationCoordinatorImpl(ComputationAPI computeEngine, DataStorage dataStorage) {
        this.computeEngine = computeEngine;
        this.dataStorage = dataStorage;
    }

    @Override
    public ComputeResult compute(ComputeRequest request) {
        try {
            // Read input data
            String inputData = dataStorage.readData(request.getSourcePath(), request.getDelimiters());
            
            // Perform computation
            ComputationResult result = computeEngine.compute(inputData, request.getDelimiters());
            
            // Write results if computation was successful
            if (result.isSuccess()) {
                String outputData = formatOutput(result.getFactors());
                dataStorage.writeData(request.getDestinationPath(), outputData);
                return new ComputeResponseImpl(true, null);
            }
            
            return new ComputeResponseImpl(false, "Computation failed: " + result.getMessage());
        } catch (Exception e) {
            return new ComputeResponseImpl(false, e.getMessage());
        }
    }
    
    private String formatOutput(List<Integer> factors) {
        if (factors == null || factors.isEmpty()) {
            return "No factors found";
        }
        return "Factors: " + factors.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
