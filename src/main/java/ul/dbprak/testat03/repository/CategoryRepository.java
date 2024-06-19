package ul.dbprak.testat03.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ul.dbprak.testat03.repository.model.Category;
import ul.dbprak.testat03.repository.model.Product;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.parentCategory IS NULL")
    List<Category> findAllByParentCategoryIsNull();

    @Query("SELECT c FROM Category c WHERE c.parentCategory = :id")
    List<Category> findAllByParentCategory(Integer id);
}