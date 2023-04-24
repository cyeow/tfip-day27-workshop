package tfip.day27workshop.repository;

import java.util.NoSuchElementException;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.validation.Valid;
import tfip.day27workshop.model.Comment;
import tfip.day27workshop.model.Review;

@Repository
public class ReviewRepository {

    @Autowired
    private MongoTemplate mongo;

    private static final String COLLECTION_REVIEWS = "reviews";

    public Review findReviewById(String reviewId) {

        if (ObjectId.isValid(reviewId)) {
            Query q = new Query();
            q.addCriteria(Criteria.where("_id").is(reviewId));

            try {
                return mongo.find(q, Document.class, COLLECTION_REVIEWS)
                        .stream().map(d -> Review.create(d)).findFirst().get();
            } catch (NoSuchElementException e) {
                return null;
            }
        }

        return null;
    }

    public String saveReview(Review r) {
        Document result = mongo.insert(r.toDocument(), COLLECTION_REVIEWS);

        return result.getObjectId("_id").toString();
    }

    public void updateReview(String reviewId, @Valid Comment c) {
        Query q = new Query();
        q.addCriteria(Criteria.where("_id").is(reviewId));
        Review r = Review.create(mongo.findOne(q, Document.class, COLLECTION_REVIEWS));
        r.addComment(c);
        System.out.println("review >> " + r);
        Document result = mongo.findAndReplace(q, r.toDocument(), COLLECTION_REVIEWS);
        System.out.println(result);
        // return result.getObjectId("_id");
    }

}
