package exercise1;

import java.util.Collections;
import java.util.List;

public class Train {
    private final List<Person> passengers;

    // Package-private constructor: can only be created by the TrainBuilder.
    Train(List<Person> passengers) {
        this.passengers = passengers;
    }
    
    public List<Person> getPassengers() {
        // Return an unmodifiable view to preserve immutability.
        return Collections.unmodifiableList(passengers);
    }
    
    public int countPassengers() {
        return passengers.size();
    }
}
