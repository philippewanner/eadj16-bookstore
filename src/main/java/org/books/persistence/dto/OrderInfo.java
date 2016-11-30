/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dto;

import org.books.persistence.enumeration.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jlue4
 */
public class OrderInfo implements Serializable {

	private Long id;
	private Long number;
	private Date date;
	private BigDecimal amount;
	private OrderStatus status;

	/*
	* Constructor for Fischlin Unit Tests
	 */
	public OrderInfo() {
	}

	/*
	* Constructor for Fischlin Unit Tests
	 */
	public OrderInfo(Long number, Date date, BigDecimal amount, OrderStatus orderStatus) {
		this(null, number, date, amount, orderStatus);
	}

	public OrderInfo(Long id, Long number, Date date, BigDecimal amount, OrderStatus orderStatus) {
		this.id = id;
		this.number = number;
		this.date = date;
		this.amount = amount;
		this.status = orderStatus;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
