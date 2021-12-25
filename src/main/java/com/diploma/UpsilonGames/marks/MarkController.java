package com.diploma.UpsilonGames.marks;

import com.diploma.UpsilonGames.IMarkAcceptableService;
import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.games.GameService;
import com.diploma.UpsilonGames.users.User;
import com.diploma.UpsilonGames.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "marks")
public class MarkController {
    private MarkService markService;
    private IMarkAcceptableService<User> userService;
    private IMarkAcceptableService<Game> gameService;

    @Autowired
    public MarkController(MarkService markService,UserService userService,GameService gameService) {
        this.markService = markService;
        this.userService = userService;
        this.gameService = gameService;
    }

    private long parseId(String id, String errorMessage) throws MarkException {
        long longId = -1;
        try {
            longId = Long.parseLong(id);
        } catch (Exception ex) {
            throw new MarkException(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return longId;
    }

    private <T> T checkAndFind(long id, IMarkAcceptableService<T> service, String errorMessage) throws MarkException {
        if (!service.existsById(id)) {
            throw new MarkException(errorMessage, HttpStatus.NOT_FOUND);
        }
        return service.findById(id);
    }
    private List getUserAndGame(String userId,String gameId)throws MarkException{
        long longUserId = parseId(userId, "User id is incorrect");
        long longGameId = parseId(gameId, "Game id is incorrect");
        User user = checkAndFind(longUserId, userService, "User not found");
        Game game = checkAndFind(longGameId, gameService, "Game not found");
        return Arrays.asList(user,game);
    }
    @PostMapping
    public ResponseEntity addMark(@RequestParam String userId, @RequestParam String gameId,
                                  @RequestParam String mark, Principal principal) {
        if(((UserService)userService).findByName(principal.getName()).getId() != Long.parseLong(userId)){
            return new ResponseEntity("It is not your id",HttpStatus.UNAUTHORIZED);
        }
        byte byteMark = -1;
        try {
            byteMark = Byte.parseByte(mark);
        } catch (Exception ex) {
            return new ResponseEntity("Wrong mark value", HttpStatus.BAD_REQUEST);
        }
        if (byteMark > 100) {
            return new ResponseEntity("Mark value is too high", HttpStatus.BAD_REQUEST);
        }
        if (byteMark < 0) {
            return new ResponseEntity("Mark value is too low", HttpStatus.BAD_REQUEST);
        }
        try {
            List result = getUserAndGame(userId,gameId);
            User user = (User)result.get(0);
            Game game = (Game)result.get(1);
            markService.save(new  Mark(byteMark,game,user));
            return new ResponseEntity("Successfully added new mark", HttpStatus.CREATED);
        } catch (MarkException markException) {
            return new ResponseEntity(markException.getMessage(), markException.getStatus());
        }catch (Exception exception) {
            return new ResponseEntity("Duplicate entry", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity getMark(@RequestParam String userId, @RequestParam String gameId) {
        try {
            List result = getUserAndGame(userId,gameId);
            User user = (User)result.get(0);
            Game game = (Game)result.get(1);
            Byte mark = markService.findMarkByUserIdAndGameId(user,game);
            return new ResponseEntity(mark, HttpStatus.OK);
        } catch (MarkException markException) {
            return new ResponseEntity(markException.getMessage(), markException.getStatus());
        }
    }
}
