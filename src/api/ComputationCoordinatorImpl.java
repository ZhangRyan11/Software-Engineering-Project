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
            if (result instanceof ComputationResultImpl && ((ComputationResultImpl) result).isSuccess()) {
                String outputData = formatOutput(((ComputationResultImpl) result).getFactors());
                dataStorage.writeData(request.getDestinationPath(), outputData);
                return new ComputeResponseImpl(true, null);
            }
            
            return new ComputeResponseImpl(false, "Computation failed");
        } catch (Exception e) {
            return new ComputeResponseImpl(false, e.getMessage());
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
