package br.com.erudio.restspringbootjavaerudio.services;

import br.com.erudio.restspringbootjavaerudio.data.vo.v1.PersonVOV1;
import br.com.erudio.restspringbootjavaerudio.data.vo.v2.PersonVOV2;
import br.com.erudio.restspringbootjavaerudio.exceptions.ResourceNotFoundException;
import br.com.erudio.restspringbootjavaerudio.mapper.DozerMapper;
import br.com.erudio.restspringbootjavaerudio.mapper.custom.PersonMapper;
import br.com.erudio.restspringbootjavaerudio.model.Person;
import br.com.erudio.restspringbootjavaerudio.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonService {
    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonMapper personMapper;

    public PersonVOV1 create(PersonVOV1 person) {
        logger.info("Creating one Person!");
        var entity = DozerMapper.parseObject(person, Person.class);
        var vo =  DozerMapper.parseObject(personRepository.save(entity), PersonVOV1.class);
        return vo;
    }

    public PersonVOV2 createV2(PersonVOV2 person) {
        logger.info("Creating one Person with V2!");
        var entity = personMapper.convertVoToEntity(person);
        var vo = personMapper.convertEntityToVo(personRepository.save(entity));
        return vo;
    }

    public PersonVOV1 update(PersonVOV1 person) {
        logger.info("Updating one Person!");
        var entity = personRepository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        var vo =  DozerMapper.parseObject(personRepository.save(entity), PersonVOV1.class);
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one Person!");
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        personRepository.delete(entity);
    }

    public List<PersonVOV1> findAll() {
        logger.info("Finding all People!");
        return DozerMapper.parseListObjects(personRepository.findAll(), PersonVOV1.class);
    }

    public PersonVOV1 findById(Long id) {
        logger.info("Finding one Person!");
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        return DozerMapper.parseObject(entity, PersonVOV1.class);
    }

}
