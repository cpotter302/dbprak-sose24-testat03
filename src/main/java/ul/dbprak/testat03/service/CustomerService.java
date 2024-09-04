package ul.dbprak.testat03.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ul.dbprak.testat03.repository.CustomerRepository;
import ul.dbprak.testat03.repository.ReviewRepository;
import ul.dbprak.testat03.repository.model.Customer;
import ul.dbprak.testat03.repository.model.Review;

import java.util.*;
import java.util.stream.Collectors;

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
        Map<Customer, List<Review>> reviewsByCustomer = reviews.stream().collect(Collectors.groupingBy(Review::getCustomer));

        Map<Customer, Double> averageRatingsByCustomer = reviewsByCustomer.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .mapToDouble(Review::getRating)
                                .average()
                                .orElse(0.0)
                ));

        return averageRatingsByCustomer.entrySet().stream()
                .filter(entry -> entry.getValue() < ratingBelow)
                .map(Map.Entry::getKey)
                .toList();
    }
}
