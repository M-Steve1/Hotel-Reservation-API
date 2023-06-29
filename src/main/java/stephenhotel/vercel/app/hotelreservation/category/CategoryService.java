package stephenhotel.vercel.app.hotelreservation.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class contains the business logic
 */
@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    /**
     *
     * @return all categories in the db.
     */
    protected List<Category> getCategories() {
        try {
            return categoryRepository.findAll();
        }catch (Exception ex) {
            throw new Error(ex);
        }
    }

    /**
     *
     * @param roomType represents the roomType e.g "STUDIO", "DELUXE", "BUSINESS SUITE" e.t.c
     * @return the category having the query parameter
     */
    protected Category findByRoomType(String roomType) {
       try {
           return categoryRepository.findByRoomType(roomType);
       }catch (Exception ex) {
           throw new Error(ex);
       }
    }
}
