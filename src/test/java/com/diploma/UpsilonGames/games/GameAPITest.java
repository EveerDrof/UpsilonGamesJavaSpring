package com.diploma.UpsilonGames.games;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameAPITest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    private static boolean isInitialized = false;
    private static long userId1,userId2;
    @BeforeEach
    public void setUp()throws Exception{
        if(!isInitialized){
            String a ="aaa;";
            a.substring(1);
            HashMap<String, String> userData = new HashMap<>();
            String userName = "GameAPITest";
            String userPassword = "aaaBBB123_asdf_!!asdf";
            userData.put("name", userName);
            userData.put("password", userPassword);
            testRestTemplate.postForEntity("/users/register", userData, null);
            ResponseEntity<HashMap> getUser =  testRestTemplate.getForEntity("/users/"+userData.get("name"), HashMap.class);
            userId1=Long.parseLong((String)getUser.getBody().get("id"));
            userData.put("name", "aasdfasf123123444");
            testRestTemplate.postForEntity("/users/register", userData, null);
            ResponseEntity<HashMap> getUser2 =  testRestTemplate.getForEntity("/users/"+userData.get("name"), HashMap.class);
            userId2=Long.parseLong((String) getUser2.getBody().get("id"));
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
        Game game = new Game("sdfsssAAAA123",222,"Game about island");
        ResponseEntity postResponseEntity = testRestTemplate.postForEntity("/games",game,null);
        Assertions.assertThat(postResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ResponseEntity<HashMap> responseEntity =  testRestTemplate.getForEntity("/games/"+game.getName()+"/short",HashMap.class);
        Object gameId = responseEntity.getBody().get("id");
        ResponseEntity<String> markPostResp = testRestTemplate.postForEntity("/marks?userId="+userId1+"&gameId="+gameId+"&mark=100",null,String.class);
        ResponseEntity markPostResp2 = testRestTemplate.postForEntity("/marks?userId="+userId2+"&gameId="+gameId+"&mark=0",null,String.class);
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
        Game game = new Game("444444AA",222,"Game about island");
        ResponseEntity postResponse = testRestTemplate.postForEntity("/games",game,null);
        Assertions.assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ResponseEntity<HashMap> gameGetResponseEntity = testRestTemplate.getForEntity("/games/"+game.getName()+"/short",HashMap.class);
        testRestTemplate.postForEntity("/marks?userId="+userId2+"&gameId="+gameGetResponseEntity.getBody().get("id")+"&mark=50",null,null);
        testRestTemplate.postForEntity("/marks?userId="+userId1+"&gameId="+gameGetResponseEntity.getBody().get("id")+"&mark=0",null,null);
        ResponseEntity<HashMap> getLongResponse = testRestTemplate.getForEntity("/games/"+game.getName()+"/short",HashMap.class);
        Assertions.assertThat(getLongResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        HashMap body = getLongResponse.getBody();
        Assertions.assertThat(body.get("name")).isEqualTo(game.getName());
        Assertions.assertThat(body.get("price")).isEqualTo(game.getPrice());
        Assertions.assertThat(body.get("averageMark")).isEqualTo(25);
    }
    @Test
    public void getAll(){
        Game game = new Game("getAlltestGame",1654566,"getAlltestGame");
        testRestTemplate.postForEntity("/games",game,null);
        ResponseEntity<String> response = testRestTemplate.getForEntity("/games/allshort", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().indexOf(game.getName())).isNotEqualTo(-1);
    }
}
