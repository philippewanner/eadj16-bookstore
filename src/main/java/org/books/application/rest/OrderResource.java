package org.books.application.rest;

import org.books.application.service.CustomerServiceBean;
import org.books.application.service.OrderService;

import javax.ejb.EJB;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * REST web service for orders resource
 */

@Path("orders")
public class OrderResource {

    @EJB
    private OrderService service;


    /** Place Order **
     Request
        HTTP Method 	POST
        Request Path 	/bookstore/rest/orders
        Headers 	    Content-Type: {mime-type},
                        Content-Length: {number-of-bytes}
        Body 	        order request data (see XML schema)
     Response
        Status Codes 	201 Created
        400             Bad Request (incomplete order data)
        404             Not Found (customer or book not found)
        402             Payment Required (payment error)
        Headers 	    Content-Type: {mime-type},
                        Content-Length: {number-of-bytes}
        Body 	        order data (see XML schema)
     */
    @POST
    public Response placeOrder(){
        return null;
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
    public Response findOrderByNummer(){
        return null;
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
    public Response searchOrdersOfCustomer(){
        return null;
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
    public Response cancelOrder(){
        return null;
    }

}

