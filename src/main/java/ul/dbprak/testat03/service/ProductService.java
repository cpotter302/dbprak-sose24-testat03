package ul.dbprak.testat03.service;

import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ul.dbprak.testat03.repository.OfferRepository;
import ul.dbprak.testat03.repository.ProductRepository;
import ul.dbprak.testat03.repository.ProductSimilarRepository;
import ul.dbprak.testat03.repository.model.Offer;
import ul.dbprak.testat03.repository.model.Product;
import ul.dbprak.testat03.repository.model.ProductSimilar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final OfferRepository offerRepository;
    private final ProductSimilarRepository productSimilarRepository;

    public ProductService(ProductRepository productRepository, OfferRepository offerRepository, ProductSimilarRepository productSimilarRepository) {
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
        this.productSimilarRepository = productSimilarRepository;
    }


    public Product getProduct(String id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getProductsByPattern(String pattern) {
        if (pattern.isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.findByPattern(pattern);
    }

    public List<Product> getTopProducts(int top) {
        if (top <= 0) {
            return Collections.emptyList();
        } else if (top >= productRepository.findAll().size()) {
            return productRepository.findAll();
        }
        Pageable pageable = PageRequest.of(0, top);
        return productRepository.findTopProducts(pageable);
    }

    public List<Product> getSimilarCheaperProduct(String id) {
        Offer offer = offerRepository.findByProduct_productId(id).get(0);
        if (offer.getPrice() == null) {
            return Collections.emptyList();
        }

        List<Product> allCheaper = new ArrayList<>(offerRepository.findAll()
                .stream()
                .filter(o -> o.getPrice() != null && o.getPrice() <= offer.getPrice() && !o.getProduct().getProductId().equals(id))
                .map(Offer::getProduct)
                .toList());

        List<Product> allSimilar = productSimilarRepository.findByProduct_productId(id).stream().map(ProductSimilar::getSimilarProduct).toList();

        allCheaper.retainAll(allSimilar);
        return allCheaper;
    }

    public List<Offer> getOffersForProduct(String id) {
        return offerRepository.findByProduct_productId(id);
    }
}
