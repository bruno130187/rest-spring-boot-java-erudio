package br.com.erudio.restspringbootjavaerudio.unittests.mockito.services;

import br.com.erudio.restspringbootjavaerudio.data.vo.v1.PersonVOV1;
import br.com.erudio.restspringbootjavaerudio.data.vo.v2.PersonVOV2;
import br.com.erudio.restspringbootjavaerudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.restspringbootjavaerudio.mapper.custom.PersonMapper;
import br.com.erudio.restspringbootjavaerudio.model.Person;
import br.com.erudio.restspringbootjavaerudio.repositories.PersonRepository;
import br.com.erudio.restspringbootjavaerudio.services.PersonService;
import br.com.erudio.restspringbootjavaerudio.unittests.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    MockPerson mockPerson;

    @InjectMocks
    private PersonService personService;

    @Mock
    PersonRepository personRepository;

    @BeforeEach
    void setUpMocks() {

        mockPerson = new MockPerson();
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void create() {
        Person person = mockPerson.mockEntity(1);
        Person persisted = person;
        persisted.setId(1L);

        PersonVOV1 personVOV1 = mockPerson.mockVO(1);
        personVOV1.setKey(1L);
        when(personRepository.save(person)).thenReturn(persisted);

        var result = personService.create(personVOV1);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            personService.create(null);
        });
        String expectedMessage = "It is not allowed to persist an null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createV2() {

    }
    @Test
    void update() {
        Person person = mockPerson.mockEntity(1);
        person.setId(1L);

        Person persisted = person;
        persisted.setId(1L);

        PersonVOV1 personVOV1 = mockPerson.mockVO(1);
        personVOV1.setKey(1L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.save(person)).thenReturn(persisted);

        var result = personService.update(personVOV1);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            personService.update(null);
        });
        String expectedMessage = "It is not allowed to persist an null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        Person person = mockPerson.mockEntity(1);
        person.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        personService.delete(1L);
    }

    @Test
    void findAll() {
        List<Person> personList = mockPerson.mockEntityList();

        when(personRepository.findAll()).thenReturn(personList);
        var people = personService.findAll();
        assertNotNull(people);
        assertEquals(14, people.size());

        var personVOV1one = people.get(1);
        assertNotNull(personVOV1one);
        assertNotNull(personVOV1one.getKey());
        assertNotNull(personVOV1one.getLinks());

        assertTrue(personVOV1one.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", personVOV1one.getAddress());
        assertEquals("First Name Test1", personVOV1one.getFirstName());
        assertEquals("Last Name Test1", personVOV1one.getLastName());
        assertEquals("Female", personVOV1one.getGender());

        var personVOV1four = people.get(4);
        assertNotNull(personVOV1four);
        assertNotNull(personVOV1four.getKey());
        assertNotNull(personVOV1four.getLinks());

        assertTrue(personVOV1four.toString().contains("links: [</api/person/v1/4>;rel=\"self\"]"));
        assertEquals("Addres Test4", personVOV1four.getAddress());
        assertEquals("First Name Test4", personVOV1four.getFirstName());
        assertEquals("Last Name Test4", personVOV1four.getLastName());
        assertEquals("Male", personVOV1four.getGender());

        var personVOV1seven = people.get(7);
        assertNotNull(personVOV1seven);
        assertNotNull(personVOV1seven.getKey());
        assertNotNull(personVOV1seven.getLinks());

        assertTrue(personVOV1seven.toString().contains("links: [</api/person/v1/7>;rel=\"self\"]"));
        assertEquals("Addres Test7", personVOV1seven.getAddress());
        assertEquals("First Name Test7", personVOV1seven.getFirstName());
        assertEquals("Last Name Test7", personVOV1seven.getLastName());
        assertEquals("Female", personVOV1seven.getGender());
    }

    @Test
    void findById() {
        Person person = mockPerson.mockEntity(1);
        person.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        var result = personService.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }
}