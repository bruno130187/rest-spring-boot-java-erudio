package br.com.erudio.restspringbootjavaerudio.unittests.mockito.services;

import br.com.erudio.restspringbootjavaerudio.data.vo.v1.BookVO;
import br.com.erudio.restspringbootjavaerudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.restspringbootjavaerudio.model.Book;
import br.com.erudio.restspringbootjavaerudio.repositories.BookRepository;
import br.com.erudio.restspringbootjavaerudio.services.BookService;
import br.com.erudio.restspringbootjavaerudio.unittests.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.IanaLinkRelations;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    MockBook mockBook;
    @InjectMocks
    private BookService bookService;
    @Mock
    BookRepository bookRepository;
    @BeforeEach
    void setUpMocks() {

        mockBook = new MockBook();
        MockitoAnnotations.openMocks(this);

    }
    @Test
    void create() {
        Book book = mockBook.mockEntity(1);
        Book persisted = book;
        persisted.setId(1L);

        BookVO bookVO = mockBook.mockVO(1);
        bookVO.setKey(1L);
        when(bookRepository.save(book)).thenReturn(persisted);

        var result = bookService.create(bookVO);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().toString().contains("</api/book/v1/1>;rel=\"self\""));
        assertEquals("Author Test1", result.getAuthor());
        GregorianCalendar gc = new GregorianCalendar(2023, 01, 05);
        assertEquals(gc.getTime(), result.getLaunchDate());
        assertEquals(BigDecimal.TEN, result.getPrice());
        assertEquals("Title1", result.getTitle());

    }
    @Test
    void testCreateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            bookService.create(null);
        });
        String expectedMessage = "It is not allowed to persist an null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Book book = mockBook.mockEntity(1);
        book.setId(1L);

        Book persisted = book;
        persisted.setId(1L);

        BookVO bookVO = mockBook.mockVO(1);
        bookVO.setKey(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(persisted);

        var result = bookService.update(bookVO);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(String.valueOf(result.getRequiredLink(IanaLinkRelations.SELF.value())).contains("</api/book/v1/1>;rel=\"self\""));
        assertEquals("Author Test1", result.getAuthor());
        GregorianCalendar gc = new GregorianCalendar(2023, 01, 05);
        assertEquals(gc.getTime(), result.getLaunchDate());
        assertEquals(BigDecimal.TEN, result.getPrice());
        assertEquals("Title1", result.getTitle());
    }
    @Test
    void testUpdateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            bookService.update(null);
        });
        String expectedMessage = "It is not allowed to persist an null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void delete() {
        Book book = mockBook.mockEntity(1);
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        bookService.delete(1L);
    }

    @Test
    void findAllSimple() {
        List<Book> bookList = mockBook.mockEntityList();

        GregorianCalendar gc = new GregorianCalendar(2023, 01, 05);

        when(bookRepository.findAllSimple()).thenReturn(bookList);
        var books = bookService.findAllSimple();
        assertNotNull(books);
        assertEquals(14, books.size());

        var bookVOV1one = books.get(1);
        assertNotNull(bookVOV1one);
        assertNotNull(bookVOV1one.getKey());
        assertNotNull(bookVOV1one.getLinks());

        assertTrue(bookVOV1one.getLinks().toString().contains("</api/book/v1/1>;rel=\"self\""));
        assertEquals("Author Test1", bookVOV1one.getAuthor());
        assertEquals(gc.getTime(), bookVOV1one.getLaunchDate());
        assertEquals(BigDecimal.TEN, bookVOV1one.getPrice());
        assertEquals("Title1", bookVOV1one.getTitle());

        var bookVOV1four = books.get(4);
        assertNotNull(bookVOV1four);
        assertNotNull(bookVOV1four.getKey());
        assertNotNull(bookVOV1four.getLinks());

        assertTrue(bookVOV1four.getLinks().toString().contains("</api/book/v1/4>;rel=\"self\""));
        assertEquals("Author Test4", bookVOV1four.getAuthor());
        assertEquals(gc.getTime(), bookVOV1four.getLaunchDate());
        assertEquals(BigDecimal.TEN, bookVOV1four.getPrice());
        assertEquals("Title4", bookVOV1four.getTitle());

        var bookVOV1seven = books.get(7);
        assertNotNull(bookVOV1seven);
        assertNotNull(bookVOV1seven.getKey());
        assertNotNull(bookVOV1seven.getLinks());

        assertTrue(bookVOV1seven.getLinks().toString().contains("</api/book/v1/7>;rel=\"self\""));
        assertEquals("Author Test7", bookVOV1seven.getAuthor());
        assertEquals(gc.getTime(), bookVOV1seven.getLaunchDate());
        assertEquals(BigDecimal.TEN, bookVOV1seven.getPrice());
        assertEquals("Title7", bookVOV1seven.getTitle());

    }

    @Test
    void findById() {
        Book book = mockBook.mockEntity(1);
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        var result = bookService.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertEquals("Author Test1", result.getAuthor());
        GregorianCalendar gc = new GregorianCalendar(2023, 01, 05);
        assertEquals(gc.getTime(), result.getLaunchDate());
        assertEquals(BigDecimal.TEN, result.getPrice());
        assertEquals("Title1", result.getTitle());
    }
}