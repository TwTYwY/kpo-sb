package entities;
import interfaces.IInventory;

public class Thing implements IInventory {
    protected int inventoryNumber;
    protected String name;

    public Thing(int inventoryNumber, String name) {
        this.inventoryNumber = inventoryNumber;
        this.name = name;
    }

    public int getInventoryNumber() {
        return inventoryNumber;
    }
    public String getName() {
        return name;
    }
}