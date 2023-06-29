package stephenhotel.vercel.app.hotelreservation.room;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import stephenhotel.vercel.app.hotelreservation.reservation.Reservation;

import java.util.List;

/**
 * Room class represents all the rooms in the hotel
 */
@Document(collection = "rooms")
@Data
public class Room {

    @Id
    private ObjectId id;
    private final String roomNumber;
    private final Double roomPrice;
    private final String roomType;
    @DocumentReference
    private final List<Reservation> reservationList;

    Room(String roomNumber, Double roomPrice, String roomType, List<Reservation> reservationList) {
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.roomType = roomType;
        this.reservationList = reservationList;
    }

}
