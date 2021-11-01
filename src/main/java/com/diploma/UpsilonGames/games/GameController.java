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

    @GetMapping("/{gameName}/short")
    public ResponseEntity getGameShort(@PathVariable String gameName) {
        Game game = gameService.findByName(gameName);
        if (game == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("name", game.getName());
        map.put("price", Double.toString(game.getPrice()));
        map.put("averageMark", Double.toString(9.5));
        map.put("id", Long.toString(game.getId()));
        return new ResponseEntity(map, HttpStatus.OK);
    }

    @GetMapping("/{gameName}/long")
    public ResponseEntity getGameLong(@PathVariable String gameName) {
        Game game = gameService.findByName(gameName);
        if (game == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(game, HttpStatus.OK);
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
