package ul.dbprak.testat03.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductSimilarId implements Serializable {
    private static final long serialVersionUID = -537014265507298660L;
    @Column(name = "product_id", nullable = false, length = 10)
    private String productId;

    @Column(name = "similar_product", nullable = false, length = 10)
    private String similarProduct;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSimilarProduct() {
        return similarProduct;
    }

    public void setSimilarProduct(String similarProduct) {
        this.similarProduct = similarProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductSimilarId entity = (ProductSimilarId) o;
        return Objects.equals(this.similarProduct, entity.similarProduct) &&
                Objects.equals(this.productId, entity.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(similarProduct, productId);
    }

}