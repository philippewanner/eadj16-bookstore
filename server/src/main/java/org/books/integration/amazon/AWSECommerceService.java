
package org.books.integration.amazon;

import java.net.MalformedURLException;
import java.net.URL;
import javax.jws.HandlerChain;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "AWSECommerceService", targetNamespace = "http://webservices.amazon.com/AWSECommerceService/2011-08-01", wsdlLocation = "http://webservices.amazon.com/AWSECommerceService/AWSECommerceService.wsdl")
@HandlerChain(file = "AWSECommerceService_handler.xml")
public class AWSECommerceService
    extends Service
{

    private final static URL AWSECOMMERCESERVICE_WSDL_LOCATION;
    private final static WebServiceException AWSECOMMERCESERVICE_EXCEPTION;
    private final static QName AWSECOMMERCESERVICE_QNAME = new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://webservices.amazon.com/AWSECommerceService/AWSECommerceService.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        AWSECOMMERCESERVICE_WSDL_LOCATION = url;
        AWSECOMMERCESERVICE_EXCEPTION = e;
    }

    public AWSECommerceService() {
        super(__getWsdlLocation(), AWSECOMMERCESERVICE_QNAME);
    }

    public AWSECommerceService(WebServiceFeature... features) {
        super(__getWsdlLocation(), AWSECOMMERCESERVICE_QNAME, features);
    }

    public AWSECommerceService(URL wsdlLocation) {
        super(wsdlLocation, AWSECOMMERCESERVICE_QNAME);
    }

    public AWSECommerceService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, AWSECOMMERCESERVICE_QNAME, features);
    }

    public AWSECommerceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public AWSECommerceService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePort")
    public AWSECommerceServicePortType getAWSECommerceServicePort() {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePort"), AWSECommerceServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePort")
    public AWSECommerceServicePortType getAWSECommerceServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePort"), AWSECommerceServicePortType.class, features);
    }

    /**
     * 
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortCA")
    public AWSECommerceServicePortType getAWSECommerceServicePortCA() {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortCA"), AWSECommerceServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortCA")
    public AWSECommerceServicePortType getAWSECommerceServicePortCA(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortCA"), AWSECommerceServicePortType.class, features);
    }

    /**
     * 
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortCN")
    public AWSECommerceServicePortType getAWSECommerceServicePortCN() {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortCN"), AWSECommerceServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortCN")
    public AWSECommerceServicePortType getAWSECommerceServicePortCN(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortCN"), AWSECommerceServicePortType.class, features);
    }

    /**
     * 
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortDE")
    public AWSECommerceServicePortType getAWSECommerceServicePortDE() {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortDE"), AWSECommerceServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortDE")
    public AWSECommerceServicePortType getAWSECommerceServicePortDE(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortDE"), AWSECommerceServicePortType.class, features);
    }

    /**
     * 
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortES")
    public AWSECommerceServicePortType getAWSECommerceServicePortES() {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortES"), AWSECommerceServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortES")
    public AWSECommerceServicePortType getAWSECommerceServicePortES(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortES"), AWSECommerceServicePortType.class, features);
    }

    /**
     * 
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortFR")
    public AWSECommerceServicePortType getAWSECommerceServicePortFR() {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortFR"), AWSECommerceServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortFR")
    public AWSECommerceServicePortType getAWSECommerceServicePortFR(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortFR"), AWSECommerceServicePortType.class, features);
    }

    /**
     * 
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortIN")
    public AWSECommerceServicePortType getAWSECommerceServicePortIN() {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortIN"), AWSECommerceServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortIN")
    public AWSECommerceServicePortType getAWSECommerceServicePortIN(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortIN"), AWSECommerceServicePortType.class, features);
    }

    /**
     * 
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortIT")
    public AWSECommerceServicePortType getAWSECommerceServicePortIT() {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortIT"), AWSECommerceServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortIT")
    public AWSECommerceServicePortType getAWSECommerceServicePortIT(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortIT"), AWSECommerceServicePortType.class, features);
    }

    /**
     * 
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortJP")
    public AWSECommerceServicePortType getAWSECommerceServicePortJP() {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortJP"), AWSECommerceServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortJP")
    public AWSECommerceServicePortType getAWSECommerceServicePortJP(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortJP"), AWSECommerceServicePortType.class, features);
    }

    /**
     * 
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortUK")
    public AWSECommerceServicePortType getAWSECommerceServicePortUK() {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortUK"), AWSECommerceServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortUK")
    public AWSECommerceServicePortType getAWSECommerceServicePortUK(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortUK"), AWSECommerceServicePortType.class, features);
    }

    /**
     * 
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortUS")
    public AWSECommerceServicePortType getAWSECommerceServicePortUS() {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortUS"), AWSECommerceServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AWSECommerceServicePortType
     */
    @WebEndpoint(name = "AWSECommerceServicePortUS")
    public AWSECommerceServicePortType getAWSECommerceServicePortUS(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservices.amazon.com/AWSECommerceService/2011-08-01", "AWSECommerceServicePortUS"), AWSECommerceServicePortType.class, features);
    }

    private static URL __getWsdlLocation() {
        if (AWSECOMMERCESERVICE_EXCEPTION!= null) {
            throw AWSECOMMERCESERVICE_EXCEPTION;
        }
        return AWSECOMMERCESERVICE_WSDL_LOCATION;
    }

}