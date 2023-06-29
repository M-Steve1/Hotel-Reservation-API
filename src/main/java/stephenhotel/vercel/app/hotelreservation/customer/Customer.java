package stephenhotel.vercel.app.hotelreservation.customer;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Customer class represents the customers/clients
 */
@Document(collection = "customers")
@Data
public class Customer {
    @Id
    private ObjectId id;
    private final String fullName;
    private final String email;
    private final String password;

    public Customer(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
}
