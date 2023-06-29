package stephenhotel.vercel.app.hotelreservation.reservation;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface ReservationRepository extends MongoRepository<Reservation, ObjectId> {
    Optional<List<Reservation>> findByCustomerEmail(String email);
}
