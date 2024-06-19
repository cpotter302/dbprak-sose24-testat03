package ul.dbprak.testat03.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ul.dbprak.testat03.repository.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}