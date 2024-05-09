package com.hendisantika.springbootrestapipostgresql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hendisantika.springbootrestapipostgresql.controller.AuthorRestController;
import com.hendisantika.springbootrestapipostgresql.entity.Author;
import com.hendisantika.springbootrestapipostgresql.repository.AuthorRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

class AuthorRestControllerTest {

    private AuthorRestController authorController;
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        authorController = new AuthorRestController(); // Instantiate the controller
        authorRepository = mock(AuthorRepository.class); // Create a mock instance of AuthorRepository
        authorController.setRepository(authorRepository); // Set the mock repository
    }

    @Test
    void testAddAuthor() {
        Author authorToAdd = new Author("John Doe", "Some description", Arrays.asList("tag1", "tag2"));
        Author savedAuthor = new Author("John Doe", "Some description", Arrays.asList("tag1", "tag2"));

        when(authorRepository.save(authorToAdd)).thenReturn(savedAuthor);

        ResponseEntity<Author> response = authorController.addAuthor(authorToAdd);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedAuthor, response.getBody());
    }

    @Test
    void testGetAllAuthors() {
        Author author1 = new Author("John Doe", "Some description", Arrays.asList("tag1", "tag2"));
        Author author2 = new Author("Jane Smith", "Another description", Arrays.asList("tag3", "tag4"));

        when(authorRepository.findAll()).thenReturn(Arrays.asList(author1, author2));

        ResponseEntity<Collection<Author>> response = authorController.getAllAuthors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsAll(Arrays.asList(author1, author2)));
    }

    @Test
    void testGetAuthorWithId() {
        long authorId = 1L;
        Author author = new Author("John Doe", "Some description", Arrays.asList("tag1", "tag2"));

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        ResponseEntity<Author> response = authorController.getAuthorWithId(authorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(author, response.getBody());

        // Test for author not found
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        response = authorController.getAuthorWithId(authorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFindAuthorWithName() {
        String name = "John Doe";
        Author author = new Author("John Doe", "Some description", Arrays.asList("tag1", "tag2"));

        when(authorRepository.findByName(name)).thenReturn(Arrays.asList(author));

        ResponseEntity<Collection<Author>> response = authorController.findAuthorWithName(name);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(author));
    }

    @Test
    void testUpdateAuthorFromDB() {
        long authorId = 1L;
        Author currentAuthor = new Author("John Doe", "Some description", Arrays.asList("tag1", "tag2"));
        Author updatedAuthor = new Author("Updated Name", "Updated description", Arrays.asList("tag1", "tag2"));

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(currentAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

        ResponseEntity<Author> response = authorController.updateAuthorFromDB(authorId, updatedAuthor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedAuthor, response.getBody());

        // Test for author not found
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        response = authorController.updateAuthorFromDB(authorId, updatedAuthor);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteAuthorWithId() {
        long authorId = 1L;

        ResponseEntity<Void> response = authorController.deleteAuthorWithId(authorId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(authorRepository, times(1)).deleteById(authorId);
    }

    @Test
    void testDeleteAllAuthors() {
        ResponseEntity<Void> response = authorController.deleteAllAuthors();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(authorRepository, times(1)).deleteAll();
    }
}

