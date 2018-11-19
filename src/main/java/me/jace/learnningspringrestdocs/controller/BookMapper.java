package me.jace.learnningspringrestdocs.controller;

import me.jace.learnningspringrestdocs.domain.Book;
import me.jace.learnningspringrestdocs.domain.Contact;
import me.jace.learnningspringrestdocs.domain.Publisher;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public Book getBook(BookInput bookInput) {
        Book book = new Book();
        book.setSubject(bookInput.getSubject());
        book.setAuthor(bookInput.getAuthor());
        book.setDescription(bookInput.getDescription());
        book.setPublisher(getPublisher(bookInput.getPublisher()));
        return book;
    }

    private Publisher getPublisher(PublisherInput pInput) {
        Publisher publisher = new Publisher();
        publisher.setName(pInput.getName());
        publisher.setContact(getContact(pInput.getContact()));
        return publisher;
    }

    private Contact getContact(ContactInput inputContact) {
        Contact contact = new Contact();
        contact.setAddress(inputContact.getAddress());
        contact.setPhone(inputContact.getPhone());
        return contact;
    }
}
