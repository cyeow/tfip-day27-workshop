package tfip.day27workshop.model;

import java.io.StringReader;
import java.time.LocalDateTime;
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
    private LocalDateTime posted;
    private List<Comment> edited;

    public Review() {
    }

    public Review(String user, Integer rating, String comment, Integer gameId, LocalDateTime posted) {
        this.user = user;
        this.rating = rating;
        this.comment = comment;
        this.gameId = gameId;
        this.posted = posted;
    }

    public Review(String user, Integer rating, String comment, Integer gameId, LocalDateTime posted,
            List<Comment> edited) {
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

    public LocalDateTime getPosted() {
        return posted;
    }

    public void setPosted(LocalDateTime posted) {
        this.posted = posted;
    }

    public List<Comment> getEdited() {
        return edited;
    }

    public void setEdited(List<Comment> edited) {
        this.edited = edited;
    }

    public Comment getNewestComment() {
        if (getEdited() != null && !getEdited().isEmpty()) {
            return getEdited().stream().max((c1, c2) -> c1.getPosted().compareTo(c2.getPosted())).get();
        }
        return null;
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

    public JsonObject toJSONNewestComment() {

        Comment newestComment = getNewestComment();
        boolean isEdited = true;
        if (newestComment == null) {
            isEdited = false;
            newestComment = new Comment(getComment(), getRating(), getPosted());
        }

        return Json.createObjectBuilder()
                .add("user", getUser())
                .add("rating", newestComment.getRating())
                .add("comment", newestComment.getComment())
                .add("ID", getGameId())
                .add("posted", getPosted().toString())
                .add("edited", isEdited)
                .add("timestamp", LocalDateTime.now().toString())
                .build();

    }

    
    public JsonObject toJSONHistory() {
        
        Comment newestComment = getNewestComment();
        
        if (newestComment != null) {
            Comment originalComment = new Comment(getComment(), getRating(), getPosted());
            addComment(originalComment);
            getEdited().removeIf(c -> c.equals(newestComment));
            setComment(newestComment.getComment());
            setRating(newestComment.getRating());
            setPosted(newestComment.getPosted());
        }

        JsonObjectBuilder ob = Json.createObjectBuilder()
                .add("user", getUser())
                .add("rating", getRating())
                .add("comment", getComment())
                .add("game_id", getGameId())
                .add("posted", getPosted().toString());

        if (getEdited() != null && getEdited().size() > 0) {
            JsonArrayBuilder ab = Json.createArrayBuilder();
            getEdited().sort((c1, c2) -> c1.getPosted().compareTo(c2.getPosted()));
            getEdited().forEach(c -> ab.add(c.toJSON()));
            ob.add("edited", ab);
        }

        return ob.add("timestamp", LocalDateTime.now().toString()).build();
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
                LocalDateTime.parse(o.getString("posted")));

        JsonArray arr = o.getJsonArray("edited");
        if (arr != null) {
            List<Comment> edited = new ArrayList<>();
            arr.forEach(a -> edited.add(Comment.create(a.asJsonObject())));
        }

        return r;
    }

    public static Review create(Document d) {
        List<Document> list = d.getList("edited", Document.class);
        List<Comment> comments = new ArrayList<>();
        if(list != null && list.size() > 0) {
            list.forEach(l -> comments.add(Comment.create(l)));    
        }

        return new Review(
                d.getString("user"),
                d.getInteger("rating"),
                d.getString("comment"),
                d.getInteger("game_id"),
                LocalDateTime.parse(d.getString("posted")),
                comments);
    }

    public void addComment(Comment c) {
        if (getEdited() == null) {
            setEdited(new ArrayList<>());
        }

        getEdited().add(c);
    }


}
