package hse.bank.facade;

import hse.bank.domain.Category;
import hse.bank.factories.DomainFactory;

import java.util.*;

public class CategoryFacade {
    private final Map<UUID, Category> categories = new HashMap<>();
    private final DomainFactory factory;

    public CategoryFacade(DomainFactory factory) {
        this.factory = factory;
    }

    public Category createCategory(Category.Type type, String name) {
        Category category = factory.createCategory(type, name);
        categories.put(category.getId(), category);
        return category;
    }

    public void updateCategory(UUID id, String name) {
        Category category = categories.get(id);
        if (category != null) {
            category.setName(name);
        }
    }

    public void deleteCategory(UUID id) {
        categories.remove(id);
    }

    public Category getCategory(UUID id) {
        return categories.get(id);
    }

    public List<Category> getAllCategories() {
        return new ArrayList<>(categories.values());
    }

    public List<Category> getCategoriesByType(Category.Type type) {
        return categories.values().stream()
                .filter(category -> category.getType() == type)
                .toList();
    }
}