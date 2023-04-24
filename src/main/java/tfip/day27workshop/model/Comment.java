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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        result = prime * result + ((rating == null) ? 0 : rating.hashCode());
        result = prime * result + ((posted == null) ? 0 : posted.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Comment other = (Comment) obj;
        if (comment == null) {
            if (other.comment != null)
                return false;
        } else if (!comment.equals(other.comment))
            return false;
        if (rating == null) {
            if (other.rating != null)
                return false;
        } else if (!rating.equals(other.rating))
            return false;
        if (posted == null) {
            if (other.posted != null)
                return false;
        } else if (!posted.equals(other.posted))
            return false;
        return true;
    }

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
