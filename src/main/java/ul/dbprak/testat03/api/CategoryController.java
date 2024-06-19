package ul.dbprak.testat03.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import ul.dbprak.testat03.repository.model.Category;
import ul.dbprak.testat03.repository.model.Product;
import ul.dbprak.testat03.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "getCategoryTree"
            , description = "Diese Methode ermittelt den kompletten Kategorienbaum durch Rückgabe des Wurzelknotens. Jeder Knoten ist dabei vom Typ Category und kann eine Liste von Unterknoten (d.h. Unterkategorien) enthalten.")
    @GetMapping("/getCategoryTree")
    public List<Category> getCategoryTree() {
        return categoryService.getCategoryTree();

    }

    @Operation(summary = "getProductsByCategoryPath"
            , description = "Nach Angabe einer Kategorie (definiert durch den Pfad von der Wurzel zu sich selbst) soll die Liste der zugeordneten Produkte ermittelt werden. Die Angabe des Pfades ist notwendig, da der Kategorienname allein nicht eindeutig ist.")
    @GetMapping("/getProductsByCategoryPath")
    public List<Product> getProductsByCategoryPath(@RequestParam String path) {
        return categoryService.getProductsByCategoryPath(path);
    }
}
