package me.jace.learnningspringrestdocs.controller;

import lombok.RequiredArgsConstructor;
import me.jace.learnningspringrestdocs.domain.Book;
import me.jace.learnningspringrestdocs.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookResource create(@RequestBody BookInput bookInput) {
        Book book = bookService.create(bookMapper.getBook(bookInput));
        return new BookResource(book);
    }

    @PutMapping("{id}")
    public BookResource edit(@PathVariable Long id, @RequestBody BookInput bookInput) {
        Book book = bookService.edit(id, bookMapper.getBook(bookInput));
        return new BookResource(book);
    }

    @GetMapping("{id}")
    public BookResource getBook(@PathVariable Long id) {
        Book book = bookService.getBookBy(id);
        return new BookResource(book);
    }

    @GetMapping("all")
    public List<BookResource> getBooks() {
        List<Book> books = bookService.getAllBooks();
        return books.stream()
                .map(BookResource::new)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<BookResource> getBooksByAuthor(@RequestParam String author) {
        List<Book> books = bookService.getBooksByAuthor(author);
        return books.stream()
                .map(BookResource::new)
                .collect(Collectors.toList());
    }
}
