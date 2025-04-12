

// No peeking! Don't look in here yet
public class MysteryClass {

    private int data = 0;

    public void initialSetup() {
        int sum = 0;
        for (int i = 0; i < 1_000_000_000; i++) {
            sum+= Math.pow(3, i);
        }
    }

    public void doOneTask() {
        data++;
    }

}
