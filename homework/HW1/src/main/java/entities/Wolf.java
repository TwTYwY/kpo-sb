package entities;

public class Wolf extends Predator {
    private String packName;

    public Wolf(int inventoryNumber, String name, int foodAmount, boolean isHealthy, String packName) {
        super(inventoryNumber, name, foodAmount, isHealthy);
        this.packName = packName;
    }

    public String getPackName() {
        return packName;
    }
}