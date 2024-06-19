package ul.dbprak.testat03.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ul.dbprak.testat03.repository.CustomerRepository;
import ul.dbprak.testat03.repository.ReviewRepository;
import ul.dbprak.testat03.repository.model.Customer;
import ul.dbprak.testat03.repository.model.Review;

import java.util.Collections;
import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;

    public CustomerService(CustomerRepository customerRepository, ReviewRepository reviewRepository) {
        this.customerRepository = customerRepository;
        this.reviewRepository = reviewRepository;
    }

    public ResponseEntity<String> addNewCustomer(Customer customer) {
        Customer existingCustomer = customerRepository.findById(customer.getCustomerId()).orElse(null);
        if (existingCustomer != null) {
            return ResponseEntity.badRequest().body("Customer already exists: " + customer.getCustomerId());
        }
        return ResponseEntity.ok(customerRepository.save(customer).toString());
    }

    public List<Customer> getTrolls(int ratingBelow) {
        if (ratingBelow < 1 || ratingBelow > 5) {
            return Collections.emptyList();
        }
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().filter(r -> r.getRating() < ratingBelow).map(Review::getCustomer).distinct().toList();
    }
}
