package com.diploma.UpsilonGames.users;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAPITest {
    private HashMap<String,String> requestBody;
    private String userName = "abc";
    @Autowired
    TestRestTemplate testRestTemplate;
    @BeforeEach
    public void setUp(){
        requestBody = new HashMap<>();
    }
    private void setPasswordAndName(String password,String userName){
        requestBody.put("name",userName);
        requestBody.put("password",password);
    }
    private void testPassword(String password, String errorMessage, HttpStatus httpStatus){
        testPassword(password,errorMessage,httpStatus,"abc");
    }
    private void testPassword(String password, String errorMessage, HttpStatus httpStatus,String userName){
        setPasswordAndName(password,userName);
        ResponseEntity<String> responseEntity = testRestTemplate
                .postForEntity("/users/register",requestBody,String.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(httpStatus);
        if(errorMessage != null){
            Assertions.assertThat(responseEntity.getBody()).isEqualTo(errorMessage);
        }
    }
    @Test
    public void postUserWithCorrectPassword_shouldReturnCreated() throws Exception{
        testPassword(
                "bcadddaasfsf!0A",
                null,
                HttpStatus.CREATED
        );
    }
    @Test
    public void postUserWithTooShortPassword_shouldReturnBadRequest() throws Exception{
        testPassword(
                "!0Aa",
                "Password is too short.It must have at least 10 symbols",
                HttpStatus.BAD_REQUEST
        );
    }
    @Test
    public void postUserWithPasswordWithoutDigits_shouldReturnBadRequest() throws Exception{
        testPassword(
                "AsadfafadsfasdfAasdasdASdasa!",
                "Password has no digits.It must have at least 1 digit",
                HttpStatus.BAD_REQUEST
        );
    }
    @Test
    public void postUserWithPasswordWithoutSpecialSymbols_shouldReturnCreated() throws Exception{
        testPassword(
                "AsadfafadsfasdfAasdasdASd0999asa",
                "Password has no special symbols.It must contain at least one of the following: !_-'\"+=()%$#@~` ",
                HttpStatus.BAD_REQUEST
        );
    }
    @Test
    public void postUserWithPasswordWithoutCapitalLetters_shouldReturnCreated() throws Exception{
        testPassword(
                "aaaaaaaaaaaa1110!",
                "Password has no capital letters.It must contain at least one capital letter",
                HttpStatus.BAD_REQUEST
        );
    }
    @Test
    public void postUserWithPasswordWithoutSmallLetters_shouldReturnCreated() throws Exception{
        testPassword(
                "ABCDFEDEDEDE1110!",
                "Password has no small letters.It must contain at least one small letter",
                HttpStatus.BAD_REQUEST
        );
    }
    @Test
    public void getNonExistingUser_shouldReturnNotFound(){
        setPasswordAndName("aaaBBBB123_asd123","9");
        ResponseEntity<String> responseEntity = testRestTemplate
                .getForEntity("/users/adfasdfasfd",String.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    public void getExistingUser_shouldReturnUser(){
        setPasswordAndName("aaaBBBB123_asd123","10");
        ResponseEntity postResponseEntity = testRestTemplate
                .postForEntity("/users/register",requestBody,String.class);
        Assertions.assertThat(postResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ResponseEntity<HashMap> getResponseEntity = testRestTemplate
                .getForEntity("/users/"+userName,HashMap.class);
        Assertions.assertThat(getResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(getResponseEntity.getBody().containsKey("id")).isTrue();
    }
}
