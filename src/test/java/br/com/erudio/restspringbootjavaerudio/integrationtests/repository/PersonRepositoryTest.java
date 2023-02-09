package br.com.erudio.restspringbootjavaerudio.integrationtests.repository;

import br.com.erudio.restspringbootjavaerudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.restspringbootjavaerudio.integrationtests.vo.PersonVO;
import br.com.erudio.restspringbootjavaerudio.model.Person;
import br.com.erudio.restspringbootjavaerudio.repositories.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("classpath:testcontainers.properties")
public class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PersonRepository personRepository;

    private static Person person;

    @BeforeAll
    public static void setup() {
        person = new Person();
    }

    @Test
    @Order(1)
    public void testFindByName() throws JsonMappingException, JsonProcessingException {

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        person = personRepository.findPeopleByName("and", pageable).getContent().get(0);

        assertNotNull(person);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertTrue(person.getEnabled());

        assertTrue(person.getId() > 0);

        assertEquals("Wayland", person.getFirstName());
        assertEquals("Thamelt", person.getLastName());
        assertEquals("337 Everett Park", person.getAddress());
        assertEquals("Male", person.getGender());
    }

    @Test
    @Order(2)
    public void testDisablePerson() throws JsonMappingException, JsonProcessingException {

        personRepository.disabledPerson(person.getId(), false);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        person = personRepository.findPeopleByName("and", pageable).getContent().get(0);

        assertNotNull(person);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertFalse(person.getEnabled());

        assertTrue(person.getId() > 0);

        assertEquals("Wayland", person.getFirstName());
        assertEquals("Thamelt", person.getLastName());
        assertEquals("337 Everett Park", person.getAddress());
        assertEquals("Male", person.getGender());
    }

}
