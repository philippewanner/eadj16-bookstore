/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.service;

import org.books.application.exception.BookNotFoundException;
import org.books.integration.amazon.*;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Book;
import org.books.persistence.enumeration.BookBinding;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jl
 */
@Stateless
@LocalBean
public class AmazonCatalogBean extends AbstractService {

    private AWSECommerceServicePortType awsecommerceServicePorttype;

    public AmazonCatalogBean() {
        AWSECommerceService s = new AWSECommerceService();
        awsecommerceServicePorttype = s.getAWSECommerceServicePort();
    }

    public List<BookInfo> searchBooks(String keywords) {
        logInfo("searchBooks");

        List<BookInfo> books = new ArrayList<>();

        BigInteger itemPage = BigInteger.valueOf(1);
        BigInteger totalPages = BigInteger.valueOf(10);

        Integer retries = 0;
        Integer maxretries = 10;

        while (itemPage.intValue() <= totalPages.intValue() && (retries < maxretries)) {

            ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
            itemSearchRequest.setKeywords(keywords);
            itemSearchRequest.setSearchIndex("Books");
            itemSearchRequest.setItemPage(itemPage);
            itemSearchRequest.getResponseGroup().add("ItemAttributes");

            ItemSearch itemSearch = new ItemSearch();
            itemSearch.setAssociateTag(SignatureProvider.getASSOCIATE_TAG());

            itemSearch.getRequest().add(itemSearchRequest);

            ItemSearchResponse response = null;
            try {
                response = awsecommerceServicePorttype.itemSearch(itemSearch);
            } catch (Exception ex) {
                response = null;
                retries++;
                logWarn("Search error / retries: " + retries + " / " + ex);
            }

            if (response != null) {
                itemPage = itemPage.add(BigInteger.ONE);

                totalPages = addBooks(response, books);

                // according to AmazonSpec only 10 pages allowed
                if (totalPages.intValue() > 10) {
                    totalPages = BigInteger.valueOf(10);
                }

                /*try {
                    // throtteling requests
                    ////Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    logWarn("Sleep error/" + ex);
                }*/
            } else {
                try {
                    Thread.sleep(retries * 1000 + 1000);
                } catch (InterruptedException ex) {
                    logWarn("Sleep error/" + ex);
                }
            }
        }

        return books;
    }

    public Book findBook(String isbn) throws BookNotFoundException {
        logger.log(Level.INFO, "SearchByIsbn");

        if (isbn == null) {
            throw new BookNotFoundException();
        }

        isbn = isbn.replace("-", "");

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

        ItemLookupResponse response = null;

        Integer retries = 0;
        while (response == null && retries < 3) {
            try {
                response = awsecommerceServicePorttype.itemLookup(itemLookup);

                logger.log(Level.INFO, "" + response);

                return readBook(response);

            } catch (Exception ex) {
                logger.log(Level.SEVERE, "SearchByIsbn error: " + ex);
                response = null;
            }

            if (response == null) {
                retries++;
                try {
                    Thread.sleep(retries * 2000);
                } catch (InterruptedException ex) {
                    logger.log(Level.WARNING, null, ex);
                }
            }
        }

        throw new BookNotFoundException();
    }

    private Book readBook(ItemLookupResponse response) throws BookNotFoundException {
        if (response.getItems().size() > 0) {

            Items items = response.getItems().get(0);
            if ("true".equals(items.getRequest().getIsValid().toLowerCase())) {

                for (Item item : items.getItem()) {
                    Book b = convertToBook(item.getItemAttributes());

                    if (b != null) {
                        return b;
                    }
                }
            }
        } else {
            logger.log(Level.WARNING, "no items found");
        }

        throw new BookNotFoundException();
    }

    private Book convertToBook(ItemAttributes itemAttributes) {
        Book b = null;

        try {
            if ("Book".equalsIgnoreCase(itemAttributes.getProductGroup())) {

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

                String price = itemAttributes.getListPrice().getFormattedPrice().substring(1, itemAttributes.getListPrice().getFormattedPrice().length());

                Double dprice = Double.valueOf(price);

                if (isbn.length() > 0
                        && authors.length() > 0
                        && bb != BookBinding.UNKNOWN
                        && numberOfPages != null
                        && year.length() > 0
                        && publisher.length() > 0
                        && title.length() > 0
                        && price != null) {
                    b = new Book();
                    b.setIsbn(isbn);
                    b.setAuthors(authors);
                    b.setPrice(BigDecimal.valueOf(dprice));
                    b.setBinding(bb);
                    b.setNumberOfPages(numberOfPages.intValue());
                    b.setPublicationYear(Integer.parseInt(year));
                    b.setPublisher(publisher);
                    b.setTitle(title);
                }
            }
        } catch (Exception ex) {
            logger.log(Level.WARNING, "incomplete book");
        }

        return b;
    }

    private BigInteger addBooks(ItemSearchResponse response, List<BookInfo> books) {
        BigInteger totalPages = BigInteger.ZERO;

        if (response.getItems().size() > 0) {
            Items items = response.getItems().get(0);

            if ("true".equalsIgnoreCase(items.getRequest().getIsValid())) {
                totalPages = items.getTotalPages();

                List<Item> itemitems = items.getItem();

                for (Item itemitem : itemitems) {
                    BookInfo bookInfo = getBookInfo(itemitem.getItemAttributes());

                    if (bookInfo != null) {
                        books.add(bookInfo);
                    }
                }
            }
        }

        return totalPages;
    }

    private BookInfo getBookInfo(ItemAttributes itemAttributes) {
        Book b = convertToBook(itemAttributes);
        if (b != null) {
            BookInfo bi = new BookInfo();
            bi.setIsbn(b.getIsbn());
            bi.setPrice(b.getPrice());
            bi.setTitle(b.getTitle());

            return bi;
        }
        return null;
    }
}
