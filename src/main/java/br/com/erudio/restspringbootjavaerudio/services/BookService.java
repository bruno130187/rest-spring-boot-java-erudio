package br.com.erudio.restspringbootjavaerudio.services;

import br.com.erudio.restspringbootjavaerudio.controllers.BookController;
import br.com.erudio.restspringbootjavaerudio.data.vo.v1.BookVOV1;
import br.com.erudio.restspringbootjavaerudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.restspringbootjavaerudio.exceptions.ResourceNotFoundException;
import br.com.erudio.restspringbootjavaerudio.mapper.DozerMapper;
import br.com.erudio.restspringbootjavaerudio.mapper.custom.BookMapper;
import br.com.erudio.restspringbootjavaerudio.model.Book;
import br.com.erudio.restspringbootjavaerudio.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {
    private final Logger logger = Logger.getLogger(BookService.class.getName());

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookMapper bookMapper;

    public BookVOV1 create(BookVOV1 bookVOV1) {
        if (bookVOV1 == null) throw new RequiredObjectIsNullException();
        logger.info("Creating one Person!");
        var entity = DozerMapper.parseObject(bookVOV1, Book.class);
        var vo =  DozerMapper.parseObject(bookRepository.save(entity), BookVOV1.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVOV1 update(BookVOV1 bookVOV1) {
        if (bookVOV1 == null) throw new RequiredObjectIsNullException();
        logger.info("Updating one Book!");
        var entity = bookRepository.findById(bookVOV1.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        entity.setAuthor(bookVOV1.getAuthor());
        entity.setLaunchDate(bookVOV1.getLaunchDate());
        entity.setPrice(bookVOV1.getPrice());
        entity.setTitle(bookVOV1.getTitle());
        var vo =  DozerMapper.parseObject(bookRepository.save(entity), BookVOV1.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one Book!");
        var entity = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        bookRepository.delete(entity);
    }

    public List<BookVOV1> findAll() {
        logger.info("Finding all Books!");
        var booksVOV1 = DozerMapper.parseListObjects(bookRepository.findAll(), BookVOV1.class);
        booksVOV1.stream().forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
        return booksVOV1;
    }

    public BookVOV1 findById(Long id) {
        logger.info("Finding one Book!");
        var entity = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var vo = DozerMapper.parseObject(entity, BookVOV1.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

}
