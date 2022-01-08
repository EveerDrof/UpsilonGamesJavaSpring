package com.diploma.UpsilonGames.tags;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "tags")
public class TagController {
    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }
    @GetMapping("/all-tags")
    public ResponseEntity getAllTags() {
        return new ResponseEntity(tagService.findAll(), HttpStatus.OK);
    }
    @PostMapping("/{tagName}")
    public ResponseEntity postTag(@PathVariable String tagName) {
        if(tagService.exists(tagName)){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        tagService.save(new Tag(tagName));
        return new ResponseEntity(HttpStatus.OK);
    }
}
