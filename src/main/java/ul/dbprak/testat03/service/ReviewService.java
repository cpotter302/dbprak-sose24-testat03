package ul.dbprak.testat03.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ul.dbprak.testat03.repository.CustomerRepository;
import ul.dbprak.testat03.repository.ProductRepository;
import ul.dbprak.testat03.repository.ReviewRepository;
import ul.dbprak.testat03.repository.model.Customer;
import ul.dbprak.testat03.repository.model.Product;
import ul.dbprak.testat03.repository.model.Review;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public ResponseEntity<String> addNewReview(Review review) {
        // Check if the customer exists
        Customer customer = customerRepository.findById(review.getCustomer().getCustomerId()).orElse(null);
        if (customer == null) {
            return ResponseEntity.badRequest().body("Customer does not exist: " + review.getCustomer().getCustomerId() + " -> create the customer first.");
        }
        // Check if the product exists
        Product product = productRepository.findById(review.getProduct().getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body("Product does not exist: " + review.getProduct().getProductId());
        }
        // Save the review

        review.setCustomer(customer);
        review.setProduct(product);
        review.setReviewDate(Date.valueOf(LocalDate.now()));
        Review newReview = reviewRepository.save(review);
        return ResponseEntity.ok(newReview.toString());
    }

    public List<Review> getReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewsById(int id) {
        return reviewRepository.findById(id).orElse(null);
    }
}
