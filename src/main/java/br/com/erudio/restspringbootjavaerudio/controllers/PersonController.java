package br.com.erudio.restspringbootjavaerudio.controllers;

import br.com.erudio.restspringbootjavaerudio.data.vo.v1.PersonVOV1;
import br.com.erudio.restspringbootjavaerudio.data.vo.v2.PersonVOV2;
import br.com.erudio.restspringbootjavaerudio.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonVOV1> findAll() {
        return personService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonVOV1 findById(@PathVariable(value = "id") Long id) {
        return personService.findById(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonVOV1 create(@RequestBody PersonVOV1 person) {
        return personService.create(person);
    }

    @PostMapping(value = "/v2",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonVOV2 createV2(@RequestBody PersonVOV2 person) {
        return personService.createV2(person);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonVOV1 update(@RequestBody PersonVOV1 person) {
        return personService.update(person);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
