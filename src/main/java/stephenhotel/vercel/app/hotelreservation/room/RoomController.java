package stephenhotel.vercel.app.hotelreservation.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller class, this class contains all "Room" api.
 */

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

    @Autowired
    RoomService roomService;

    /**
     *
     * @return List of Room
     * @description It returns all the rooms in the db
     */
    @GetMapping("/rooms")
    private ResponseEntity<List<Room>> getAllRooms() {
        try {
            return new ResponseEntity<>(roomService.getRooms(), HttpStatus.OK);
        } catch (Exception ex) {
            throw new Error("Couldn't fetch rooms");
        }
    }

    /**
     *
     * @param body Request body
     * @return Room
     * @description It will return the created room
     */
    @PostMapping("/create")
    private ResponseEntity<Room> createRoom(@RequestBody Map<String, String> body) {
       try{
           String roomNumber = body.get("roomNumber");
           Double roomPrice = Double.parseDouble(body.get("roomPrice"));
           String roomType = body.get("roomType").toUpperCase();

           Room room = new Room(roomNumber, roomPrice, roomType, new ArrayList<>());

           return new ResponseEntity<>(roomService.createRoom(room), HttpStatus.CREATED);
       }catch (Exception ex) {
           throw new Error("Couldn't create room");
       }
    }

    /**
     *
     * @param roomNumber Room number
     * @return Room
     * @description It returns the room with the specified room number
     */
    @GetMapping("/{roomNumber}")
    private ResponseEntity<Optional<Room>> getRoom(@PathVariable String roomNumber) {
        try{
            return new ResponseEntity<>(roomService.getRoom(roomNumber), HttpStatus.OK);
        }catch (Exception ex) {
            throw new Error("Could not fetch room");
        }
    }

    /**
     *
     * @param body Request body
     * @return List of Room
     * @description This method when executed returns list of rooms based on the Customer's check-in and check-out date
     */
    @PostMapping("/available-rooms")
    private ResponseEntity<List<Room>> getAllAvailableRooms(@RequestBody Map<String, String> body) {
       try{
           String checkInDate = body.get("checkInDate");
           String checkOutDate = body.get("checkOutDate");
           return new ResponseEntity<>(roomService.getAvailableRooms(checkInDate, checkOutDate), HttpStatus.OK);
       }catch (Exception ex) {
           throw new Error("Could not fetch available rooms");
       }
    }

    /**
     *
     * @param body Request body
     * @return List of Room
     * @description This method returns list of rooms from under the specified category/roomType but based on the Customer's check-in and check-out date
     */
    @PostMapping("/available-rooms-in-category")
    private ResponseEntity<List<Room>> getAvailableRoomsBasedOnCategory(@RequestBody Map<String, String> body) {
      try{
          String checkInDate = body.get("checkInDate");
          String checkOutDate = body.get("checkOutDate");
          String roomType = body.get("roomType");
          return new ResponseEntity<>(roomService.getAvailableRoomsBasedOnCategory(checkInDate, checkOutDate, roomType.toUpperCase()), HttpStatus.OK);
      }catch (Exception ex) {
          throw new Error("Could not fetch rooms categorically");
      }
    }

    /**
     *
     * @param body Request body
     * @return Room list size
     * @description It returns the number of rooms under the specified category/roomType that are available based on Customer's check-in and check-out date
     */
    @PostMapping("/available-rooms-in-category/count")
    private ResponseEntity<Integer> count(@RequestBody Map<String, String> body) {
        try{
            String checkInDate = body.get("checkInDate");
            String checkOutDate = body.get("checkOutDate");
            String roomType = body.get("roomType");
            return new ResponseEntity<>(roomService.count(checkInDate, checkOutDate, roomType.toUpperCase()), HttpStatus.OK);
        } catch (Exception ex){
            throw new Error("Could not get room count");
        }
    }


    /**
     * @param body  Request body
     * @return List of rooms
     * @description This function finds rooms by adding 7 days to the check-in and checkout date of the customer,
     * this occurs when there are no available rooms.
     */
    @PostMapping("/find-rooms")
    private ResponseEntity<List<Room>> findRooms(@RequestBody Map<String, String> body) {
        try{
            String checkInDate = body.get("checkInDate");
            String checkOutDate = body.get("checkOutDate");
            return new ResponseEntity<>(roomService.findRooms(checkInDate, checkOutDate), HttpStatus.OK);
        }catch (Exception ex) {
            throw new Error("Could not find rooms");
        }
    }

    /**
     * @description Checks each room from db and removes the expired reservations.
     */
    @GetMapping("/update-rooms")
    private void updateRooms() {
        try {
            this.roomService.updateRooms();
        }catch (Exception ex){
            throw new Error("Could not update rooms");
        }
    }

    /**
     *
     * @param roomType Room type
     * @return Room
     * @description It returns a room with the specified room type
     */
    @GetMapping("/find-room-by-category/{roomType}")
    private ResponseEntity<Room> getRoomByCategory(@PathVariable String roomType) {
        try{
            return new ResponseEntity<>(roomService.getRoomByCategory(roomType), HttpStatus.OK);
        }catch (Exception ex){
            throw new Error("Could not fetch room categorically");
        }
    }

}
