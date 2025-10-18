package entities;

public class Rabbit extends Herbo {
    private String furColor;

    public Rabbit(int inventoryNumber, String name, int foodAmount, boolean isHealthy, int kindnessLevel, String furColor) {
        super(inventoryNumber, name, foodAmount, isHealthy, kindnessLevel);
        this.furColor = furColor;
    }

    public String getFurColor() {
        return furColor;
    }
}