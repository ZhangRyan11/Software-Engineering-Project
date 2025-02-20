package exercise2;

import java.util.ArrayList;
import java.util.List;

public class PettingZoo {

	private List<Animal> animals = new ArrayList<>();
	
	public void addAnimalToZoo(Animal animal) {
		animals.add(animal);
	}
	
	public void visitZoo() {
		for (Animal animal : animals) {
			animal.tryToPet();
		}
	}
	
	public void visitAnimal(int animalIndex) {
		animals.get(animalIndex).tryToPet();
	}
	
	PettingZoo zoo = new PettingZoo();
	zoo.addAnimalToZoo(new FriendlyAnimalWrapper(new Goat()));
	zoo.addAnimalToZoo(new FriendlyAnimalWrapper(new HoneyBadger()));

}
