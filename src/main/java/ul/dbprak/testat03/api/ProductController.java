package ul.dbprak.testat03.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import ul.dbprak.testat03.repository.model.Offer;
import ul.dbprak.testat03.repository.model.Product;
import ul.dbprak.testat03.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // TODO: Detailinfos aus Welcher Tabelle?
    @Operation(summary = "getProduct"
            , description = "Für eine bestimmte Produkt-Id werden mit dieser Methode die Detailinformationen des Produkts ermittelt.")
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @Operation(summary = "getProducts"
            , description = "Diese Methode soll eine Liste der in der Datenbank enthaltenen Produkte, deren Titel mit dem übergebenen Pattern übereinstimmen, zurückliefern. Beachten Sie, dass im Falle von pattern=null die komplette Liste zurückgeliefert wird. Das Pattern kann SQL-Wildcards enthalten.")
    @GetMapping("")
    public List<Product> getProducts(@RequestParam String pattern) {
        return productService.getProductsByPattern(pattern);
    }

    @Operation(summary = "getTopProducts"
            , description = "Diese Methode liefert eine Liste aller Produkte zurück, die unter den Top k sind basierend auf dem Rating.")
    @GetMapping("/by-rating")
    public List<Product> getTopProducts(@RequestParam int top) {
        return productService.getTopProducts(top);
    }

    @Operation(summary = "getSimilarCheaperProduct"
            , description = "Diese Methode liefert für ein Produkt(Id) eine List von Produkten, die ähnlich und billiger sind als das spezifizierte.")
    @GetMapping("/{id}/similiar-cheaper")
    public List<Product> getSimilarCheaperProduct(@PathVariable String id) {
        return productService.getSimilarCheaperProduct(id);
    }

    @Operation(summary = "getOffers"
            , description = "Für das übergegebene Produkt(Id) werden alle verfügbaren Angebote zurückgeliefert.")
    @GetMapping("/{id}/offers")
    public List<Offer> getOffers(@PathVariable String id) {
        return productService.getOffersForProduct(id);
    }
}