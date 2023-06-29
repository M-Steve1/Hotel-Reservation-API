package stephenhotel.vercel.app.hotelreservation.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller class, this class contains all "Customer" api.
 */
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    /**
     *
     * @return all the registered customers
     */
    @GetMapping("/customers")
    protected List<Customer> getCustomers() {
        try {
            return customerService.getCustomers();
        } catch (Exception ex) {
            throw new Error("Could not get Customers");
        }
    }

    /**
     *
     * @param body request body
     * @return "Account Created"
     * @description On success: "Account Created", on failure: "Please enter a valid email" or "Email Already Exist"
     */
    @PostMapping("/signup")
    private ResponseEntity<String> create(@RequestBody Map<String, String> body) {
        try{
            String fullName = body.get("fullName");
            String email = body.get("email");
            String password = body.get("password");
            return new ResponseEntity<>(customerService.create(fullName, email, password), HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new Error("Could not create user");
        }
    }

    /**
     *
     * @param body request body
     * @return "User Authenticated"
     * @description On success: "User Authenticated", on failure: "Incorrect Details" or "Account does not exist"
     */
    @PostMapping("/authenticate")
    private ResponseEntity<String> authenticate(@RequestBody Map<String, String> body) {
       try {
           String email = body.get("email");
           String password = body.get("password");
           return new ResponseEntity<>(customerService.authenticate(email, password), HttpStatus.OK);
       } catch (Exception ex) {
           throw new Error ("Couldn't authenticate user");
       }
    }
}
