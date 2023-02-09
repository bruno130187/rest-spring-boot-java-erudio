package br.com.erudio.restspringbootjavaerudio.integrationtests.vo;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@XmlRootElement
public class PersonVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String firstName;
	private String lastName;
	private String address;
	private String gender;
	private Boolean enabled;
	
	public PersonVO() {}

	public PersonVO(Long id, String firstName, String lastName, String address, String gender, Boolean enabled) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.gender = gender;
		this.enabled = enabled;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PersonVO personVO = (PersonVO) o;
		return id.equals(personVO.id) && firstName.equals(personVO.firstName) && lastName.equals(personVO.lastName) && address.equals(personVO.address) && gender.equals(personVO.gender) && enabled.equals(personVO.enabled);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, address, gender, enabled);
	}
}