package ul.dbprak.testat03.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ul.dbprak.testat03.repository.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("SELECT p FROM Product p WHERE p.title ILIKE :pattern")
    List<Product> findByPattern(@Param("pattern") String pattern);

    @Query(value = "SELECT p FROM Product p WHERE p.averageRating IS NOT NULL ORDER BY p.averageRating DESC")
    List<Product> findTopProducts(Pageable pageable);
}