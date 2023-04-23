package tfip.day27workshop.model;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Review {

    private String user;
    private Integer rating;
    private String comment;
    private Integer gameId;
    private LocalDate posted;
    private List<Comment> edited;

    public Review() {
    }

    public Review(String user, Integer rating, String comment, Integer gameId, LocalDate posted) {
        this.user = user;
        this.rating = rating;
        this.comment = comment;
        this.gameId = gameId;
        this.posted = posted;
    }

    public Review(String user, Integer rating, String comment, Integer gameId, LocalDate posted, List<Comment> edited) {
        this.user = user;
        this.rating = rating;
        this.comment = comment;
        this.gameId = gameId;
        this.posted = posted;
        this.edited = edited;
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

    public List<Comment> getEdited() {
        return edited;
    }

    public void setEdited(List<Comment> edited) {
        this.edited = edited;
    }

    @Override
    public String toString() {
        return "Review [user=" + user + ", rating=" + rating + ", comment=" + comment + ", gameId=" + gameId
                + ", posted=" + posted + ", edited=" + edited + "]";
    }

    // marshalling

    public String toJSONString() {
        return toJSON().toString();
    }

    public JsonObject toJSON() {
        JsonObjectBuilder ob = Json.createObjectBuilder()
                .add("user", getUser())
                .add("rating", getRating())
                .add("comment", getComment())
                .add("game_id", getGameId())
                .add("posted", getPosted().toString());

        if (getEdited() != null) {
            JsonArrayBuilder ab = Json.createArrayBuilder();
            getEdited().forEach(c -> ab.add(c.toJSON()));
            ob.add("edited", ab);
        }

        return ob.build();
    }

    public Document toDocument() {
        return Document.parse(toJSONString());
    }

    // unmarshalling

    public static Review create(String json) {
        return create(Json.createReader(new StringReader(json)).readObject());
    }

    public static Review create(JsonObject o) {
        Review r = new Review(
                o.getString("user"),
                o.getInt("rating"),
                o.getString("comment"),
                o.getInt("game_id"),
                LocalDate.parse(o.getString("posted")));

        JsonArray arr = o.getJsonArray("edited");
        if(arr != null) {
            List<Comment> edited = new ArrayList<>();
            arr.forEach(a -> edited.add(Comment.create(a.asJsonObject())));    
        }

        return r;
    }

    public static Review create(Document d) {
        List<Document> list = d.getList("edited", Document.class);
        List<Comment> comments = new ArrayList<>();
        list.forEach(l -> comments.add(Comment.create(l)));
        
        return new Review(
                d.getString("user"),
                d.getInteger("rating"),
                d.getString("comment"),
                d.getInteger("game_id"),
                LocalDate.parse(d.getString("posted")),
                comments);
    }

    public void addComment(Comment c) {
        if(getEdited() == null) {
            setEdited(new ArrayList<>());
        }

        getEdited().add(c);
    }

}
