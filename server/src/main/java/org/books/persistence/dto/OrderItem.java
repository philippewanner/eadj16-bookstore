/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dto;

/**
 *
 * @author jl
 */
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OrderItem {

    private BookInfo bookInfo;
    private Integer quantity;

    public OrderItem() {
    }

    public OrderItem(BookInfo bookInfo, Integer quantity) {
        this.bookInfo = bookInfo;
        this.quantity = quantity;
    }

    /**
     * @return the bookInfo
     */
    public BookInfo getBookInfo() {
        return bookInfo;
    }

    /**
     * @param bookInfo the bookInfo to set
     */
    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
