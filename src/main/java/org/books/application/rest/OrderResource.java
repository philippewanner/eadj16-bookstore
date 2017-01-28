package org.books.application.rest;

import org.books.application.dto.PurchaseOrder;
import org.books.application.dto.PurchaseOrderItem;
import org.books.application.exception.*;
import org.books.application.service.OrderService;
import org.books.persistence.dto.BookInfo;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static org.books.application.rest.CatalogResource.logger;
import org.books.persistence.entity.SalesOrder;
import org.books.persistence.entity.SalesOrderItem;

/**
 * REST web service endpoint for orders resource
 */

@Path("orders")
public class OrderResource {

    protected static Logger logger;
    
    @EJB
    private OrderService orderService;

    public OrderResource(){
        logger = Logger.getLogger(getClass().getName());
    }

    /** Place Order **
     Request
        HTTP Method 	POST
        Request Path 	/bookstore/rest/orders
        Headers 	    Content-Type: {mime-type},
                        Content-Length: {number-of-bytes}
        Body 	        order request data (see XML schema)
     Response
        Status Codes 	201 Created
                        400 Bad Request (incomplete order data)
                        404 Not Found (customer or book not found)
                        402 Payment Required (payment error)
        Headers 	    Content-Type: {mime-type},
                        Content-Length: {number-of-bytes}
        Body 	        order data (see XML schema)
     */
    @POST
    @Consumes({APPLICATION_XML})
    @Produces({APPLICATION_XML})
    public Response placeOrder(PurchaseOrder purchaseOrder){

        try {
            //todo 400 Bad Request (incomplete order data)
            return Response.status(Response.Status.CREATED).entity(orderService.placeOrder(purchaseOrder)).build();

        } catch (CustomerNotFoundException e) {
            throw new WebApplicationException("Customer not found", Response.Status.NOT_FOUND);
        } catch (BookNotFoundException e) {
            throw new WebApplicationException("Book not found", Response.Status.NOT_FOUND);
        } catch (PaymentFailedException e) {
            throw new WebApplicationException("Payment error", Response.Status.PAYMENT_REQUIRED);
        }
    }

    
    
    

    /** Find Order by Number **
     Request
        HTTP Method 	GET
        Request Path 	/bookstore/rest/orders/{number}
        Headers 	    Accept: {mime-type}
        Body 	        empty
     Response
        Status Codes 	200 OK
                        404 Not Found (order not found)
        Headers 	    Content-Type: {mime-type}
        Content-Length: {number-of-bytes}
        Body 	        order data (see XML schema)
     */
    @GET
    @Path("{number}")
    @Produces({APPLICATION_XML})
    public SalesOrder findOrderByNumber(@PathParam("number") Long number){

        logger.log(Level.INFO, "findOrderByNumber " + number);
        
        try {
            SalesOrder so = orderService.findOrder(number);
                                    
            for(SalesOrderItem soi: so.getSalesOrderItems()){
                soi.getBook();
                soi.getPrice();
                soi.getQuantity();
            }
                        
            so.getCustomer();
            so.getAddress();
            so.getCreditCard();
            so.getCustomer();
            so.getNumber();
            so.getDate();
            so.getAmount();
            so.getStatus();
                
            
            
            return so;
            
            ////return Response.status(Response.Status.OK).entity(orderService.findOrder(number)).build();

        } catch (OrderNotFoundException e) {
            logger.log(Level.WARNING, "Order not found " + number);
            throw new WebApplicationException("Order not found", Response.Status.NOT_FOUND);
        }
    }


    /** Search Orders of Customer **
     Request
        HTTP Method 	GET
        Request Path 	/bookstore/rest/orders?customerNr={number}&year={year}
        Headers 	    Accept: {mime-type}
        Body 	        empty
     Response
        Status Codes 	200 OK
                        400 Bad Request (customer number or year missing)
                        404 Not Found (customer not found)
        Headers 	    Content-Type: {mime-type}
                        Content-Length: {number-of-bytes}
        Body 	        list of order infos (see XML schema)
     */
    @GET
    @Produces({APPLICATION_XML})
    public Response searchOrdersOfCustomer(@QueryParam("number") Long customerNr, @QueryParam("year") Integer year){

        if(customerNr == null || year == null){
            throw new WebApplicationException("Customer number or year missing", Response.Status.BAD_REQUEST);
        }

        try {
            return Response.status(Response.Status.OK).entity(orderService.searchOrders(customerNr, year)).build();

        } catch (CustomerNotFoundException e) {
            throw new WebApplicationException("Customer not found", Response.Status.NOT_FOUND);
        }
    }


    /** Cancel Order **
     Request
        HTTP Method 	DELETE
        Request Path 	/bookstore/rest/orders/{number}
        Headers 	    none
        Body 	        empty
        Remark 	        The cancellation of an order causes a status change
     Response
        Status Codes 	204 No Content
                        403 Forbidden (order not cancelable)
                        404 Not Found (order not found)
        Headers 	    none
        Body 	        empty
     */
    @DELETE
    @Path("{number}")
    public Response cancelOrder(@PathParam("number") Long number){

        try {
            orderService.cancelOrder(number);
            return Response.status(Response.Status.NO_CONTENT).build();

        } catch (OrderNotFoundException e) {
            throw new WebApplicationException("Order not found", Response.Status.NOT_FOUND);
        } catch (OrderAlreadyShippedException e) {
            throw new WebApplicationException("Order not cancelable", Response.Status.FORBIDDEN);
        }
    }

}

