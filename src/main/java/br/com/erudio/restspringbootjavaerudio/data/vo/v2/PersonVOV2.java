package br.com.erudio.restspringbootjavaerudio.data.vo.v2;

import br.com.erudio.restspringbootjavaerudio.data.vo.v1.PersonVOV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@JsonPropertyOrder({"id", "address", "firstName", "lastName", "gender"})
public class PersonVOV2 extends RepresentationModel<PersonVOV2> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Mapping("id")
    @JsonProperty("id")
    private Long key;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private Date birthday;

    public PersonVOV2() {
    }

    public PersonVOV2(Long key, String firstName, String lastName, String address, String gender, Date birthday) {
        this.key = key;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.gender = gender;
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonVOV2 that = (PersonVOV2) o;
        return key.equals(that.key) && firstName.equals(that.firstName) && lastName.equals(that.lastName) && address.equals(that.address) && gender.equals(that.gender) && birthday.equals(that.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, firstName, lastName, address, gender, birthday);
    }
}
