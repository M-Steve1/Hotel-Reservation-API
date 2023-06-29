package stephenhotel.vercel.app.hotelreservation.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import stephenhotel.vercel.app.hotelreservation.room.Room;
import stephenhotel.vercel.app.hotelreservation.shared.Shared;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service class contains the business logic
 */
@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    Shared shared = new Shared();

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     *
     * @param email Customer's email
     * @param roomNumber  room number to be reserved
     * @param checkInDate Date customer will check in yyyy/mm/dd
     * @param checkOutDate Date customer will check out yyyy/mm/dd
     * @return The created Reservation
     */
    protected Reservation reserveARoom(String email, String roomNumber, String checkInDate, String checkOutDate, int numberOfAdults, int numberOfChildren) {
        try {
            Date checkIn = shared.getDate(checkInDate);
            Date checkOut = shared.getDate(checkOutDate);

            Reservation reservation = reservationRepository.insert(new Reservation(email, roomNumber, checkIn, checkOut, numberOfAdults, numberOfChildren));

            mongoTemplate.update(Room.class)
                    .matching(Criteria.where("roomNumber").is(roomNumber))
                    .apply(new Update().push("reservationList").value(reservation))
                    .first();

            return reservation;
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    /**
     *
     * @return All Reservations
     */
    protected List<Reservation> getReservations () {
        try {
            return reservationRepository.findAll();
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    /**
     *
     * @param email Customer's email
     * @return All the reservations registered with the customer's email.
     */
    protected  Optional<List<Reservation>> getCustomerReservations(String email) {
        try{
            return reservationRepository.findByCustomerEmail(email);
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }
}
