package stephenhotel.vercel.app.hotelreservation.category;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * This class represents the rooms categories
 * The Hotel rooms are separated into categories
 * and each category has a price.
 */
@Document(collection = "categories")
@Data
public class Category {
    @Id
    private ObjectId id;
    private final String roomType;
    private final Double price;
    private final List<String> urls;

    public Category(String roomType, Double price, List<String> urls) {
        this.roomType = roomType;
        this.price = price;
        this.urls = urls;
    }

}
