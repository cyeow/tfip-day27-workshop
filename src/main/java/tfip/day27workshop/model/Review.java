package tfip.day27workshop.model;

import java.io.StringReader;
import java.time.LocalDate;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Review {

    private String user;
    private Integer rating;
    private String comment;
    private Integer gameId;
    private LocalDate posted;

    public Review() {
    }

    public Review(String user, Integer rating, String comment, Integer gameId, LocalDate posted) {
        this.user = user;
        this.rating = rating;
        this.comment = comment;
        this.gameId = gameId;
        this.posted = posted;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public LocalDate getPosted() {
        return posted;
    }

    public void setPosted(LocalDate posted) {
        this.posted = posted;
    }

    @Override
    public String toString() {
        return "Review [user=" + user + ", rating=" + rating + ", comment=" + comment + ", gameId=" + gameId
                + ", posted=" + posted + "]";
    }

    // marshalling

    public String toJSONString() {
        return toJSON().toString();
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("user", getUser())
                .add("rating", getRating())
                .add("comment", getComment())
                .add("game_id", getGameId())
                .add("posted", getPosted().toString())
                .build();
    }

    // unmarshalling
    public static Review create(String json) {
        return create(Json.createReader(new StringReader(json)).readObject());
    }

    public static Review create(JsonObject o) {
        return new Review(
                o.getString("user"),
                o.getInt("rating"),
                o.getString("comment"),
                o.getInt("ID"),
                LocalDate.parse(o.getString("posted")));
    }

    public static Review create(Document d) {
        return new Review(
                d.getString("user"),
                d.getInteger("rating"),
                d.getString("comment"),
                d.getInteger("ID"),
                LocalDate.parse(d.getString("posted")));
    }
}
