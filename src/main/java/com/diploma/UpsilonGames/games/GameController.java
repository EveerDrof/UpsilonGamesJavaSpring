package com.diploma.UpsilonGames.games;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "games")
public class GameController {
    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }
    private HashMap<String,Object> gameToSmallHashMap(Game game){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", game.getName());
        map.put("price", game.getPrice());
        map.put("id", game.getId());
        map.put("averageMark",gameService.getAvgMarkByGameId(game));
        return  map;
    }
    @GetMapping("/{gameName}/short")
    public ResponseEntity getGameShort(@PathVariable String gameName) {
        Game game = gameService.findByName(gameName);
        if (game == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        HashMap map = gameToSmallHashMap(game);
        return new ResponseEntity(map, HttpStatus.OK);
    }

    @GetMapping("/{gameName}/long")
    public ResponseEntity getGameLong(@PathVariable String gameName) {
        Game game = gameService.findByName(gameName);
        if (game == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        HashMap map = gameToSmallHashMap(game);
        map.put("description",game.getDescription());
        return new ResponseEntity(map, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity postGame(@RequestBody Game game) {
        Game returned = gameService.save(game);
        System.out.println(returned);
        if (returned != null) {
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
