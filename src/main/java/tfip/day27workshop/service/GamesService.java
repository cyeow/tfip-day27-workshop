package tfip.day27workshop.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tfip.day27workshop.model.Game;
import tfip.day27workshop.model.Games;
import tfip.day27workshop.repository.GamesRepository;

@Service
public class GamesService {

    @Autowired
    GamesRepository repo;

    public Games getGames(Integer offset, Integer limit) {
        return new Games(
                repo.getGameList(offset, limit),
                offset,
                limit,
                LocalDateTime.now());
    }

    public Games getGamesByRank(Integer offset, Integer limit) {
        return new Games(
                repo.getGameListByRank(offset, limit),
                offset,
                limit,
                LocalDateTime.now());
    }

    public Game getGameById(String gameId) {
        return repo.getGameById(gameId);
    }

    public boolean isValidGameId(Integer gameId) {
        if(getGameById(gameId.toString()) != null)  {
            return true;
        }
        return false;
    }

}
