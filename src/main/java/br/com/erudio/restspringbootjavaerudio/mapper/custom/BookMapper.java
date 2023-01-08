package br.com.erudio.restspringbootjavaerudio.mapper.custom;

import br.com.erudio.restspringbootjavaerudio.data.vo.v1.BookVOV1;
import br.com.erudio.restspringbootjavaerudio.data.vo.v2.PersonVOV2;
import br.com.erudio.restspringbootjavaerudio.model.Book;
import br.com.erudio.restspringbootjavaerudio.model.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

import static br.com.erudio.restspringbootjavaerudio.util.BookUtil.formataValorParaRealString;

@Service
public class BookMapper {

    public BookVOV1 convertEntityToVo(Book book) {
        BookVOV1 vo = new BookVOV1();
        vo.setKey(book.getId());
        vo.setAuthor(book.getAuthor());
        vo.setLaunchDate(book.getLaunchDate());
        vo.setPrice(book.getPrice());
        vo.setTitle(book.getTitle());
        return vo;
    }

    public Book convertVoToEntity(BookVOV1 bookVOV1) {
        Book entity = new Book();
        entity.setId(bookVOV1.getKey());
        entity.setAuthor(bookVOV1.getAuthor());
        entity.setLaunchDate(bookVOV1.getLaunchDate());
        entity.setPrice(bookVOV1.getPrice());
        entity.setTitle(bookVOV1.getTitle());
        return entity;
    }

}
