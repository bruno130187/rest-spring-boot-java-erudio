package br.com.erudio.restspringbootjavaerudio.unittests.mockito.services;

import br.com.erudio.restspringbootjavaerudio.data.vo.v1.PersonVO;
import br.com.erudio.restspringbootjavaerudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.restspringbootjavaerudio.model.Person;
import br.com.erudio.restspringbootjavaerudio.repositories.PersonRepository;
import br.com.erudio.restspringbootjavaerudio.services.PersonService;
import br.com.erudio.restspringbootjavaerudio.unittests.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.IanaLinkRelations;

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
    @Order(1)
    void create() {
        Person person = mockPerson.mockEntity(1);
        Person persisted = person;
        persisted.setId(1L);

        PersonVO personVO = mockPerson.mockVO(1);
        personVO.setKey(1L);
        when(personRepository.save(person)).thenReturn(persisted);

        var result = personService.create(personVO);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(String.valueOf(result.getRequiredLink(IanaLinkRelations.SELF.value())).contains("</api/person/v1/1>;rel=\"self\""));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    @Order(2)
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            personService.create(null);
        });
        String expectedMessage = "It is not allowed to persist an null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(3)
    void createV2() {

    }
    @Test
    @Order(4)
    void update() {
        Person person = mockPerson.mockEntity(1);
        person.setId(1L);

        Person persisted = person;
        persisted.setId(1L);

        PersonVO personVO = mockPerson.mockVO(1);
        personVO.setKey(1L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.save(person)).thenReturn(persisted);

        var result = personService.update(personVO);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(String.valueOf(result.getRequiredLink(IanaLinkRelations.SELF.value())).contains("</api/person/v1/1>;rel=\"self\""));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    @Order(5)
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            personService.update(null);
        });
        String expectedMessage = "It is not allowed to persist an null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(6)
    void findById() {
        Person person = mockPerson.mockEntity(1);
        person.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        var result = personService.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(String.valueOf(result.getRequiredLink(IanaLinkRelations.SELF.value())).contains("</api/person/v1/1>;rel=\"self\""));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    @Order(7)
    void findAllSimple() {
        List<Person> personList = mockPerson.mockEntityList();

        when(personRepository.findAllSimple()).thenReturn(personList);
        var people = personService.findAllSimple();
        assertNotNull(people);
        assertEquals(14, people.size());

        var personVOone = people.get(1);
        assertNotNull(personVOone);
        assertNotNull(personVOone.getKey());
        assertNotNull(personVOone.getLinks());

        assertTrue(String.valueOf(personVOone.getRequiredLink(IanaLinkRelations.SELF.value())).contains("</api/person/v1/1>;rel=\"self\""));
        assertEquals("Addres Test1", personVOone.getAddress());
        assertEquals("First Name Test1", personVOone.getFirstName());
        assertEquals("Last Name Test1", personVOone.getLastName());
        assertEquals("Female", personVOone.getGender());

        var personVOfour = people.get(4);
        assertNotNull(personVOfour);
        assertNotNull(personVOfour.getKey());
        assertNotNull(personVOfour.getLinks());

        assertTrue(String.valueOf(personVOfour.getRequiredLink(IanaLinkRelations.SELF.value())).contains("</api/person/v1/4>;rel=\"self\""));
        assertEquals("Addres Test4", personVOfour.getAddress());
        assertEquals("First Name Test4", personVOfour.getFirstName());
        assertEquals("Last Name Test4", personVOfour.getLastName());
        assertEquals("Male", personVOfour.getGender());

        var personVOseven = people.get(7);
        assertNotNull(personVOseven);
        assertNotNull(personVOseven.getKey());
        assertNotNull(personVOseven.getLinks());

        assertTrue(String.valueOf(personVOseven.getRequiredLink(IanaLinkRelations.SELF.value())).contains("</api/person/v1/7>;rel=\"self\""));
        assertEquals("Addres Test7", personVOseven.getAddress());
        assertEquals("First Name Test7", personVOseven.getFirstName());
        assertEquals("Last Name Test7", personVOseven.getLastName());
        assertEquals("Female", personVOseven.getGender());
    }

    @Test
    @Order(8)
    void delete() {
        Person person = mockPerson.mockEntity(1);
        person.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        personService.delete(1L);
    }
}