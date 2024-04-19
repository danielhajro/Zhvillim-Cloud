package com.hendisantika.springbootrestapipostgresql.controller;

import java.util.Collection;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hendisantika.springbootrestapipostgresql.entity.Author;
import com.hendisantika.springbootrestapipostgresql.repository.AuthorRepository;

@RestController
@RequestMapping("/api/authors")
public class AuthorRestController {
	
	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(AuthorRestController.class);

    @Autowired
    private AuthorRepository repository;

    @PostMapping
    public ResponseEntity<Author> addAuthor(@RequestBody Author author) {
    	logger.info("Adding new Author");
        Author savedAuthor = repository.save(author);
        return new ResponseEntity<>(savedAuthor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Collection<Author>> getAllAuthors() {
    	logger.info("Getting all Authors from the repository");
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorWithId(@PathVariable Long id) {
    	logger.info("Getting Author with id: " + id);
        Optional<Author> authorOptional = repository.findById(id);
        if (authorOptional.isPresent()) {
            return new ResponseEntity<>(authorOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(params = {"name"})
    public ResponseEntity<Collection<Author>> findAuthorWithName(@RequestParam(value = "name") String name) {
    	logger.info("Getting Author with name: " + name);
    	return new ResponseEntity<>(repository.findByName(name), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthorFromDB(@PathVariable("id") long id, @RequestBody Author author) {
    	logger.info("Updating Author with id: " + id);
        Optional<Author> currentAuthorOpt = repository.findById(id);
        if (currentAuthorOpt.isPresent()) {
            Author currentAuthor = currentAuthorOpt.get();
            currentAuthor.setName(author.getName());
            currentAuthor.setDescription(author.getDescription());
            currentAuthor.setTags(author.getTags());
            Author updatedAuthor = repository.save(currentAuthor);
            return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
        } else {
        	logger.warn("Author with id: " + id + " could not be found.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthorWithId(@PathVariable Long id) {
    	logger.info("Deleting Author with id: " + id);
        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllAuthors() {
    	logger.info("Deleting all authors in the repository");
        repository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}