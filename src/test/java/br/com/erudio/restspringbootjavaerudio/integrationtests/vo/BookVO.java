package br.com.erudio.restspringbootjavaerudio.integrationtests.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
public class BookVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String MY_TIME_ZONE="America/Sao_Paulo";

    private Long id;
    private String author;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = MY_TIME_ZONE)
    private Date launchDate;
    private BigDecimal price;
    private String title;

    public BookVO() {
    }

    public BookVO(Long id, String author, Date launchDate, BigDecimal price, String priceFormatted, String title) {
        this.id = id;
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
        BookVO bookVOV1 = (BookVO) o;
        return id.equals(bookVOV1.id) && author.equals(bookVOV1.author) && launchDate.equals(bookVOV1.launchDate) && price.equals(bookVOV1.price) && title.equals(bookVOV1.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, author, launchDate, price, title);
    }
}
