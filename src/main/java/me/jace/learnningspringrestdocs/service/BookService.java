package me.jace.learnningspringrestdocs.service;

import lombok.RequiredArgsConstructor;
import me.jace.learnningspringrestdocs.domain.Book;
import me.jace.learnningspringrestdocs.domain.BookRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    @Transactional
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public Book edit(Long id, Book book) {
        book.setId(id);
        return bookRepository.save(book);
    }

    public Book getBookBy(Long id) {
        return bookRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findBooksByAuthor(author);
    }

}
