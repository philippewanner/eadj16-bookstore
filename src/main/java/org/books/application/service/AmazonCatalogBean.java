/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.service;

import java.util.logging.Level;
import javax.ejb.Stateless;
import org.books.integration.amazon.AWSECommerceService;
import org.books.integration.amazon.AWSECommerceServicePortType;
import org.books.integration.amazon.ItemLookup;
import org.books.integration.amazon.ItemLookupRequest;
import org.books.integration.amazon.ItemLookupResponse;
import org.books.integration.amazon.SignatureProvider;
import org.books.persistence.entity.Book;

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

    public Book findBook(String isbn) {
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
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "SearchByIsbn error: " + ex);
        }

        return null;
    }

}
