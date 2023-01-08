package br.com.erudio.restspringbootjavaerudio.unittests.mocks;

import br.com.erudio.restspringbootjavaerudio.data.vo.v1.BookVOV1;
import br.com.erudio.restspringbootjavaerudio.model.Book;

import java.math.BigDecimal;
import java.util.*;

public class MockBook {

    public Book mockEntity() {
        return mockEntity(0);
    }

    public BookVOV1 mockVO() {
        return mockVO(0);
    }

    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVOV1> mockVOList() {
        List<BookVOV1> books = new ArrayList<>();
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
        return book;
    }

    public BookVOV1 mockVO(Integer number) {
        BookVOV1 book = new BookVOV1();
        book.setKey(number.longValue());
        book.setAuthor("Author Test" + number);
        GregorianCalendar gc = new GregorianCalendar(2023, 01, 05);
        book.setLaunchDate(gc.getTime());
        book.setPrice(BigDecimal.TEN);
        book.setTitle("Title" + number);
        return book;
    }


}
