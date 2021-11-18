package com.diploma.UpsilonGames;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.pictures.Picture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Random;

public class TestUtils {

    private static SessionFactory sessionFactory;
    private static BlobHelper blobHelper;
    public static void setSessionFactory(SessionFactory sf){
        sessionFactory = sf;
    }
    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            System.out.println(jsonContent);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static int bytesLen = 1000;
    public static byte[] getUniqueTestBytes(){
        Random random = new Random();
        byte[] bytes = new byte[bytesLen];
        random.nextBytes(bytes);
        return bytes;
    }
    public static void setBlobHelper(BlobHelper blobHelper){
        TestUtils.blobHelper = blobHelper;
    }
    public static Picture getUniqueTestPicture(Game game) throws Exception {
        if (blobHelper == null) {
            blobHelper = new BlobHelper(sessionFactory);
        }
        byte[] bytes =getUniqueTestBytes();
        InputStream is = new ByteArrayInputStream(bytes);
        Blob blob = blobHelper.createBlob(is, bytes.length);
        Picture result = new Picture(blob, game);
        blob.length();
        bytesLen += 1;
        return result;
    }
}
