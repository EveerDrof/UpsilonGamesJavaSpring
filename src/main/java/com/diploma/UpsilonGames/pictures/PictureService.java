package com.diploma.UpsilonGames.pictures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PictureService {
    private PictureRepository pictureRepository;
    @Autowired
    public PictureService(PictureRepository pictureRepository){
        this.pictureRepository = pictureRepository;
    }
    public Picture save(Picture picture){
        return pictureRepository.save(picture);
    }
    public Picture findById(long id){
        return pictureRepository.findById(id).get();
    }

    public ArrayList<Picture> findAll() {
        return pictureRepository.findAll();
    }
}
