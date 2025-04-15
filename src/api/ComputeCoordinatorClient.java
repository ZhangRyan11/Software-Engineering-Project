package grpc.example;


import java.util.concurrent.TimeUnit;

import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import phoneorder.PhoneOrder.PhoneOrderRequest;
import phoneorder.PhoneOrder.PhoneOrderResponse;
import phoneorder.PhoneOrderServiceGrpc;
import phoneorder.PhoneOrderServiceGrpc.PhoneOrderServiceBlockingStub;

public class ComputeCoordinatorClient { // Boilerplate TODO: change to <servicename>Client
    private final PhoneOrderServiceBlockingStub blockingStub; // Boilerplate TODO: update to appropriate blocking stub

    public PhoneOrderClient(Channel channel) {
        blockingStub = PhoneOrderServiceGrpc.newBlockingStub(channel);  // Boilerplate TODO: update to appropriate blocking stub
    }

    // Boilerplate TODO: replace this method with actual client call/response logic
    public void order() {        
        PhoneOrderRequest request = PhoneOrderRequest.newBuilder().setModel("android").setIncludeWarranty(true).build();
        PhoneOrderResponse response;
        try {
            response = blockingStub.createPhoneOrder(request);
        } catch (StatusRuntimeException e) {
            e.printStackTrace();
            return;
        }
        if (response.hasErrorMessage()) {
            System.err.println("Error: " + response.getErrorMessage());
        } else {
            System.out.println("Order number: " + response.getOrderNumber());
        }
    }

    public static void main(String[] args) throws Exception {
        String target = "localhost:50051";  // Boilerplate TODO: make sure the server/port match the server/port you want to connect to

        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
                .build();
        try {
            PhoneOrderClient client = new PhoneOrderClient(channel); // Boilerplate TODO: update to this class name
            client.order();
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
