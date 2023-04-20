package tfip.day27workshop.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tfip.day27workshop.model.Review;
import tfip.day27workshop.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repo;

    public boolean isValidReviewId(String reviewId) {
        if (repo.findReviewById(reviewId) != null) {
            return true;
        }
        return false;
    }

    public Review getReviewById(String reviewId) {
        return repo.findReviewById(reviewId);
    }

    public String addReview(Review r) {
        if (r.getPosted() == null) {
            r.setPosted(LocalDate.now());
        }

        return repo.saveReview(r);
    }

    public void updateReview(String reviewId, Review r) {
    }

}
