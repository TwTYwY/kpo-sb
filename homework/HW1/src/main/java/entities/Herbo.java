package entities;

public abstract class Herbo extends Animal {
    protected int kindnessLevel;

    public Herbo(int inventoryNumber, String name, int foodAmount, boolean isHealthy, int kindnessLevel) {
        super(inventoryNumber, name, foodAmount, isHealthy);
        this.kindnessLevel = kindnessLevel;
    }

    public int getKindnessLevel() {
        return kindnessLevel;
    }

    @Override
    public boolean canBeInContactZoo() {
        return isHealthy() && kindnessLevel > 5;
    }
}