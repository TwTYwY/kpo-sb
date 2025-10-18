package entities;

public class Table extends Thing {
    private String material;

    public Table(int inventoryNumber, String name, String material) {
        super(inventoryNumber, name);
        this.material = material;
    }

    public String getMaterial() {
        return material;
    }
}