package br.com.erudio.restspringbootjavaerudio.repositories;

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
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Modifying
    @Query("UPDATE Person p SET p.enabled =:enabled WHERE p.id =:id")
    void disabledPerson(@Param("id") Long id, @Param("enabled") Boolean enabled);

    @Query("SELECT p FROM Person p")
    List<Person> findAllSimple();

    @Query("SELECT p FROM Person p WHERE p.firstName LIKE LOWER ( CONCAT('%',:firstName,'%'))")
    Page<Person> findPeopleByName(@Param("firstName") String firstName, Pageable pageable);

}
