package services;
import entities.Animal;
import entities.Thing;
import interfaces.IInventory;
import java.util.*;

public class ZooService {
    private List<Animal> animals;
    private List<Thing> things;

    public ZooService() {
        this.animals = new ArrayList<>();
        this.things = new ArrayList<>();
    }

    private boolean checkAnimalHealth(Animal animal) {
        return animal.isHealthy();
    }

    public boolean addAnimal(Animal animal) {
        if (checkAnimalHealth(animal)) {
            animals.add(animal);
            return true;
        }
        return false;
    }

    public void addThing(Thing thing) {
        things.add(thing);
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(animals);
    }

    public int getTotalFoodAmount() {
        int total = 0;
        for (Animal animal : animals) {
            total += animal.getFoodAmount();
        }
        return total;
    }

    public List<Animal> getAnimalsForContactZoo() {
        List<Animal> contactAnimals = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.canBeInContactZoo()) {
                contactAnimals.add(animal);
            }
        }
        return contactAnimals;
    }

    public List<IInventory> getAllInventory() {
        List<IInventory> allItems = new ArrayList<>();
        allItems.addAll(animals);
        allItems.addAll(things);
        return allItems;
    }
}