package api;

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
                return new ComputeResult(true, null);
            }
            
            return new ComputeResult(false, "Computation failed");
        } catch (Exception e) {
            return new ComputeResult(false, e.getMessage());
        }
    }

    private String formatOutput(Iterable<Integer> factors) {
        StringBuilder sb = new StringBuilder();
        for (Integer factor : factors) {
            sb.append(factor).append("\n");
        }
        return sb.toString();
    }
}
