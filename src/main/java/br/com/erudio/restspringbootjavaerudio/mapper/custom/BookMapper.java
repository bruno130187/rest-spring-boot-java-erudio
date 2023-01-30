package br.com.erudio.restspringbootjavaerudio.mapper.custom;

import br.com.erudio.restspringbootjavaerudio.data.vo.v1.BookVO;
import br.com.erudio.restspringbootjavaerudio.model.Book;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public BookVO convertEntityToVo(Book book) {
        BookVO vo = new BookVO();
        vo.setKey(book.getId());
        vo.setAuthor(book.getAuthor());
        vo.setLaunchDate(book.getLaunchDate());
        vo.setPrice(book.getPrice());
        vo.setTitle(book.getTitle());
        return vo;
    }

    public Book convertVoToEntity(BookVO bookVO) {
        Book entity = new Book();
        entity.setId(bookVO.getKey());
        entity.setAuthor(bookVO.getAuthor());
        entity.setLaunchDate(bookVO.getLaunchDate());
        entity.setPrice(bookVO.getPrice());
        entity.setTitle(bookVO.getTitle());
        return entity;
    }

}
