package exercise2;

public class FriendlyAnimalWrapper implements Animal {
    private Animal animal;

    public FriendlyAnimalWrapper(Animal animal) {
        this.animal = animal;
    }

    @Override
    public boolean tryToPet() {
        if (!animal.isFriendly()) {
            System.out.println("You cannot pet an unfriendly animal!");
            return false;
        }
        // Only delegate if the animal is friendly.
        return animal.tryToPet();
    }

    @Override
    public boolean isFriendly() {
        // Delegate the friendly check to the wrapped animal.
        return animal.isFriendly();
    }
}
