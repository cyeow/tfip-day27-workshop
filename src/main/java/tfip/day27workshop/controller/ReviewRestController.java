package tfip.day27workshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import tfip.day27workshop.model.Review;
import tfip.day27workshop.service.GamesService;
import tfip.day27workshop.service.ReviewService;

@RestController
public class ReviewRestController {

    @Autowired
    private ReviewService reviewSvc;

    @Autowired
    private GamesService gameSvc;

    @PostMapping(path = "/review", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> addReview(Review r) {
        if (!isValidReview(r)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(generateMsgJsonString("Invalid values in form."));
        }

        try {
            String id = reviewSvc.addReview(r);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(generateMsgJsonString("Review saved with id " + id + "."));

        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(generateMsgJsonString("Error encountered saving review."));
        }
    }

    @PutMapping(path = "/review/{reviewId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateReview(@PathVariable String reviewId, Review r) {

        if (!reviewSvc.isValidReviewId(reviewId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(generateMsgJsonString("No review found with id " + reviewId + "."));
        }

        reviewSvc.updateReview(reviewId, r);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body("");
    }

    @GetMapping(path = "/review/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getReview(@PathVariable String reviewId) {

        Review r = reviewSvc.getReviewById(reviewId);

        if (r == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(generateMsgJsonString("No review found with id " + reviewId + "."));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(r.toJSONString());
    }

    @GetMapping(path = "/review/{reviewId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getReviewHistory(@PathVariable String reviewId) {

        if (!reviewSvc.isValidReviewId(reviewId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(generateMsgJsonString("No review found with id " + reviewId + "."));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body("");
    }

    private String generateMsgJsonString(String msg) {
        return Json.createObjectBuilder()
                .add("message", msg)
                .build().toString();
    }

    private boolean isValidReview(Review r) {
        if (r.getUser() == null || r.getUser().isBlank()) {
            return false;
        } else if (r.getRating() < 0 || r.getRating() > 10) {
            return false;
        } else if (!gameSvc.isValidGameId(r.getGameId())) {
            return false;
        }
        return true;
    }

}
