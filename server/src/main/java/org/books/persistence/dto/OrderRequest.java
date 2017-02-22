/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jl
 */
@XmlRootElement(name = "orderRequest")
public class OrderRequest {

    private String customerNr;
    private List<OrderItem> items;

    public OrderRequest() {
    }

    /**
     * @return the customerNr
     */
    public String getCustomerNr() {
        return customerNr;
    }

    /**
     * @param customerNr the customerNr to set
     */
    public void setCustomerNr(String customerNr) {
        this.customerNr = customerNr;
    }

    /**
     * @return the items
     */
    public List<OrderItem> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
