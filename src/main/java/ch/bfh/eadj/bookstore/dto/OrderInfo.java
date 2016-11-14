/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.eadj.bookstore.dto;

import ch.bfh.eadj.bookstore.common.definition.Definitions.OrderStatus;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author jlue4
 */
public class OrderInfo {
    private Long number;
    
    private Date date;
    
    private BigDecimal amount;
    
    private OrderStatus status;
}
