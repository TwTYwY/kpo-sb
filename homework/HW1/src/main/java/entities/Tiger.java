package entities;

public class Tiger extends Predator {
    private int stripeCount;

    public Tiger(int inventoryNumber, String name, int foodAmount, boolean isHealthy, int stripeCount) {
        super(inventoryNumber, name, foodAmount, isHealthy);
        this.stripeCount = stripeCount;
    }

    public int getStripeCount() {
        return stripeCount;
    }
}