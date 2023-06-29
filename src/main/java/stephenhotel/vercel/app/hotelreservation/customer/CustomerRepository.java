package stephenhotel.vercel.app.hotelreservation.customer;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CustomerRepository extends MongoRepository<Customer, ObjectId> {
    Optional<Customer> findByEmail(String email);
}
