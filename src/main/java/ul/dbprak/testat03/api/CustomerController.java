package ul.dbprak.testat03.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ul.dbprak.testat03.repository.model.Customer;
import ul.dbprak.testat03.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "addNewCustomer"
            , description = "Diese Methode fügt einen neuen User hinzu.")
    @PostMapping("")
    public ResponseEntity<String> addNewCustomer(@RequestBody Customer customer) {
        return customerService.addNewCustomer(customer);

    }

    /*
    * Die Methode soll eine Liste von Nutzern ausgeben,
    * deren Durchschnittsbewertung unter einem spezifizierten Rating ist.
    * */
    @Operation(summary = "getTrolls"
            , description = "Die Methode soll eine Liste von Nutzern ausgeben, deren Durchschnittsbewertung unter einem spezifizierten Rating ist.")
    @GetMapping("/trolls")
    public List<Customer> getTrolls(@RequestParam int ratingBelow) {
        return customerService.getTrolls(ratingBelow);
    }
}