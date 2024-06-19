package ul.dbprak.testat03.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ul.dbprak.testat03.repository.model.ProductSimilar;
import ul.dbprak.testat03.repository.model.ProductSimilarId;

import java.util.List;

public interface ProductSimilarRepository extends JpaRepository<ProductSimilar, ProductSimilarId> {
    List<ProductSimilar> findByProduct_productId(String product_productId);
}