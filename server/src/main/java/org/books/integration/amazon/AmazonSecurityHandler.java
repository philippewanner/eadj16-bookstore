/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.integration.amazon;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import static javax.xml.ws.handler.MessageContext.MESSAGE_OUTBOUND_PROPERTY;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author jl
 */
public class AmazonSecurityHandler implements SOAPHandler<SOAPMessageContext> {

    private Logger logger = Logger.getLogger(getClass().getName());

    private SignatureProvider signatureProvider;

    public AmazonSecurityHandler() {
        try {
            signatureProvider = new SignatureProvider();
            signatureProvider.Update();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AmazonSecurityHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(AmazonSecurityHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        SOAPMessage message = context.getMessage();

        logger.log(Level.INFO, "handleMessage: " + message);

        if ((Boolean) context.get(MESSAGE_OUTBOUND_PROPERTY)) {
            try {                
                String bodyText = context.getMessage().getSOAPBody().getFirstChild().getNodeName();

                if ("ItemLookup".equals(bodyText)) {
                    AddItemLookupSecurityHeader(context);
                } else if ("ItemSearch".equals(bodyText)) {
                    AddItemSearchSecurityHeader(context);
                }

                System.out.println("handleMessage:");
                message.writeTo(System.out);
            } catch (SOAPException ex) {

            } catch (IOException ex) {

            }
        }
        return true;

    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {

    }

    private void AddItemLookupSecurityHeader(SOAPMessageContext context) throws SOAPException {

        SOAPElement soapheader = context.getMessage().getSOAPHeader();

        soapheader.setAttributeNS("xmlns", "aws", "http://security.amazonaws.com/doc/2007-01-01/");

        String namespace = "http://security.amazonaws.com/doc/2007-01-01/";

        SOAPElement keyId = context.getMessage().getSOAPHeader().addChildElement("AWSAccessKeyId", "aws", namespace);
        keyId.addTextNode(SignatureProvider.getACCESS_KEY());

        SOAPElement ts = context.getMessage().getSOAPHeader().addChildElement("Timestamp", "aws", namespace);
        ts.addTextNode(signatureProvider.getTimestamp());

        SOAPElement signature = context.getMessage().getSOAPHeader().addChildElement("Signature", "aws", namespace);
        signature.addTextNode(signatureProvider.getItemLookupSignature());
    }

    private void AddItemSearchSecurityHeader(SOAPMessageContext context) throws SOAPException {
        SOAPElement soapheader = context.getMessage().getSOAPHeader();

        soapheader.setAttributeNS("xmlns", "aws", "http://security.amazonaws.com/doc/2007-01-01/");

        String namespace = "http://security.amazonaws.com/doc/2007-01-01/";

        SOAPElement keyId = context.getMessage().getSOAPHeader().addChildElement("AWSAccessKeyId", "aws", namespace);
        keyId.addTextNode(SignatureProvider.getACCESS_KEY());

        SOAPElement ts = context.getMessage().getSOAPHeader().addChildElement("Timestamp", "aws", namespace);
        ts.addTextNode(signatureProvider.getTimestamp());

        SOAPElement signature = context.getMessage().getSOAPHeader().addChildElement("Signature", "aws", namespace);
        signature.addTextNode(signatureProvider.getItemSearchSignature());
    }

}
