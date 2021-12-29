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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "store/")
public class StoreRecordController {
    private UserService userService;
    private GameService gameService;
    private StoreRecordService storeRecordService;

    @Autowired
    public StoreRecordController(UserService userService, GameService gameService,
                                 StoreRecordService storeRecordService){

        this.userService = userService;
        this.gameService = gameService;
        this.storeRecordService = storeRecordService;
    }
    public ResponseEntity checkUserAndGetGames(Principal principal,StoreRecordType storeRecordType){
        if(!userService.existsByName(principal.getName())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByName(principal.getName());
        List<Game> result = storeRecordService.getUserGamesWithType(user,storeRecordType);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    @PostMapping("cart/")
    public ResponseEntity addGameToCart(@RequestParam String gameName, Principal principal){
        if(!userService.existsByName(principal.getName())){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByName(principal.getName());
        if(!gameService.existsByName(gameName)){
            return new ResponseEntity("Game not found",HttpStatus.NOT_FOUND);
        }
        Game game = gameService.findByName(gameName);
        storeRecordService.save(new StoreRecord(user,game,StoreRecordType.IN_CART));
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping("cart/")
    public ResponseEntity getUserCart(Principal principal){
        return  checkUserAndGetGames(principal,StoreRecordType.IN_CART);
    }
    @PostMapping("buyCart/")
    public ResponseEntity buyCart(Principal principal){
        if(!userService.existsByName(principal.getName())){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByName(principal.getName());
        storeRecordService.switchUserGamesFromCartToLibrary(user);
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping("library/")
    public ResponseEntity getUserLibrary(Principal principal){
        return  checkUserAndGetGames(principal,StoreRecordType.IN_LIBRARY);
    }
}
