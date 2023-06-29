package stephenhotel.vercel.app.hotelreservation.reservation;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;

/**
 * Reservation class represents the reservations made by customers
 */

@Document(collection = "reservations")
@Data
public class Reservation {
    @Id
    private ObjectId id;
    private final String customerEmail;
    private final String roomNumber;
    private final Date checkInDate;
    private final Date checkOutDate;
    private final int numberOfAdults;
    private  final int numberOfChildren;

    public Reservation(String customerEmail, String roomNumber, Date checkInDate, Date checkOutDate, int numberOfAdults, int numberOfChildren) {
        this.customerEmail = customerEmail;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }
}
