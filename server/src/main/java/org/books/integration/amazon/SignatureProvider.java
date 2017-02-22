/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.integration.amazon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author jl
 */
public class SignatureProvider {

    private static final String ASSOCIATE_TAG = "test0e5d-20";
    private static final String ACCESS_KEY = "AKIAIYFLREOYORYNAQTQ";
    private static final String SECRET_KEY = "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";
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String MAC_ALGORITHM = "HmacSHA256";

    private String itemLookupSignature;
    private String itemSearchSignature;
    private String timestamp;

    public void Update() throws NoSuchAlgorithmException, InvalidKeyException {
        DateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
        timestamp = dateFormat.format(Calendar.getInstance().getTime());

        itemLookupSignature = getSignature("ItemLookup", timestamp);
        itemSearchSignature = getSignature("ItemSearch", timestamp);
    }

    private String getSignature(String method, String timestamp) throws NoSuchAlgorithmException, InvalidKeyException {

        Mac mac = Mac.getInstance(MAC_ALGORITHM);
        SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), MAC_ALGORITHM);
        mac.init(key);
        byte[] data = mac.doFinal((method + timestamp).getBytes());
        String signature = DatatypeConverter.printBase64Binary(data);
        
        System.out.println(method + "signature: timestamp " + timestamp + ", signature " + signature);
        
        return signature;
    }

    /**
     * @return the ASSOCIATE_TAG
     */
    public static String getASSOCIATE_TAG() {
        return ASSOCIATE_TAG;
    }

    /**
     * @return the ACCESS_KEY
     */
    public static String getACCESS_KEY() {
        return ACCESS_KEY;
    }

    /**
     * @return the itemLookupSignature
     */
    public String getItemLookupSignature() {
        return itemLookupSignature;
    }

    /**
     * @return the itemSearchSignature
     */
    public String getItemSearchSignature() {
        return itemSearchSignature;
    }

    /**
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }
}
