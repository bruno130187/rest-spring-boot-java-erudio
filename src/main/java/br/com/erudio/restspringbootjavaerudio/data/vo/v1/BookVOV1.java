package br.com.erudio.restspringbootjavaerudio.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@JsonPropertyOrder({"id", "author", "launch_date", "price", "priceFormatted", "title"})
public class BookVOV1 extends RepresentationModel<BookVOV1> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String MY_TIME_ZONE="America/Sao_Paulo";

    @Mapping("id")
    @JsonProperty("id")
    private Long key;
    private String author;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = MY_TIME_ZONE)
    private Date launchDate;

    private BigDecimal price;
    private String title;

    public BookVOV1() {
    }

    public BookVOV1(Long key, String author, Date launchDate, BigDecimal price, String priceFormatted, String title) {
        this.key = key;
        this.author = author;
        this.launchDate = launchDate;
        this.price = price.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookVOV1 bookVOV1 = (BookVOV1) o;
        return key.equals(bookVOV1.key) && author.equals(bookVOV1.author) && launchDate.equals(bookVOV1.launchDate) && price.equals(bookVOV1.price) && title.equals(bookVOV1.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, author, launchDate, price, title);
    }
}
