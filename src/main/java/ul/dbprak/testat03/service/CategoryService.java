package ul.dbprak.testat03.service;

import org.springframework.stereotype.Service;
import ul.dbprak.testat03.repository.CategoryRepository;
import ul.dbprak.testat03.repository.model.Category;
import ul.dbprak.testat03.repository.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getCategoryTree() {
        List<Category> rootCategories = categoryRepository.findAllByParentCategoryIsNull();
        for (Category category : rootCategories) {
            addSubcategories(category);
        }
        return rootCategories;
    }

    private void addSubcategories(Category category) {
        List<Category> subcategories = categoryRepository.findAllByParentCategory(category.getId());
        category.setSubCategories(subcategories);
        for (Category subcategory : subcategories) {
            addSubcategories(subcategory);
        }
    }

    public List<Product> getProductsByCategoryPath(String path) {

        if (path == null || path.isEmpty()) {
            return Collections.emptyList();
        }

        List<Category> rootTree = getCategoryTree();
        List<Product> productList = new ArrayList<>();

        if (path.equals("/")) {
            addProductsFromSub(rootTree, productList);
            return productList;
        }

        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String[] categoryNames = path.split("/");

        if (categoryNames.length == 0) {
            return Collections.emptyList();
        }

        Category catCurrent = null;

        for (String name : categoryNames) {
            catCurrent = matchNameToCategory(name, rootTree);
            if (catCurrent != null) {
                rootTree = catCurrent.getSubCategories();
            } else {
                return Collections.emptyList();
            }
        }

        productList = catCurrent.getProducts();
        addProductsFromSub(rootTree, productList);

        return productList;
    }

    private Category matchNameToCategory(String name, List<Category> categories) {
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }

    private void addProductsFromSub(List<Category> categories, List<Product> products) {
        for(Category category : categories) {
            products.addAll(category.getProducts());
            addProductsFromSub(category.getSubCategories(), products);
        }
    }

}
