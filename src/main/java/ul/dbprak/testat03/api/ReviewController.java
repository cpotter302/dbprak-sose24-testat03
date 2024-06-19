package ul.dbprak.testat03.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ul.dbprak.testat03.repository.model.Review;
import ul.dbprak.testat03.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "addNewReview"
            , description = "Die Methode soll ein neues Review in der Datenbank speichern.")
    @PostMapping("/add")
    public ResponseEntity<String> addNewReview(@RequestBody Review review) {
        return reviewService.addNewReview(review);

    }

    @Operation(summary = "getReviews"
            , description = "Die Methode soll eine Liste aller Reviews zurückgeben.")
    @GetMapping("")
    public List<Review> getReviews() {
        return reviewService.getReviews();
    }

    @Operation(summary = "getReviewById"
            , description = "Die Methode soll ein Review anhand der Id zurückgeben.")
    @GetMapping("{id}")
    public Review getReviewById(@PathVariable int id) {
        return reviewService.getReviewsById(id);
    }

}