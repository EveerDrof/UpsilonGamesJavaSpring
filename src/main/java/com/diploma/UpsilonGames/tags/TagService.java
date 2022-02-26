package com.diploma.UpsilonGames.tags;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class TagService {
    private TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public ArrayList<Tag> findAll(){
        return new ArrayList<>(tagRepository.findAll());
    }

    public boolean exists(String tagName) {
        return tagRepository.existsByName(tagName);
    }

    public void save(Tag tag) {
        tagRepository.save(tag);
    }
}
