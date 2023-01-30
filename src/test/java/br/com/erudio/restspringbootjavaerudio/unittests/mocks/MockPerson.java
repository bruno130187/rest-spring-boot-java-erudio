package br.com.erudio.restspringbootjavaerudio.unittests.mocks;

import br.com.erudio.restspringbootjavaerudio.data.vo.v1.PersonVO;
import br.com.erudio.restspringbootjavaerudio.data.vo.v2.PersonVOV2;
import br.com.erudio.restspringbootjavaerudio.model.Person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockPerson {

    public Person mockEntity() {
        return mockEntity(0);
    }

    public PersonVO mockVO() {
        return mockVO(0);
    }

    public PersonVOV2 mockVOV2() {
        return mockVOV2(0);
    }

    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<Person>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<PersonVO> mockVOList() {
        List<PersonVO> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockVO(i));
        }
        return persons;
    }

    public List<PersonVOV2> mockVOV2List() {
        List<PersonVOV2> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockVOV2(i));
        }
        return persons;
    }

    public Person mockEntity(Integer number) {
        Person person = new Person();
        person.setAddress("Addres Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setGender(((number % 2)==0) ? "Male" : "Female");
        person.setId(number.longValue());
        person.setLastName("Last Name Test" + number);
        person.setEnabled(((number % 2)==0) ? true : false);
        return person;
    }

    public PersonVO mockVO(Integer number) {
        PersonVO personVO = new PersonVO();
        personVO.setAddress("Addres Test" + number);
        personVO.setFirstName("First Name Test" + number);
        personVO.setGender(((number % 2)==0) ? "Male" : "Female");
        personVO.setKey(number.longValue());
        personVO.setLastName("Last Name Test" + number);
        personVO.setEnabled(((number % 2)==0) ? true : false);
        return personVO;
    }

    public PersonVOV2 mockVOV2(Integer number) {
        PersonVOV2 person = new PersonVOV2();
        person.setAddress("Addres Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setGender(((number % 2)==0) ? "Male" : "Female");
        person.setKey(number.longValue());
        person.setLastName("Last Name Test" + number);
        person.setBirthday(new Date());
        person.setEnabled(((number % 2)==0) ? true : false);
        return person;
    }

}
