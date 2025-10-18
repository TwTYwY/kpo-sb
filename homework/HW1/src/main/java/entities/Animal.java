package entities;
import interfaces.IAlive;
import interfaces.IInventory;

public abstract class Animal implements IAlive, IInventory {
    protected int inventoryNumber;
    protected String name;
    protected int foodAmount;
    protected boolean isHealthy;

    public Animal(int inventoryNumber, String name, int foodAmount, boolean isHealthy) {
        this.inventoryNumber = inventoryNumber;
        this.name = name;
        this.foodAmount = foodAmount;
        this.isHealthy = isHealthy;
    }

    public int getInventoryNumber() {
        return inventoryNumber;
    }
    public String getName() {
        return name;
    }
    public int getFoodAmount() {
        return foodAmount;
    }
    public boolean isHealthy() {
        return isHealthy;
    }

    public abstract boolean canBeInContactZoo();
}