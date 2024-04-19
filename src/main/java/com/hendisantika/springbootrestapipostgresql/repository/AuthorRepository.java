package com.hendisantika.springbootrestapipostgresql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.hendisantika.springbootrestapipostgresql.entity.Author;
import com.hendisantika.springbootrestapipostgresql.entity.Book;

@RepositoryRestResource
public interface AuthorRepository extends JpaRepository<Author, Long> {
    java.util.List<Author> findByName(String name);
}
