package ul.dbprak.testat03.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ul.dbprak.testat03.repository.model.Offer;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Integer> {
        List<Offer> findByProduct_productId(String product_productId);
    }