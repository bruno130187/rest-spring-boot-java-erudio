package br.com.erudio.restspringbootjavaerudio.repositories;

import br.com.erudio.restspringbootjavaerudio.model.Book;
import br.com.erudio.restspringbootjavaerudio.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Query("UPDATE Book b SET b.enabled =:enabled WHERE b.id =:id")
    void disabledBook(@Param("id") Long id, @Param("enabled") Boolean enabled);

    @Query("SELECT b FROM Book b")
    List<Book> findAllSimple();

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER ( CONCAT ('%', :title ,'%'))")
    Page<Book> findBooksByTitle(@Param("title") String title, Pageable pageable);

}
