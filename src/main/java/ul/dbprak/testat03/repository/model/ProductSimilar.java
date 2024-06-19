package ul.dbprak.testat03.repository.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "product_similars")
public class ProductSimilar {
    @EmbeddedId
    private ProductSimilarId id;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @MapsId("similarProduct")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "similar_product", nullable = false)
    private Product similarProduct;

    public ProductSimilarId getId() {
        return id;
    }

    public void setId(ProductSimilarId id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getSimilarProduct() {
        return similarProduct;
    }

    public void setSimilarProduct(Product similarProduct) {
        this.similarProduct = similarProduct;
    }

}