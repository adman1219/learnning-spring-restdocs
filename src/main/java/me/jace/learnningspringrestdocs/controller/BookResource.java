package me.jace.learnningspringrestdocs.controller;

import lombok.Getter;
import me.jace.learnningspringrestdocs.domain.Book;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class BookResource extends ResourceSupport {
    private final Book book;

    BookResource(Book book) {
        add(linkTo(BookController.class).slash(book.getId()).withSelfRel());
        add(linkTo(methodOn(BookController.class).getBooks()).withRel("books"));
        this.book = book;
    }
}
