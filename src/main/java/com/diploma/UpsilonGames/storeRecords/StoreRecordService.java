package com.diploma.UpsilonGames.storeRecords;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.games.GameService;
import com.diploma.UpsilonGames.users.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreRecordService {
    private StoreRecordRepository storeRecordRepository;
    private GameService gameService;

    public StoreRecordService(StoreRecordRepository storeRecordRepository, GameService gameService){

        this.storeRecordRepository = storeRecordRepository;
        this.gameService = gameService;
    }
    public List<Game> getUserGamesWithType(User userId,StoreRecordType storeRecordType){
        List<Long> gamesIds =  storeRecordRepository.getUserGamesWithType(
                userId,storeRecordType.name()
        );
        return gamesIds.stream().map(id->{
            Game game = gameService.findById(id);
            game.getId();
            return game;
        })
                .collect(Collectors.toList());
    }
    public void save(StoreRecord storeRecord){
        storeRecordRepository.save(storeRecord);
    }
    public void switchUserGamesFromCartToLibrary(User user){
        storeRecordRepository.switchUserGamesType(user,StoreRecordType.IN_CART.name(),
                StoreRecordType.IN_LIBRARY.name());
    }
    public Game findByGameNameAndUser(Game game,User user){
        return storeRecordRepository.findByGameIdAndUserId(game,user);
    }
    public  boolean existsByGameIdAndUserIdAndType(Game game,User user,
                                                   StoreRecordType storeRecordType){
        if(storeRecordRepository.existsByGameIdAndUserIdAndType(
                game,user,storeRecordType.name()) == 1){
            return true;
        } else {
            return false;
        }
    }

    public void deleteFromCart(Game gameId, User userId) {
        storeRecordRepository.deleteByGameIdAndUserId(gameId,userId);
    }
}
