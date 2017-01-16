/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Level;
import javax.ejb.Stateless;
import org.books.application.exception.BookNotFoundException;
import org.books.integration.amazon.AWSECommerceService;
import org.books.integration.amazon.AWSECommerceServicePortType;
import org.books.integration.amazon.ItemAttributes;
import org.books.integration.amazon.ItemLookup;
import org.books.integration.amazon.ItemLookupRequest;
import org.books.integration.amazon.ItemLookupResponse;
import org.books.integration.amazon.Items;
import org.books.integration.amazon.SignatureProvider;
import org.books.persistence.entity.Book;
import org.books.persistence.enumeration.BookBinding;

/**
 *
 * @author jl
 */
@Stateless
public class AmazonCatalogBean extends AbstractService {

    private AWSECommerceServicePortType awsecommerceServicePorttype;

    public AmazonCatalogBean() {
        AWSECommerceService s = new AWSECommerceService();
        awsecommerceServicePorttype = s.getAWSECommerceServicePort();
    }

    public Book findBook(String isbn) throws BookNotFoundException {
        logger.log(Level.INFO, "SearchByIsbn");

        ItemLookupRequest itemLookupRequest = new ItemLookupRequest();
        itemLookupRequest.setIdType("ISBN");
        itemLookupRequest.getItemId().add(isbn);
        itemLookupRequest.setSearchIndex("Books");
        itemLookupRequest.getResponseGroup().add("ItemAttributes");

        ItemLookup itemLookup = new ItemLookup();
        String awsAccessKeyId = SignatureProvider.getACCESS_KEY();
        String associateTag = SignatureProvider.getASSOCIATE_TAG();
        itemLookup.setAWSAccessKeyId(awsAccessKeyId);
        itemLookup.setAssociateTag(associateTag);
        itemLookup.getRequest().add(itemLookupRequest);

        try {
            ItemLookupResponse response = awsecommerceServicePorttype.itemLookup(itemLookup);

            logger.log(Level.INFO, "" + response);

            return readBook(response);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "SearchByIsbn error: " + ex);
        }

        throw new BookNotFoundException();
    }

    private Book readBook(ItemLookupResponse response) throws BookNotFoundException {
        if (response.getItems().size() > 0) {

            Items items = response.getItems().get(0);
            if ("true".equals(items.getRequest().getIsValid().toLowerCase())) {

                if (items.getItem().size() > 0) {
                    Book b = convertToBook(items.getItem().get(0).getItemAttributes());

                    if (b != null) {
                        return b;
                    }
                }
            }
        }
        else{
            logger.log(Level.WARNING, "no items found");
        }

        throw new BookNotFoundException();
    }

    private Book convertToBook(ItemAttributes itemAttributes) {
        Book b = null;

        try {
            String isbn = itemAttributes.getISBN();

            String authors = "";
            for (String author : itemAttributes.getAuthor()) {
                authors = authors + ", " + author;
            }
            authors = authors.substring(2, authors.length());

            BookBinding bb = BookBinding.UNKNOWN;
            switch (itemAttributes.getBinding().toLowerCase()) {
                case "hardcover":
                    bb = BookBinding.HARDCOVER;
                    break;
                case "paperback":
                    bb = BookBinding.PAPERBACK;
                    break;
                case "ebook":
                    bb = BookBinding.EBOOK;
                    break;
            }

            BigInteger numberOfPages = itemAttributes.getNumberOfPages();

            String year = itemAttributes.getPublicationDate().substring(0, 4);

            String publisher = itemAttributes.getPublisher();

            String title = itemAttributes.getTitle();

            if (isbn.length() > 0
                    && authors.length() > 0
                    && bb != BookBinding.UNKNOWN
                    && numberOfPages != null
                    && year.length() > 0
                    && publisher.length() > 0
                    && title.length() > 0) {
                b = new Book();
                b.setIsbn(isbn);
                b.setAuthors(authors);
                b.setPrice(BigDecimal.valueOf(0)); // TODO: price
                b.setBinding(bb);
                b.setNumberOfPages(numberOfPages.intValue());
                b.setPublicationYear(Integer.parseInt(year));
                b.setPublisher(publisher);
                b.setTitle(title);
            }
        } catch (Exception ex) {
            logger.log(Level.WARNING, "incomplete book");
        }

        return b;
    }

}
