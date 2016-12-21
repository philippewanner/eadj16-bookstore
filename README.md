# eadj16-bookstore [![Build Status](https://travis-ci.org/philippewanner/eadj16-bookstore.svg?branch=master)](https://travis-ci.org/philippewanner/eadj16-bookstore)
Projekt Bookstore - CAS Enterprie Application Development Java

#Project description

##Goal
The goal of this project is to develop an online bookstore. The application has a layered architecture:
- the presentation layer which contains the RESTful web services
- the service layer which contains the Enterprise JavaBeans
- the persistence layer which contains the repositories

In order to keep the application simple, the following design decisions have been made:
- the services are stateless and have remote interfaces
- the entities are used as transfer objects between the layers
- the presentation layer is responsible for the input validation

###Catalog Service
The objective of this task is to implement the catalog service of the bookstore application.

1. Implement a stateless session bean specified by the remote interface CatalogService.

    - The service allows to
    - add a book to the catalog
    - find a book by ISBN number
    - search for books by keywords
    - update the data of a book
    
2. Implement integration tests to verify the functionality of the service.

###Customer Service
The objective of this task is to implement the customer service of the bookstore application.

1. Implement a stateless session bean specified by the remote interface CustomerService.

    The service allows to
    
    - register a customer
    - authenticate a customer
    - change the password of a customer
    - find a customer by number or email address
    - update the data of a customer
    
2. Implement integration tests to verify the functionality of the service.

###Persistence
The objective of this task is to implement the persistence of books, customers and orders.

1. Implement the Java entities Book, Customer and SalesOrder and their relationships.
2. Implement the repositories BookRepository, CustomerRepository and OrderRepository which store and retrieve the entities.
3. Define the persistence unit bookstore which maps the entities to the data source jdbc/bookstore.

###Order Service
The objective of this task is to implement the order service of the bookstore application.

1. Implement a stateless session bean specified by the remote interface OrderService.
    
    The service allows to
    
    - place an order
    - find an order by number
    - search for orders by customer and year
    - cancel an order
2. Implement integration tests to verify the functionality of the service.

###Order Processor
The objective of this task is to simulate the order processing in the bookstore application.

1. Configure the message queue jms/orderQueue in the GlassFish application server.
2. In the session bean OrderService send a message to the order queue after an order has been accepted.
3. Implement the message-driven bean OrderProcessor which receives messages from the order queue and sets the status of
 the corresponding orders to processing.
4. Use the timer service to set the status of the orders to shipped after a specific time interval has elapsed (if they
 have not been canceled yet).
5. Configure the mail session mail/bookstore in the GlassFish application server.
6. Implement the session bean MailService which allows to asynchronously send email messages.
7. Use the session bean to send email confirmations to the customer after an order has started to be processed and after it has been shipped.
