package com.diploma.UpsilonGames.storeRecords;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.games.GameService;
import com.diploma.UpsilonGames.users.User;
import com.diploma.UpsilonGames.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "store/")
public class StoreRecordController {
    private UserService userService;
    private GameService gameService;
    private StoreRecordService storeRecordService;

    @Autowired
    public StoreRecordController(UserService userService, GameService gameService,
                                 StoreRecordService storeRecordService) {

        this.userService = userService;
        this.gameService = gameService;
        this.storeRecordService = storeRecordService;
    }

    public ResponseEntity checkUserAndGetGames(Principal principal, StoreRecordType storeRecordType) {
        if (!userService.existsByName(principal.getName())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByName(principal.getName());
        List<Game> result = storeRecordService.getUserGamesWithType(user, storeRecordType);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("cart/")
    public ResponseEntity addGameToCart(@RequestParam String gameName, Principal principal) {
        if (!userService.existsByName(principal.getName())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByName(principal.getName());
        if (!gameService.existsByName(gameName)) {
            return new ResponseEntity("Game not found", HttpStatus.NOT_FOUND);
        }
        Game game = gameService.findByName(gameName);
        try {
            storeRecordService.save(new StoreRecord(user, game, StoreRecordType.IN_CART));
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity("Duplicate record", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("cart/")
    public ResponseEntity getUserCart(Principal principal) {
        return checkUserAndGetGames(principal, StoreRecordType.IN_CART);
    }

    @PostMapping("buyCart/")
    public ResponseEntity buyCart(Principal principal) {
        if (!userService.existsByName(principal.getName())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByName(principal.getName());
        storeRecordService.switchUserGamesFromCartToLibrary(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("library/")
    public ResponseEntity getUserLibrary(Principal principal) {
        return checkUserAndGetGames(principal, StoreRecordType.IN_LIBRARY);
    }

    @GetMapping("checkGameInLibrary/{gameName}")
    public ResponseEntity checkIfGameInLibrary(Principal principal, @PathVariable String gameName) {
        if (!userService.existsByName(principal.getName())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        if (!gameService.existsByName(gameName)) {
            return new ResponseEntity("Game not found", HttpStatus.NOT_FOUND);
        }
        Game game = gameService.findByName(gameName);
        User user = userService.findByName(principal.getName());
        if(storeRecordService
                .existsByGameIdAndUserIdAndType(game, user, StoreRecordType.IN_CART))
        {
            return new ResponseEntity("inCart",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
        }
        if(storeRecordService
                .existsByGameIdAndUserIdAndType(game, user, StoreRecordType.IN_LIBRARY)
        ) {
            return new ResponseEntity("inLibrary",HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
        }
    }
    @PostMapping("cart/remove")
    public ResponseEntity deleteFromCart(@RequestParam String gameName,Principal principal){
        if (!userService.existsByName(principal.getName())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        if (!gameService.existsByName(gameName)) {
            return new ResponseEntity("Game not found", HttpStatus.NOT_FOUND);
        }
        Game game = gameService.findByName(gameName);
        User user = userService.findByName(principal.getName());
        try{
            storeRecordService.deleteFromCart(game,user);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity("No such record",HttpStatus.BAD_REQUEST);
        }
    }
}
