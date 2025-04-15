package api;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import coordinatorservice.ComputationCoordinatorGrpc;
import coordinatorservice.ComputationCoordinatorGrpc.ComputationCoordinatorBlockingStub;
import coordinatorservice.CoordinatorServiceProto.ComputationResponse;
import coordinatorservice.CoordinatorServiceProto.NumberListRequest;
import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

public class ComputeCoordinatorClient {
    private final ComputationCoordinatorBlockingStub blockingStub;

    public ComputeCoordinatorClient(Channel channel) {
        blockingStub = ComputationCoordinatorGrpc.newBlockingStub(channel);
    }

    public void submitNumberList(double[] numbers, String outputFile, String delimiter) {
        NumberListRequest request = NumberListRequest.newBuilder()
            .addAllNumbers(Arrays.stream(numbers).boxed().collect(Collectors.toList()))
            .setOutputFile(outputFile)
            .setDelimiter(delimiter)
            .build();
            
        ComputationResponse response;
        try {
            response = blockingStub.submitNumberList(request);
            if (response.getSuccess()) {
                System.out.println("Job submitted successfully. Job ID: " + response.getJobId());
            } else {
                System.err.println("Error: " + response.getMessage());
            }
        } catch (StatusRuntimeException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        String target = "localhost:50052";

        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
                .build();
        try {
            ComputeCoordinatorClient client = new ComputeCoordinatorClient(channel);
            // Example usage
            client.submitNumberList(new double[]{1.0, 2.0, 3.0}, "output.txt", ",");
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
