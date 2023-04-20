package tfip.day27workshop.repository;

import java.util.List;
import java.util.NoSuchElementException;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import tfip.day27workshop.model.Game;

@Repository
public class GamesRepository {

    @Autowired
    MongoTemplate mongo;

    public List<Game> getGameList(Integer offset, Integer limit) {
        return getGameListByField(offset, limit, "name", Direction.ASC);
    }

    public List<Game> getGameListByRank(Integer offset, Integer limit) {
        return getGameListByField(offset, limit, "ranking", Direction.ASC);
    }

    // abstraction of the game list by ranking/name/year
    private List<Game> getGameListByField(Integer offset, Integer limit, String fieldName, Direction dir) {
        Query q = new Query().skip(offset).limit(limit);
        q.with(Sort.by(dir, fieldName));

        // the following lines are for offset by page number (offset # of records will
        // be calculated using pageNumber * limit)
        // Pageable p = PageRequest.of(pageNumber, limit);
        // q.with(p);

        return mongo.find(q, Document.class, "games").stream().map(d -> Game.create(d)).toList();
    }

    public Game getGameById(String gameId) {
        // if game not found return null

        Query q = new Query();

        if(ObjectId.isValid(gameId)) {
            q.addCriteria(Criteria.where("_id").is(gameId));            
        } else {
            System.out.println("gameId is " + gameId + " parsed: " + Integer.parseInt(gameId));
            q.addCriteria(Criteria.where("gid").is(Integer.parseInt(gameId)));
        }

        try {
            return mongo.find(q, Document.class, "games").stream().map(d -> Game.create(d)).findFirst().get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

}
