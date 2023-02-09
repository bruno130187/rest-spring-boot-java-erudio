package br.com.erudio.restspringbootjavaerudio.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Data
@JsonPropertyOrder({"id", "author", "launch_date", "price", "priceFormatted", "title"})
@XmlRootElement
public class BookVO extends RepresentationModel<BookVO> implements Serializable {

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

    private Boolean enabled;

    public BookVO() {
    }

    public BookVO(Long key, String author, Date launchDate, BigDecimal price, String priceFormatted, String title, Boolean enabled) {
        this.key = key;
        this.author = author;
        this.launchDate = launchDate;
        this.price = price.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.title = title;
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookVO bookVO = (BookVO) o;
        return key.equals(bookVO.key) && author.equals(bookVO.author) && launchDate.equals(bookVO.launchDate) && price.equals(bookVO.price) && title.equals(bookVO.title) && enabled.equals(bookVO.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, author, launchDate, price, title, enabled);
    }
}
