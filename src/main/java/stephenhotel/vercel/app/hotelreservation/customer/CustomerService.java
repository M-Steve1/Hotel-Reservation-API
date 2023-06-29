package stephenhotel.vercel.app.hotelreservation.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Service class contains the business logic
 */
@Service
public class CustomerService {
    private final String emailRegex = "^(.+)@(.+).com$";
    private final Pattern pattern = Pattern.compile(emailRegex);
    private Properties props;

    @Autowired
    CustomerRepository customerRepository;

    /**
     *
     * @return all the registered customers
     */
    protected List<Customer> getCustomers() {
        try {
            return customerRepository.findAll();
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    /**
     *
     * @param email customer's email
     * @return Customer
     */
    private Optional<Customer> doesUserExist(String email) {
        try{
            return customerRepository.findByEmail(email);
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    /**
     * @description loads "config.properties" file and give access to the properties
     */
    private void loadConfigProperties() {
        try(InputStream is = CustomerService.class.getClassLoader().getResourceAsStream("config.properties")){
            props = new Properties();
            props.load(is);
        }catch (IOException ex) {
            throw new Error(ex);
        }
    }

    /**
     *
     * @param password customer's password
     * @return hashed password
     */
    private String hashPassword(String password) {
       try{
           loadConfigProperties();
           String pepper = props.getProperty("pepper");
           int saltRounds = Integer.parseInt(props.getProperty("saltRounds"));
           String generatedSalt = BCrypt.gensalt(saltRounds);

           return BCrypt.hashpw(password + pepper, generatedSalt);
       } catch (Exception ex) {
           throw new Error(ex);
       }

    }

    /**
     *
     * @param fullName customer's full name
     * @param email customer's email
     * @param password customer's password
     * @return "Account Created"
     * @description On success: "Account Created", on failure: "Please enter a valid email" or "Email Already Exist"
     */
    protected String create(String fullName, String email, String password) {
        try {
            String hashedPassword = hashPassword(password);

            Customer customer = new Customer(fullName, email, hashedPassword);

            if (!pattern.matcher(email).matches()) {
                return "Please enter a valid email";
            } else if (doesUserExist(email).isPresent()) {
                return "Email Already Exist";
            } else {
                customerRepository.insert(customer);
                return "Account Created";
            }
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    /**
     *
     * @param email customer's email
     * @param password customer's password
     * @return "User Authenticated"
     * @description On success: "User Authenticated", on failure: "Incorrect Details" or "Account does not exist"
     */
    protected String authenticate(String email, String password) {
        try {
            loadConfigProperties();

            Optional<Customer> customer = doesUserExist(email);
            if(customer.isPresent()) {
                String pepper = props.getProperty("pepper");
                boolean comparePassword = BCrypt.checkpw(password + pepper, customer.get().getPassword());
                if (comparePassword) {
                    return "User Authenticated";
                } else {
                    return "Incorrect Details";
                }
            } else {
                return "Account does not exist";
            }
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

}
