package stephenhotel.vercel.app.hotelreservation.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller class, this class contains all "Reservation" api.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    /**
     *
     * @param body request body
     * @return the reservation created
     */
    @PostMapping("/create")
    private ResponseEntity<Reservation> reserveARoom(@RequestBody Map<String, String> body) {
       try {
           String email = body.get("email");
           String roomNumber = body.get("roomNumber");
           String checkInDate = body.get("checkInDate");
           String checkOutDate = body.get("checkOutDate");
           int numberOfAdults = Integer.parseInt(body.get("numberOfAdults"));
           int numberOfChildren = Integer.parseInt(body.get("numberOfChildren"));
           return new ResponseEntity<>(reservationService.reserveARoom(email, roomNumber, checkInDate, checkOutDate, numberOfAdults, numberOfChildren), HttpStatus.CREATED);
       }catch (Exception ex) {
           throw new Error("Could not reserve a room");
       }
    }

    /**
     *
     * @return list of reservations
     */
    @GetMapping("/reservations")
    private ResponseEntity<List<Reservation>> getReservations() {
        try {
            return new ResponseEntity<>(reservationService.getReservations(), HttpStatus.OK);
        }catch (Exception ex) {
            throw new Error("Could not fetch reservation");
        }
    }

    /**
     *
     * @param email customer's email
     * @return list of reservations made by a customer
     */
    @GetMapping("/customer-reservations/{email}")
    private ResponseEntity<Optional<List<Reservation>>> getCustomerReservation(@PathVariable String email) {
        try{
            return new ResponseEntity<>(reservationService.getCustomerReservations(email), HttpStatus.OK);
        }catch (Exception ex) {
            throw new Error("Could not fetch customer's reservation");
        }
    }
}
