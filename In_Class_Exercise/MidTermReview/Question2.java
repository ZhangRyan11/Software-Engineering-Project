public class MemoryMystery {
	
	public static void main(String[] args) {
		Person shaggy = new Person();
		addPet(shaggy);
		
		private static void addpet(Person person) {
			Dog scoobyDoo = new Dog("Scooby dooby doo");
			person.addDog(scoobyDoo);
		}
	}
}

public class Person {
	
	private Dog petDog = null;
	
	public void addDog(Dog dog) {
		petDog = dog;
	}
}

public class Dog {
	private String name;
	
	public Dog(String dogName) {
		name = dogName;
	}
}