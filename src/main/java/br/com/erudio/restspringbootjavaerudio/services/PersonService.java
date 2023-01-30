package br.com.erudio.restspringbootjavaerudio.services;

import br.com.erudio.restspringbootjavaerudio.controllers.PersonController;
import br.com.erudio.restspringbootjavaerudio.data.vo.v1.PersonVO;
import br.com.erudio.restspringbootjavaerudio.data.vo.v2.PersonVOV2;
import br.com.erudio.restspringbootjavaerudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.restspringbootjavaerudio.exceptions.ResourceNotFoundException;
import br.com.erudio.restspringbootjavaerudio.mapper.DozerMapper;
import br.com.erudio.restspringbootjavaerudio.mapper.custom.PersonMapper;
import br.com.erudio.restspringbootjavaerudio.model.Person;
import br.com.erudio.restspringbootjavaerudio.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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

    public PersonVO create(PersonVO person) {
        if (person == null) throw new RequiredObjectIsNullException();
        logger.info("Creating one Person!");
        var entity = DozerMapper.parseObject(person, Person.class);
        var vo =  DozerMapper.parseObject(personRepository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVOV2 createV2(PersonVOV2 person) {
        if (person == null) throw new RequiredObjectIsNullException();
        logger.info("Creating one Person with V2!");
        var entity = personMapper.convertVoToEntity(person);
        var vo = personMapper.convertEntityToVo(personRepository.save(entity));
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO person) {
        if (person == null) throw new RequiredObjectIsNullException();
        logger.info("Updating one Person!");
        var entity = personRepository.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        var vo =  DozerMapper.parseObject(personRepository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one Person!");
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        personRepository.delete(entity);
    }

    public List<PersonVO> findAll() {
        logger.info("Finding all People!");
        var personsVOV1 = DozerMapper.parseListObjects(personRepository.findAll(), PersonVO.class);
        personsVOV1.stream().forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        return personsVOV1;
    }

    public PersonVO findById(Long id) {
        logger.info("Finding one Person!");
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var vo = DozerMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    @Transactional
    public PersonVO disabledPerson(Long id, Boolean enabled) {
        logger.info("Disabling one Person!");
        personRepository.disabledPerson(id, enabled);
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var vo = DozerMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

}
