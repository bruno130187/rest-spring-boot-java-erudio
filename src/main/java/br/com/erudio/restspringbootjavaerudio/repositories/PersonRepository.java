package br.com.erudio.restspringbootjavaerudio.repositories;

import br.com.erudio.restspringbootjavaerudio.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {



}
