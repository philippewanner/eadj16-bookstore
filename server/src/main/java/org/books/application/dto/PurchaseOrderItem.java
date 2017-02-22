package org.books.application.dto;

import org.books.persistence.dto.BookInfo;

import java.io.Serializable;

public class PurchaseOrderItem implements Serializable {

    private BookInfo bookInfo;
    private Integer quantity;

    public PurchaseOrderItem(){}

    public PurchaseOrderItem(BookInfo bookInfo, Integer quantity){

        this.bookInfo = bookInfo;
        this.quantity = quantity;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
