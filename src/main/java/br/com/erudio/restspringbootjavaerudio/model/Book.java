package br.com.erudio.restspringbootjavaerudio.model;

import jakarta.persistence.*;
import lombok.Data;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "books")
public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "author", nullable = false, length = 255)
    private String author;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "launch_date", nullable = false)
    private Date launchDate;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    public Book() {
    }

    public Book(Long id, String author, Date launchDate, BigDecimal price, String title) {
        this.id = id;
        this.author = author;
        this.launchDate = launchDate;
        this.price = price;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id.equals(book.id) && author.equals(book.author) && launchDate.equals(book.launchDate) && price.equals(book.price) && title.equals(book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, launchDate, price, title);
    }

}
