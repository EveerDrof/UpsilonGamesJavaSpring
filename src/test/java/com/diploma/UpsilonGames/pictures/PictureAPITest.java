package com.diploma.UpsilonGames.pictures;

import com.diploma.UpsilonGames.BlobHelper;
import com.diploma.UpsilonGames.TestUtils;
import com.diploma.UpsilonGames.games.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PictureAPITest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    private static boolean initialized = false;
    private static Game game;
    @InjectMocks
    private BlobHelper blobHelper;
    @BeforeEach
    public void setUp(){
        if(!initialized){
            TestUtils.setBlobHelper(blobHelper);
            game = new Game("PictureAPITest game default",222,"PictureAPITest");
            ResponseEntity postResponse = testRestTemplate.postForEntity("/games",game,null);
            initialized = true;
        }
    }
    @Test
    public void getNonExistingShortcutImage_shouldReturnNotFound(){
        ResponseEntity response = testRestTemplate.getForEntity(
                "/pictures/"+game.getName()+"/shortcutImage",
                null
        );
        Assertions.assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }
    @Test
    public void saveScreenshot_shouldReturnCreated() throws Exception{
        ResponseEntity response = testRestTemplate.postForEntity(
                "/pictures/"+game.getName()+"/screenshot",TestUtils.getUniqueTestBytes(),null);
        Assertions.assertEquals(HttpStatus.CREATED,response.getStatusCode());
    }
    @Test
    public void postAndGetScreenshotIds_shouldReturnListOfIds() throws Exception{
        ResponseEntity response = testRestTemplate.postForEntity(
                "/pictures/"+game.getName()+"/screenshot",TestUtils.getUniqueTestBytes(),null);
        Assertions.assertEquals(HttpStatus.CREATED,response.getStatusCode());
        ResponseEntity<Object> getResponse = testRestTemplate.getForEntity(
                "/pictures/"+game.getName()+"/screenshotIDs",
                Object.class
        );
        Assertions.assertEquals(HttpStatus.OK,getResponse.getStatusCode());
        Assertions.assertTrue(((ArrayList<Long>)getResponse.getBody()).size()>0);
    }
    @Test
    public void saveScreenshotAndGetIt_shouldReturnPicture() throws Exception{
        ResponseEntity<Object> initialIdsResponse = testRestTemplate.getForEntity(
                "/pictures/"+game.getName()+"/screenshotIDs",
                Object.class
        );
        List<HashMap<String,Long>> initialPicturesIDs = (List<HashMap<String,Long>>)initialIdsResponse.getBody();
        byte[] bytes = TestUtils.getUniqueTestBytes();
        ResponseEntity postResponse = testRestTemplate.postForEntity(
                "/pictures/"+game.getName()+"/screenshot",bytes,null);
        Assertions.assertEquals(HttpStatus.CREATED,postResponse.getStatusCode());
        ResponseEntity<Object> secondIdsResponse = testRestTemplate.getForEntity(
                "/pictures/"+game.getName()+"/screenshotIDs",
                Object.class
        );
        List<HashMap<String,Long>> secondPicturesIDs = (List<HashMap<String,Long>>)secondIdsResponse.getBody();
        secondPicturesIDs.removeAll(initialPicturesIDs);
        ResponseEntity<byte[]> getResponse = testRestTemplate.getForEntity(
                "/pictures/"+secondPicturesIDs.get(0).get("id"),byte[].class);

        Assertions.assertEquals(HttpStatus.OK,getResponse.getStatusCode());
        Assertions.assertTrue(Arrays.equals(bytes,getResponse.getBody()));
    }
    @Test
    public void saveAndGetShortcut_shouldReturnCreated() throws Exception{
        byte[] bytes = TestUtils.getUniqueTestBytes();
        ResponseEntity postResponse = testRestTemplate.postForEntity(
                "/pictures/"+game.getName()+"/shortcut",bytes,null);
        Assertions.assertEquals(HttpStatus.CREATED,postResponse.getStatusCode());
        ResponseEntity<byte[]> getResponse = testRestTemplate.getForEntity(
                "/pictures/"+game.getName()+"/shortcut",byte[].class);
        Assertions.assertEquals(HttpStatus.OK,getResponse.getStatusCode());
        Assertions.assertTrue(Arrays.equals(getResponse.getBody(),bytes));
    }

}
