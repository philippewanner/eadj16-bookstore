package org.books.application.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;

@RequestScoped
@Path("orders")
public class OrderResource {


}

/*


Bookstore Project: Orders Resource
Place Order
Request
HTTP Method 	POST
Request Path 	/bookstore/rest/orders
Headers 	Content-Type: {mime-type}
Content-Length: {number-of-bytes}
Body 	order request data (see XML schema)
Response
Status Codes 	201 Created
400 Bad Request (incomplete order data)
404 Not Found (customer or book not found)
402 Payment Required (payment error)
Headers 	Content-Type: {mime-type}
Content-Length: {number-of-bytes}
Body 	order data (see XML schema)
Find Order by Number
Request
HTTP Method 	GET
Request Path 	/bookstore/rest/orders/{number}
Headers 	Accept: {mime-type}
Body 	empty
Response
Status Codes 	200 OK
404 Not Found (order not found)
Headers 	Content-Type: {mime-type}
Content-Length: {number-of-bytes}
Body 	order data (see XML schema)
Search Orders of Customer
Request
HTTP Method 	GET
Request Path 	/bookstore/rest/orders?customerNr={number}&year={year}
Headers 	Accept: {mime-type}
Body 	empty
Response
Status Codes 	200 OK
400 Bad Request (customer number or year missing)
404 Not Found (customer not found)
Headers 	Content-Type: {mime-type}
Content-Length: {number-of-bytes}
Body 	list of order infos (see XML schema)
Cancel Order
Request
HTTP Method 	DELETE
Request Path 	/bookstore/rest/orders/{number}
Headers 	none
Body 	empty
Remark 	The cancellation of an order causes a status change
Response
Status Codes 	204 No Content
403 Forbidden (order not cancelable)
404 Not Found (order not found)
Headers 	none
Body 	empty
 */