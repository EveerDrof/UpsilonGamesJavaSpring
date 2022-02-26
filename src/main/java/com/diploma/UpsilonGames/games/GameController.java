package com.diploma.UpsilonGames.games;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping(value = "games")
public class GameController {
    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    private HashMap<String, Object> gameToSmallHashMap(Game game) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", game.getName());
        map.put("price", game.getPrice());
        map.put("id", game.getId());
        map.put("discountPrice", game.getDiscountPrice());
        map.put("averageMark", gameService.getAvgMarkByGameId(game));
        return map;
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

    private HashMap getFullGameData(Game game) {
        HashMap map = gameToSmallHashMap(game);
        map.put("description", game.getDescription());
        map.put("tags", game.getTags());
        map.put("foreignReviewsDataSteam", game.getForeignReviewsDataSteam());
        return map;
    }

    @GetMapping("/{gameName}/long")
    public ResponseEntity getGameLong(@PathVariable String gameName) {
        Game game = gameService.findByName(gameName);
        if (game == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(getFullGameData(game), HttpStatus.OK);
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

    @GetMapping("/allshort")
    public ResponseEntity findAll() {
        ArrayList<HashMap<String, Object>> records = new ArrayList<>();
        gameService.findAll().stream().forEach((game) -> {
            HashMap<String, Object> record = gameToSmallHashMap(game);
            records.add(record);
        });
        return new ResponseEntity(records, HttpStatus.OK);
    }

    @GetMapping("/selection")
    public ResponseEntity getSelection(@RequestParam String tags, @RequestParam double maxPrice,
                                       @RequestParam double minPrice, @RequestParam byte minMark,
                                       @RequestParam String namePart,
                                       @RequestParam double minDiscountPercent,
                                       @RequestParam String sortType,
                                       @RequestParam int limit) {
        String[] tagsArr = new String[0];
        if (!Objects.equals(tags, "")) {
            tagsArr = tags.split(",");
        }
        return new ResponseEntity(gameService.select(tagsArr, maxPrice, minPrice, minMark,
                        namePart, minDiscountPercent, sortType, limit)
                .stream().map((game) -> {
                    return getFullGameData(game);
                }), HttpStatus.OK);
    }
}
