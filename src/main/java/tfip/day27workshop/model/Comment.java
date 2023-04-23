package tfip.day27workshop.model;

import java.time.LocalDateTime;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Comment {

    private String comment;

    @NotNull
    @Min(value = 0)
    @Max(value = 10)
    private Integer rating;

    private LocalDateTime posted;

    public Comment() {
    }

    public Comment(String comment, Integer rating, LocalDateTime posted) {
        this.comment = comment;
        this.rating = rating;
        this.posted = posted;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDateTime getPosted() {
        return posted;
    }

    public void setPosted(LocalDateTime posted) {
        this.posted = posted;
    }

    @Override
    public String toString() {
        return "Comment [comment=" + comment + ", rating=" + rating + ", posted=" + posted + "]";
    }

    public String toJSONString() {
        return toJSON().toString();
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("comment", getComment())
                .add("rating", getRating())
                .add("posted", getPosted().toString())
                .build();
    }

    public Document toDocument() {
        return Document.parse(toJSONString());
    }

    public static Comment create(JsonObject o) {
        return new Comment(
                o.getString("comment"),
                o.getInt("rating"),
                LocalDateTime.parse(o.getString("posted")));
    }

    public static Comment create(Document d) {
        return new Comment(
                d.getString("comment"),
                d.getInteger("rating"),
                LocalDateTime.parse(d.getString("posted")));
    }
}
