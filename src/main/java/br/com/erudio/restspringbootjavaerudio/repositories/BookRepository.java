package br.com.erudio.restspringbootjavaerudio.repositories;

import br.com.erudio.restspringbootjavaerudio.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {



}
