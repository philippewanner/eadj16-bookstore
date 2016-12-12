/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 *
 * @author jlue4
 */
public class MyPOCInterceptor {

    @AroundInvoke
    public Object findBookCatcher(InvocationContext invocation) throws Exception {
        System.out.println("findBookCatcher");

        try {
            return invocation.proceed();
        } finally {
        }
    }
}
