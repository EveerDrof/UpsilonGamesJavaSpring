package com.diploma.UpsilonGames.games;

import com.diploma.UpsilonGames.users.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameAPITest {
    @Autowired
    TestRestTemplate testRestTemplate;
    private static boolean isInitialized = false;
    @BeforeEach
    public void setUp()throws Exception{
        if(!isInitialized){
            HashMap<String, String> userData = new HashMap<>();
            String userName = "michael";
            String userPassword = "aaaBBB123__!!asdf";
            userData.put("name", "michael");
            userData.put("password", userPassword);
            testRestTemplate.postForEntity("/users/register", userData, String.class).getStatusCode();
            userData.put("name", "aasdfasf");
            testRestTemplate.postForEntity("/users/register", userData, String.class).getStatusCode();
            isInitialized = true;
        }
    }
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
        ResponseEntity responseEntity = testRestTemplate.postForEntity("/games",game,null);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        testRestTemplate.postForEntity("/marks?userId=1&gameId=2&mark=100",game,null);
        testRestTemplate.postForEntity("/marks?userId=2&gameId=2&mark=0",game,null);
        ResponseEntity<HashMap> longResponse = testRestTemplate.getForEntity("/games/"+game.getName()+"/long",HashMap.class);
        Assertions.assertThat(longResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        HashMap<String,Object> body = longResponse.getBody();
        Assertions.assertThat(body.get("name")).isEqualTo(game.getName());
        Assertions.assertThat(body.get("description")).isEqualTo(game.getDescription());
        Assertions.assertThat(body.get("price")).isEqualTo(game.getPrice());
        Assertions.assertThat(body.get("averageMark")).isEqualTo(50);
    }
    @Test
    public void addAndGetShort() throws Exception{
        Game game = new Game("Crysis",222,"Game about island");
        ResponseEntity<Game> responseEntity = testRestTemplate.postForEntity("/games",game,Game.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        testRestTemplate.postForEntity("/marks?userId=2&gameId=1&mark=50",game,null);
        testRestTemplate.postForEntity("/marks?userId=1&gameId=1&mark=0",game,null);
        ResponseEntity<HashMap> getLongResponse = testRestTemplate.getForEntity("/games/"+game.getName()+"/short",HashMap.class);
        Assertions.assertThat(getLongResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        HashMap body = getLongResponse.getBody();
        Assertions.assertThat(body.get("name")).isEqualTo(game.getName());
        Assertions.assertThat(body.get("price")).isEqualTo(game.getPrice());
        Assertions.assertThat(body.get("averageMark")).isEqualTo(25);
    }
}
