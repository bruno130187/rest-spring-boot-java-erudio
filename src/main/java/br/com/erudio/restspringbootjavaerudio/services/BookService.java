package br.com.erudio.restspringbootjavaerudio.services;

import br.com.erudio.restspringbootjavaerudio.controllers.BookController;
import br.com.erudio.restspringbootjavaerudio.controllers.PersonController;
import br.com.erudio.restspringbootjavaerudio.data.vo.v1.BookVO;
import br.com.erudio.restspringbootjavaerudio.data.vo.v1.PersonVO;
import br.com.erudio.restspringbootjavaerudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.restspringbootjavaerudio.exceptions.ResourceNotFoundException;
import br.com.erudio.restspringbootjavaerudio.mapper.DozerMapper;
import br.com.erudio.restspringbootjavaerudio.mapper.custom.BookMapper;
import br.com.erudio.restspringbootjavaerudio.model.Book;
import br.com.erudio.restspringbootjavaerudio.repositories.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
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
    PagedResourcesAssembler<BookVO> pagedResourcesAssembler;

    @Autowired
    BookMapper bookMapper;

    public BookVO create(BookVO bookVO) {
        if (bookVO == null) throw new RequiredObjectIsNullException();
        logger.info("Creating one Person!");
        var entity = DozerMapper.parseObject(bookVO, Book.class);
        var vo =  DozerMapper.parseObject(bookRepository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO bookVO) {
        if (bookVO == null) throw new RequiredObjectIsNullException();
        logger.info("Updating one Book!");
        var entity = bookRepository.findById(bookVO.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        entity.setAuthor(bookVO.getAuthor());
        entity.setLaunchDate(bookVO.getLaunchDate());
        entity.setPrice(bookVO.getPrice());
        entity.setTitle(bookVO.getTitle());
        var vo =  DozerMapper.parseObject(bookRepository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one Book!");
        var entity = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        bookRepository.delete(entity);
    }

    public List<BookVO> findAllSimple() {
        logger.info("Finding all Books!");
        var booksVO = DozerMapper.parseListObjects(bookRepository.findAllSimple(), BookVO.class);
        booksVO.stream().forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
        return booksVO;
    }

    public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {
        logger.info("Finding all Books!");
        var bookPage = bookRepository.findAll(pageable);
        var bookVosPage = bookPage.map(p -> DozerMapper.parseObject(p, BookVO.class));
        bookVosPage.map(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
        Link Link = linkTo(methodOn(BookController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageNumber(), "asc")).withSelfRel();
        return pagedResourcesAssembler.toModel(bookVosPage, Link);
    }

    public PagedModel<EntityModel<BookVO>> findBooksByTitle(String title, Pageable pageable) {
        logger.info("Finding Books By Title!");
        var bookPage = bookRepository.findBooksByTitle(title, pageable);
        var booksVosPage = bookPage.map(b -> DozerMapper.parseObject(b, BookVO.class));
        booksVosPage.map(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel()));
        Link Link = linkTo(methodOn(BookController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageNumber(), "asc")).withSelfRel();
        return pagedResourcesAssembler.toModel(booksVosPage, Link);
    }

    public BookVO findById(Long id) {
        logger.info("Finding one Book!");
        var entity = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var vo = DozerMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    @Transactional
    public BookVO disabledBook(Long id, Boolean enabled) {
        logger.info("Disabling-Enabling one Book!");
        bookRepository.disabledBook(id, enabled);
        var entity = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var vo = DozerMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

}
