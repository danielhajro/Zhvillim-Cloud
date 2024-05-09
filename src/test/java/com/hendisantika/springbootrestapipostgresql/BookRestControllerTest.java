package com.hendisantika.springbootrestapipostgresql;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hendisantika.springbootrestapipostgresql.controller.BookRestController;
import com.hendisantika.springbootrestapipostgresql.entity.Book;
import com.hendisantika.springbootrestapipostgresql.repository.BookRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

class BookRestControllerTest {

    private BookRestController bookController;
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookController = new BookRestController(); // Instantiate the controller
        bookRepository = mock(BookRepository.class); // Create a mock instance of BookRepository
        bookController.setRepository(bookRepository); // Set the mock repository
    }

    @Test
    void testAddBook() {
        Book bookToAdd = new Book("Book Title", "Book Description", Arrays.asList("Tag1", "Tag2"));
        Book savedBook = new Book("Book Title", "Book Description", Arrays.asList("Tag1", "Tag2"));

        when(bookRepository.save(bookToAdd)).thenReturn(savedBook);

        ResponseEntity<?> response = bookController.addBook(bookToAdd);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedBook, response.getBody());
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book("Book 1", "Description 1", Arrays.asList("Tag1", "Tag2"));
        Book book2 = new Book("Book 2", "Description 2", Arrays.asList("Tag3", "Tag4"));

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        ResponseEntity<Collection<Book>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsAll(Arrays.asList(book1, book2)));
    }

    @Test
    void testGetBookWithId() {
        long bookId = 1L;
        Book book = new Book("Book Title", "Book Description", Arrays.asList("Tag1", "Tag2"));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        ResponseEntity<Book> response = bookController.getBookWithId(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());

        // Test for book not found
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        response = bookController.getBookWithId(bookId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFindBookWithName() {
        String name = "Book Title";
        Book book = new Book("Book Title", "Book Description", Arrays.asList("Tag1", "Tag2"));

        when(bookRepository.findByName(name)).thenReturn(Arrays.asList(book));

        ResponseEntity<Collection<Book>> response = bookController.findBookWithName(name);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(book));
    }

    @Test
    void testUpdateBookFromDB() {
        long bookId = 1L;
        Book currentBook = new Book("Book Title", "Book Description", Arrays.asList("Tag1", "Tag2"));
        Book updatedBook = new Book("Updated Title", "Updated Description", Arrays.asList("Tag1", "Tag2"));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(currentBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        ResponseEntity<Book> response = bookController.updateBookFromDB(bookId, updatedBook);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBook, response.getBody());

        // Test for book not found
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        response = bookController.updateBookFromDB(bookId, updatedBook);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteBookWithId() {
        long bookId = 1L;

        ResponseEntity<Void> response = bookController.deleteBookWithId(bookId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testDeleteAllBooks() {
        ResponseEntity<Void> response = bookController.deleteAllBooks();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(bookRepository, times(1)).deleteAll();
    }
}
