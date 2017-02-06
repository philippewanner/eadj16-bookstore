package org.books.persistence.entity;

import org.books.persistence.enumeration.CreditCardType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@Embeddable
@XmlRootElement
public class CreditCard implements Serializable {

	private CreditCardType type;
	@Column(name = "creditCardNumber")
	private String number;
	private Integer expirationMonth;
	private Integer expirationYear;

	public CreditCard() {
	}

	public CreditCard(CreditCardType type, String number, Integer expirationMonth, Integer expirationYear) {
		this.type = type;
		this.number = number;
		this.expirationMonth = expirationMonth;
		this.expirationYear = expirationYear;
	}

	public CreditCardType getType() {
		return type;
	}

	public void setType(CreditCardType type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(Integer expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public Integer getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(Integer expirationYear) {
		this.expirationYear = expirationYear;
	}
}