package exercise1;

import java.util.ArrayList;
import java.util.List;

public class TrainBuilder {
    private final List<Person> passengers = new ArrayList<>();
    private boolean allAboardCalled = false;

 
    public boolean boardTrain(Person person) {
        if (allAboardCalled) {
            // No one can board after allAboard() is called.
            return false;
        }
        if (!person.hasTicket()) {
            return false;
        }
        passengers.add(person);
        return true;
    }
    
  
    public Train allAboard() {
        allAboardCalled = true;
        // Pass a copy of the passengers list to ensure immutability.
        return new Train(new ArrayList<>(passengers));
    }
}
