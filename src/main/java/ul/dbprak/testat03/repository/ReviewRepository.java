package ul.dbprak.testat03.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ul.dbprak.testat03.repository.model.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}