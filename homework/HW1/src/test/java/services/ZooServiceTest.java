package services;

import entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class ZooServiceTest {
    private ZooService zooService;

    @BeforeEach
    void setUp() {
        zooService = new ZooService();
    }

    @Test
    void testAddHealthyAnimal() {
        Rabbit rabbit = new Rabbit(1, "Тестовый кролик", 2, true, 8, "Белый");
        boolean result = zooService.addAnimal(rabbit);

        assertTrue(result);
        assertEquals(1, zooService.getAnimals().size());
        assertEquals("Тестовый кролик", zooService.getAnimals().get(0).getName());
    }

    @Test
    void testAddSickAnimal() {
        Rabbit rabbit = new Rabbit(1, "Больной кролик", 2, false, 8, "Белый");
        boolean result = zooService.addAnimal(rabbit);

        assertFalse(result);
        assertEquals(0, zooService.getAnimals().size());
    }

    @Test
    void testAddMultipleAnimals() {
        zooService.addAnimal(new Rabbit(1, "Кролик1", 2, true, 8, "Белый"));
        zooService.addAnimal(new Tiger(2, "Тигр1", 10, true, 10));
        zooService.addAnimal(new Wolf(3, "Волк1", 8, true, "Альфа"));

        assertEquals(3, zooService.getAnimals().size());
    }

    @Test
    void testTotalFoodAmount() {
        zooService.addAnimal(new Rabbit(1, "Кролик1", 2, true, 8, "Белый"));
        zooService.addAnimal(new Tiger(2, "Тигр1", 10, true, 10));
        zooService.addAnimal(new Monkey(3, "Обезьяна1", 3, true, 9, 7));

        int totalFood = zooService.getTotalFoodAmount();
        assertEquals(15, totalFood);
    }

    @Test
    void testTotalFoodWithSickAnimals() {
        zooService.addAnimal(new Rabbit(1, "Здоровый кролик", 2, true, 8, "Белый"));
        zooService.addAnimal(new Rabbit(2, "Больной кролик", 3, false, 8, "Черный"));
        zooService.addAnimal(new Tiger(3, "Здоровый тигр", 10, true, 10));

        int totalFood = zooService.getTotalFoodAmount();
        assertEquals(12, totalFood);
    }

    @Test
    void testTotalFoodEmptyZoo() {
        int totalFood = zooService.getTotalFoodAmount();
        assertEquals(0, totalFood);
    }

    @Test
    void testContactZooAnimals() {
        zooService.addAnimal(new Rabbit(1, "Контактный кролик", 2, true, 8, "Белый"));
        zooService.addAnimal(new Rabbit(2, "Неконтактный кролик", 2, true, 3, "Черный"));
        zooService.addAnimal(new Monkey(3, "Контактная обезьяна", 3, true, 9, 8));
        zooService.addAnimal(new Monkey(4, "Неконтактная обезьяна", 3, true, 4, 8));
        zooService.addAnimal(new Tiger(5, "Тигр", 10, true, 10));
        zooService.addAnimal(new Wolf(6, "Волк", 8, true, "Стая"));

        List<Animal> contactAnimals = zooService.getAnimalsForContactZoo();
        assertEquals(2, contactAnimals.size());
        assertTrue(contactAnimals.stream().anyMatch(a -> a.getName().equals("Контактный кролик")));
        assertTrue(contactAnimals.stream().anyMatch(a -> a.getName().equals("Контактная обезьяна")));
        assertFalse(contactAnimals.stream().anyMatch(a -> a.getName().equals("Тигр")));
    }

    @Test
    void testContactZooWithSickAnimals() {
        zooService.addAnimal(new Rabbit(1, "Здоровый добрый", 2, true, 8, "Белый"));
        zooService.addAnimal(new Rabbit(2, "Больной добрый", 2, false, 8, "Черный"));
        zooService.addAnimal(new Rabbit(3, "Здоровый злой", 2, true, 3, "Серый"));

        List<Animal> contactAnimals = zooService.getAnimalsForContactZoo();
        assertEquals(1, contactAnimals.size());
        assertEquals("Здоровый добрый", contactAnimals.get(0).getName());
    }

    @Test
    void testContactZooEmpty() {
        List<Animal> contactAnimals = zooService.getAnimalsForContactZoo();
        assertTrue(contactAnimals.isEmpty());
    }

    @Test
    void testAnimalWithMinimalKindness() {
        Rabbit rabbit = new Rabbit(1, "Пограничный кролик", 2, true, 6, "Белый");
        zooService.addAnimal(rabbit);

        List<Animal> contactAnimals = zooService.getAnimalsForContactZoo();
        assertEquals(1, contactAnimals.size());
    }

    @Test
    void testDifferentAnimalTypes() {
        Rabbit rabbit = new Rabbit(1, "Кролик", 2, true, 8, "Синий");
        Tiger tiger = new Tiger(2, "Тигр", 10, true, 15);
        Wolf wolf = new Wolf(3, "Волк", 8, true, "Альфа");
        Monkey monkey = new Monkey(4, "Обезьяна", 3, true, 9, 8);

        zooService.addAnimal(rabbit);
        zooService.addAnimal(tiger);
        zooService.addAnimal(wolf);
        zooService.addAnimal(monkey);

        assertEquals(4, zooService.getAnimals().size());

        Animal addedRabbit = zooService.getAnimals().get(0);
        assertTrue(addedRabbit instanceof Rabbit);
        assertEquals("Синий", ((Rabbit) addedRabbit).getFurColor());
    }
}