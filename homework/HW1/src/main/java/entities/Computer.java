package entities;

public class Computer extends Thing {
    private String processor;

    public Computer(int inventoryNumber, String name, String processor) {
        super(inventoryNumber, name);
        this.processor = processor;
    }

    public String getProcessor() {
        return processor;
    }
}