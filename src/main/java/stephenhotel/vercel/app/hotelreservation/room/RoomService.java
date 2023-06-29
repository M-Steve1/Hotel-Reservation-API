package stephenhotel.vercel.app.hotelreservation.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import stephenhotel.vercel.app.hotelreservation.reservation.Reservation;
import stephenhotel.vercel.app.hotelreservation.shared.Shared;

import java.util.*;

/**
 * Service class contains the business logic
 */
@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    MongoTemplate mongoTemplate;
    Shared shared = new Shared();

    /**
     *
     * @return List of Room
     */
    protected List<Room> getRooms() {
        try {
            return roomRepository.findAll();
        }catch (Exception ex){
            throw new Error(ex);
        }
    }

    /**
     *
     * @param room Room
     * @return Room
     * @description It returns the created room
     */
    protected Room createRoom(Room room) {
        try {
            return roomRepository.insert(room);
        }catch (Exception ex) {
            throw new Error(ex);
        }
    }

    /**
     *
     * @param roomNumber room number
     * @return Room
     * @description It returns the room with the specified room number
     */
    protected Optional<Room> getRoom(String roomNumber) {
        try{
            return roomRepository.findByRoomNumber(roomNumber);
        }catch (Exception ex) {
            throw new Error(ex);
        }
    }

    /**
     *
     * @param checkInDate Customer's check in date
     * @param checkOutDate Customer's check out date
     * @return List of Room
     * @description This method will return List of rooms based on the Customer's check-in and check-out date
     */
    protected List<Room> getAvailableRooms(String checkInDate, String checkOutDate) {
       try{
           List<Room> availableRooms = new ArrayList<>();
           List<String> confirm = new ArrayList<>();
           List<Room> rooms = getRooms();

           Date checkIn = shared.getDate(checkInDate);
           Date checkOut = shared.getDate(checkOutDate);

           for(Room room: rooms) {
               if(room.getReservationList().isEmpty()) {
                   availableRooms.add(room);
               } else {
                   for (Reservation reservation: room.getReservationList()) {
                       if (
                               checkIn.after(reservation.getCheckOutDate()) ||
                                       (checkIn.before(reservation.getCheckInDate()) && checkOut.before(reservation.getCheckInDate()))
                       ) {
                           confirm.add("Yes");
                       }
                   }

                   if (room.getReservationList().size() == confirm.size()) {
                       availableRooms.add(room);
                   }
               }
           }

           return availableRooms;
       }catch (Exception ex) {
           throw new Error(ex);
       }
    }

    /**
     *
     * @param checkInDate Customer's check in date
     * @param checkOutDate Customer's check out date
     * @param roomType Room type
     * @return List of Room
     * @description This method will return list of rooms from under the specified category/roomType but based on the Customer's check-in and check-out date
     */
    protected List<Room> getAvailableRoomsBasedOnCategory(String checkInDate, String checkOutDate, String roomType) {
        try{
            List<Room> availableRooms = new ArrayList<>();
            List<String> confirm = new ArrayList<>();
            Query query = new Query(Criteria.where("roomType").is(roomType));
            List<Room> rooms = mongoTemplate.find(query, Room.class);

            Date checkIn = shared.getDate(checkInDate);
            Date checkOut = shared.getDate(checkOutDate);

            for(Room room: rooms) {
                if (room.getReservationList().isEmpty()) {
                    availableRooms.add(room);
                } else {
                    for (Reservation reservation : room.getReservationList()) {
                        if (
                                checkIn.after(reservation.getCheckOutDate()) ||
                                        (checkIn.before(reservation.getCheckInDate()) && checkOut.before(reservation.getCheckInDate()))
                        ) {
                            confirm.add("Yes");
                        }
                    }

                    if (room.getReservationList().size() == confirm.size()) {
                        availableRooms.add(room);
                    }
                }
            }
            return availableRooms;
        }catch (Exception ex){
            throw new Error(ex);
        }
    }

    /**
     *
     * @param checkInDate Customer's check in date
     * @param checkOutDate Customer's check out date
     * @param roomType Room type
     * @return Room list size
     * @description It returns the number of rooms under the specified category/roomType that are available based on Customer's check-in and check-out date
     */
    protected int count (String checkInDate, String checkOutDate, String roomType) {
        try {
            return getAvailableRoomsBasedOnCategory(checkInDate, checkOutDate, roomType).size();
        }catch (Exception ex){
            throw new Error(ex);
        }
    }

    /**
     * @description Checks each room from db and removes the expired reservations.
     */
    protected void updateRooms() {
        try {
            List<Room> rooms = getRooms();
            Date currentDate = Calendar.getInstance().getTime();
            List<Reservation> expiredReservations = new ArrayList<>();

            for (Room room: rooms) {
                if (!room.getReservationList().isEmpty()) {
                    for (Reservation reservation: room.getReservationList()) {
                        expiredReservations.clear();
                        if (currentDate.after(reservation.getCheckOutDate())) {
                            expiredReservations.add(reservation);
                        }
                    }
                    room.getReservationList().removeAll(expiredReservations);
                    mongoTemplate.update(Room.class)
                            .matching(Criteria.where("roomNumber").is(room.getRoomNumber()))
                            .apply(new Update().set("reservationList", room.getReservationList()))
                            .findAndModify();
                }
            }
        }catch (Exception ex) {
            throw new Error(ex);
        }
    }
    /**
     *
     * @param checkInDate Customer's check in date
     * @param checkOutDate Customer's check out date
     * @return List of rooms
     * @description This method finds rooms by adding 7 days to the check-in and checkout date of the customer,
     * this occurs when there are no available rooms.
     */
    protected List<Room> findRooms (String checkInDate, String checkOutDate) {
        try{
            List<Room> availableRooms = new ArrayList<>();
            List<String> confirm = new ArrayList<>();
            List<Room> rooms = getRooms();

            Date checkIn = shared.getDate(checkInDate);
            Date checkOut = shared.getDate(checkOutDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(checkIn);
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            Date checkInDatePlus7 = calendar.getTime();

            calendar.setTime(checkOut);
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            Date checkOutDatePlus7 = calendar.getTime();

            for (Room room: rooms) {
                for (Reservation reservation: room.getReservationList()) {
                    if (
                            checkInDatePlus7.after(reservation.getCheckOutDate()) ||
                                    (checkInDatePlus7.before(reservation.getCheckInDate()) && checkOutDatePlus7.before(reservation.getCheckInDate()))
                    ) {
                        confirm.add("Yes");
                    }
                }
                if (confirm.size() == room.getReservationList().size()) {
                    availableRooms.add(room);
                }
            }

            return availableRooms;
        }catch (Exception ex){
            throw new Error(ex);
        }
    }

    /**
     *
     * @param roomType Room type
     * @return Room
     * @description It returns a room with the specified room type
     */
    protected Room getRoomByCategory(String roomType) {
       try{
           Query query = new Query(Criteria.where("roomType").is(roomType));
           return mongoTemplate.findOne(query, Room.class);
       }catch (Exception ex){
           throw new Error(ex);
       }
    }
}
