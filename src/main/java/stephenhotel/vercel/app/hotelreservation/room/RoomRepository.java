package stephenhotel.vercel.app.hotelreservation.room;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface RoomRepository extends MongoRepository<Room, ObjectId> {
    Optional<Room> findByRoomNumber(String roomNumber);
}
