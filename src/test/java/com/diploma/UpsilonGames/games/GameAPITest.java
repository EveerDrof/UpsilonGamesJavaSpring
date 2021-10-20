package com.diploma.UpsilonGames.games;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameAPITest {
    @Autowired
    TestRestTemplate testRestTemplate;
    @Test
    public void getShortNonExistentGame_shouldReturnNotFould() throws Exception{
        ResponseEntity<HashMap> responseEntity = testRestTemplate
                .getForEntity("/games/aaaaaaa/short",null,HashMap.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    public void getLongNonExistentGame_shouldReturnNotFould() throws Exception{
        ResponseEntity<Game> responseEntity = testRestTemplate
                .getForEntity("/games/aaaaaaa/long",null,Game.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    public void addAndGetLong() throws Exception{
        Game game = new Game("Far cry",222,"Game about island");
        ResponseEntity<Game> responseEntity = testRestTemplate.postForEntity("/games",game,Game.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ResponseEntity<Game> getLongResponse = testRestTemplate.getForEntity("/games/"+game.getName()+"/long",Game.class);
        Assertions.assertThat(getLongResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Game body = getLongResponse.getBody();
        Assertions.assertThat(body.getName()).isEqualTo(game.getName());
        Assertions.assertThat(body.getDescription()).isEqualTo(game.getDescription());
        Assertions.assertThat(body.getPrice()).isEqualTo(game.getPrice());
    }
    @Test
    public void addAndGetShort() throws Exception{
        Game game = new Game("Crysis",222,"Game about island");
        ResponseEntity<Game> responseEntity = testRestTemplate.postForEntity("/games",game,Game.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ResponseEntity<HashMap> getLongResponse = testRestTemplate.getForEntity("/games/"+game.getName()+"/short",HashMap.class);
        Assertions.assertThat(getLongResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        HashMap body = getLongResponse.getBody();
        Assertions.assertThat(body.get("name")).isEqualTo(game.getName());
        Assertions.assertThat(body.get("price")).isEqualTo(Double.toString(game.getPrice()));
    }
}
