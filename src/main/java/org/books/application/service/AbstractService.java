/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;

/**
 *
 * @author jan
 */
public abstract class AbstractService{
    
    protected static Logger logger;

    protected void logInfo(String msg) {        
        logger.log(Level.INFO, msg);
    }
    
    protected void logError(String msg) {        
        logger.log(Level.SEVERE, msg);
    }
    
    protected void logWarn(String msg) {        
        logger.log(Level.WARNING, msg);
    }
    
    public AbstractService(){        
        logger = Logger.getLogger(getClass().getName());
    }
    
    @PostActivate
    public void postActivate() {
        logInfo("postActivate");
    }

    @PostConstruct
    public void postConstruct() {
        logInfo("postConstruct");
    }

    @PreDestroy
    public void preDestroy() {
        logInfo("preDestroy");
    }

    @PrePassivate
    public void prePassivate() {
        logInfo("prePassivate");
    }
}
