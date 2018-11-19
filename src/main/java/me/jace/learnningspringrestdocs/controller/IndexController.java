package me.jace.learnningspringrestdocs.controller;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public ResourceSupport index() {
        ResourceSupport index = new ResourceSupport();
        index.add(linkTo(BookController.class).withRel("books"));
        return index;
    }


}
