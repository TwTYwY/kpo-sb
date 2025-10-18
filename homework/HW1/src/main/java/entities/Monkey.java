package entities;

public class Monkey extends Herbo {
    private int intelligenceLevel;

    public Monkey(int inventoryNumber, String name, int foodAmount, boolean isHealthy, int kindnessLevel, int intelligenceLevel) {
        super(inventoryNumber, name, foodAmount, isHealthy, kindnessLevel);
        this.intelligenceLevel = intelligenceLevel;
    }

    public int getIntelligenceLevel() {
        return intelligenceLevel;
    }
}