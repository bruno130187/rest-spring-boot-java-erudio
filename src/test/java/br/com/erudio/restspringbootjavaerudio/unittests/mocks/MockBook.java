package br.com.erudio.restspringbootjavaerudio.unittests.mocks;

import br.com.erudio.restspringbootjavaerudio.data.vo.v1.BookVO;
import br.com.erudio.restspringbootjavaerudio.model.Book;

import java.math.BigDecimal;
import java.util.*;

public class MockBook {

    public Book mockEntity() {
        return mockEntity(0);
    }

    public BookVO mockVO() {
        return mockVO(0);
    }

    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
        }
        return books;
    }

    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number.longValue());
        book.setAuthor("Author Test" + number);
        GregorianCalendar gc = new GregorianCalendar(2023, 01, 05);
        book.setLaunchDate(gc.getTime());
        book.setPrice(BigDecimal.TEN);
        book.setTitle("Title" + number);
        book.setEnabled(((number % 2)==0) ? true : false);
        return book;
    }

    public BookVO mockVO(Integer number) {
        BookVO book = new BookVO();
        book.setKey(number.longValue());
        book.setAuthor("Author Test" + number);
        GregorianCalendar gc = new GregorianCalendar(2023, 01, 05);
        book.setLaunchDate(gc.getTime());
        book.setPrice(BigDecimal.TEN);
        book.setTitle("Title" + number);
        book.setEnabled(((number % 2)==0) ? true : false);
        return book;
    }


}
