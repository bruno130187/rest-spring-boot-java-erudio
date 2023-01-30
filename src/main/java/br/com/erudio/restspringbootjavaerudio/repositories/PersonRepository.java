package br.com.erudio.restspringbootjavaerudio.repositories;

import br.com.erudio.restspringbootjavaerudio.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Modifying
    @Query("UPDATE Person p SET p.enabled =:enabled WHERE p.id =:id")
    void disabledPerson(@Param("id") Long id, @Param("enabled") Boolean enabled);

}
