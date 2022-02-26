package com.diploma.UpsilonGames.pictures;

import com.diploma.UpsilonGames.BlobHelper;
import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.games.GameService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Blob;

@RestController
@RequestMapping(value = "pictures")
public class PictureController {
    private PictureService pictureService;
    private BlobHelper blobHelper;
    private GameService gameService;

    @Autowired
    public PictureController(PictureService pictureService, BlobHelper blobHelper, GameService gameService) {
        this.pictureService = pictureService;
        this.blobHelper = blobHelper;
        this.gameService = gameService;
    }

    @RequestMapping(value = "/{gameName}/screenshot", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public ResponseEntity addPicture(@PathVariable String gameName, HttpServletRequest httpServletRequest) {
        Blob blob;
        try {
            blob = blobHelper.createBlob(httpServletRequest.getInputStream(), httpServletRequest.getContentLength());
        } catch (Exception ex) {
            return new ResponseEntity("Creating blob error", HttpStatus.BAD_REQUEST);
        }
        Game game = gameService.findByName(gameName);
        if (game == null) {
            return new ResponseEntity("Game not found", HttpStatus.BAD_REQUEST);
        }
        Picture picture = new Picture(blob, game);
        Picture result = pictureService.save(picture);
        if (result == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{gameName}/screenshotIDs")
    public ResponseEntity getScreenshotIDs(@PathVariable String gameName) {
        return new ResponseEntity(pictureService.findByGameId(gameService.findByName(gameName)), HttpStatus.OK);
    }

    @GetMapping("/{pictureId}")
    public void findById(@PathVariable long pictureId, HttpServletResponse response) throws Exception {
        Picture picture = pictureService.findById(pictureId);
        if (picture == null) {
            response.setStatus(404);
        } else {
            IOUtils.copy(picture.getData().getBinaryStream(), response.getOutputStream());
        }
    }

    @GetMapping("/{gameName}/shortcut")
    public void getScreenshot(@PathVariable String gameName, HttpServletResponse response) throws Exception {
        Picture picture = gameService.getShortcutByGameName(gameName);
        if (picture == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            IOUtils.copy(picture.getData().getBinaryStream(), response.getOutputStream());
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{gameName}/shortcut", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public ResponseEntity postScreenshot(@PathVariable String gameName, HttpServletRequest httpServletRequest) {
        Game game = gameService.findByName(gameName);
        if (game == null) {
            return new ResponseEntity("Game not found", HttpStatus.NOT_FOUND);
        }
        Blob blob;
        try {
            blob = blobHelper.createBlob(httpServletRequest.getInputStream(), httpServletRequest.getContentLength());
        } catch (Exception ex) {
            return new ResponseEntity("Creating blob error", HttpStatus.BAD_REQUEST);
        }
        Picture picture = new Picture(blob, game);
        picture = pictureService.save(picture);
        if (picture == null) {
            return new ResponseEntity(HttpStatus.CREATED);
        }
        game.setShortcut(picture);
        gameService.save(game);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
