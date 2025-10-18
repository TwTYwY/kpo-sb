package entities;

public abstract class Predator extends Animal {

    public Predator(int inventoryNumber, String name, int foodAmount, boolean isHealthy) {
        super(inventoryNumber, name, foodAmount, isHealthy);
    }

    @Override
    public boolean canBeInContactZoo() {
        return false;
    }
}