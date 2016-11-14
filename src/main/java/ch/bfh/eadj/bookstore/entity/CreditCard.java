package ch.bfh.eadj.bookstore.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CreditCard {

	public enum CreditCardType {
		AMERICAN_EXPRESS, MASTER_CARD, VISA
	}

	private CreditCardType type;
	@Column(name = "creditCardNumber")
	private String number;
	private Integer expirationMonth;
	private Integer expirationYear;

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