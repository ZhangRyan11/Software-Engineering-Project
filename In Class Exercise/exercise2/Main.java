package exercise2;

public class Main {
    public static void main(String[] args) {
        PettingZoo zoo = new PettingZoo();
        zoo.addAnimalToZoo(new Goat());
        zoo.addAnimalToZoo(new HoneyBadger());
        
        zoo.visitZoo();
    }
}
