// File: Box.java
public class Box implements Sizeable {
    private int size;

    public Box(int size) {
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }
    
    @Override
    public String toString() {
        return "Box(size=" + size + ")";
    }
}
