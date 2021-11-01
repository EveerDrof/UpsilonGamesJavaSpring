package com.diploma.UpsilonGames.marks;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class MarkAPITest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    private static boolean initialized = false;
    Game game;
    User user;
    HashMap<String, String> userData;
    @BeforeEach
    public void setUp() throws Exception{
        if(!initialized) {
            userData = new HashMap<>();
            String userName = "michael";
            String userPassword = "aaaBBB123__!!asdf";
            userData.put("name", userName);
            userData.put("password", userPassword);
            user = new User(userName,userPassword);
            testRestTemplate.postForEntity("/users/register", userData, String.class).getStatusCode();
            game = new Game("Crysis", 2000, "Cool game");
            testRestTemplate.postForEntity("/games/", game, null);
            initialized = true;
        }
    }
    public void testPostStatusEndText(String url, String responseText, HttpStatus status){
        ResponseEntity responseEntity = testRestTemplate
                .postForEntity(url,null,String.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(status);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(responseText);
    }
    public void testGetStatusEndText(String url, String responseText, HttpStatus status){
        ResponseEntity responseEntity = testRestTemplate
                .getForEntity(url,String.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(status);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(responseText);
    }
    @Test
    public void postCorrectMark_shouldReturnCreated() throws Exception{
        testPostStatusEndText(
                "/marks?userId=1&gameId=1&mark=70",
                "Successfully added new mark",
                HttpStatus.CREATED
        );
    }
    @Test
    public void postMark_withIncorrectUserId_shouldReturnBadRequest() throws Exception{
        testPostStatusEndText(
                "/marks?userId=safdasfd&gameId=1&mark=70",
                "User id is incorrect",
                HttpStatus.BAD_REQUEST
        );
    }
    @Test
    public void postMark_withNonExistentUser_shouldReturnBadRequestWithText() throws Exception{
        testPostStatusEndText(
                "/marks?userId=999&gameId=1&mark=70",
                "User not found",
                HttpStatus.NOT_FOUND
        );
    }
    @Test
    public void postMark_withIncorrectGameId_shouldBadRequest() throws Exception{
        testPostStatusEndText(
                "/marks?userId=1&gameId=adsasda&mark=70",
                "Game id is incorrect",
                HttpStatus.BAD_REQUEST
        );
    }
    @Test
    public void postMark_withNonExistentGame_shouldReturnNotFoundWithText() throws Exception{
        testPostStatusEndText(
                "/marks?userId=1&gameId=9999&mark=70",
                "Game not found",
                HttpStatus.NOT_FOUND
        );
    }
        @Test
    public void postMark_withIncorrectMarkValue_shouldReturnBadRequestWithText() throws Exception{
        testPostStatusEndText(
                "/marks?userId=1&gameId=1&mark=asdffasd",
                "Wrong mark value",
                HttpStatus.BAD_REQUEST
        );
    }
    @Test
    public void postMark_withTooHighMarkValue_shouldReturnBadRequestWithText() throws Exception{
        testPostStatusEndText(
                "/marks?userId=1&gameId=1&mark=123",
                "Mark value is too high",
                HttpStatus.BAD_REQUEST
        );
    }
    @Test
    public void postMark_withTooLowMarkValue_shouldReturnBadRequestWithText() throws Exception{
        testPostStatusEndText(
                "/marks?userId=1&gameId=1&mark=-100",
                "Mark value is too low",
                HttpStatus.BAD_REQUEST
        );
    }
    @Test
    public void getNonExistentMarkByNonExistentUserId_shouldReturnNotFoundWithText() throws Exception{
        testGetStatusEndText(
                "/marks?userId=100&gameId=1",
                "User not found",
                HttpStatus.NOT_FOUND
        );
    }
    @Test
    public void getNonExistentMarkByNonExistentGameId_shouldReturnNotFoundWithText() throws Exception{
        testGetStatusEndText(
                "/marks?userId=1&gameId=999",
                "Game not found",
                HttpStatus.NOT_FOUND
        );
    }
    @Test
    public void getMarkByWrongGameId_shouldReturnBadRequestWithText() throws Exception{
        testGetStatusEndText(
                "/marks?userId=1&gameId=asfsdfsfd",
                "Game id is incorrect",
                HttpStatus.BAD_REQUEST
        );
    }
    @Test
    public void getMarkByWrongUserId_shouldReturnBadRequestWithText() throws Exception{
        testGetStatusEndText(
                "/marks?userId=asdfasfsf&gameId=1",
                "User id is incorrect",
                HttpStatus.BAD_REQUEST
        );
    }
    @Test
    public void getMarkByCorrectValues_shouldReturnMarkData() throws Exception{
        ResponseEntity<Byte> responseEntity = testRestTemplate
                .getForEntity("/marks?userId=1&gameId=1",byte.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Byte mark = responseEntity.getBody();
        Mark createdMark = new Mark((byte)70,game,user);
        Assertions.assertThat(mark).isEqualTo(createdMark.getMark());
    }
}
