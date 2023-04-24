package tfip.day27workshop.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tfip.day27workshop.model.Comment;
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
            r.setPosted(LocalDateTime.now());
        }

        return repo.saveReview(r);
    }

    public void updateReview(String reviewId, Comment c) {
        if (c.getComment() == null) {
            c.setComment("");
        }
        if (c.getPosted() == null) {
            c.setPosted(LocalDateTime.now());
        }

        System.out.println(c);
        repo.updateReview(reviewId, c);
    }

}
