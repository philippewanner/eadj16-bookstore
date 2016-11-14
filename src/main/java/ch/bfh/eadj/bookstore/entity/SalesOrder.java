package ch.bfh.eadj.bookstore.entity;

import javax.persistence.Entity;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.Set;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import ch.bfh.eadj.bookstore.common.definition.Definitions.OrderStatus;
import javax.persistence.Embedded;
import javax.persistence.OneToOne;

@Entity
public class SalesOrder extends BaseEntity {

    /**
     * Type Set because unique required but not ordered
     */
    @OneToMany
    @JoinColumn(name = "salesOrder_id")
    private Set<SalesOrderItem> salesOrderItems;

    @Embedded
    private Address address;
    
    @Embedded
    private CreditCard creditCard;
    
    @OneToOne(optional=false)
    private Customer customer;

    private LocalDate date;

    private BigDecimal amount;

    private OrderStatus status;

}
