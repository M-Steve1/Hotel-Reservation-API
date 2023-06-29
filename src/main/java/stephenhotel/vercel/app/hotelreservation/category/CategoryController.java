package stephenhotel.vercel.app.hotelreservation.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class, this class contains all "Category" api.
 */

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /**
     *
     * @return all the categories in the db.
     */
    @GetMapping("/categories")
    private ResponseEntity<List<Category>> getCategories() {
       try{
           return  new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
       } catch (Exception ex) {
           throw new Error("Can not get Categories");
       }
    }

    /**
     *
     * @param roomType represents the roomType e.g "STUDIO", "DELUXE", "BUSINESS SUITE" e.t.c
     * @return the category having the query parameter
     */
    @GetMapping("/{roomType}")
    private ResponseEntity<Category> getCategory(@PathVariable String roomType) {
        try {
            return  new ResponseEntity<>(categoryService.findByRoomType(roomType.toUpperCase()), HttpStatus.OK);
        } catch (Exception ex) {
            throw new Error("Can not get Categories");
        }
    }
}
