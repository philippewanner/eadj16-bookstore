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
public class MethodLogger {

    @AroundInvoke
    public Object logMethodCall(InvocationContext invocation) throws Exception {
        try {
            StringBuilder methodLog = new StringBuilder();
            methodLog.append("------------------------------------------------------------------------------------------------\n");
            methodLog.append("        logMethodCall: " + invocation.getMethod().getName() + "\n");
            
            for (Object o : invocation.getParameters()) {
                methodLog.append("            " + o.getClass() + " " + o.toString() + "\n");
            }

            methodLog.append("        ------------------------------------------------------------------------------------------------\n");
                        
            System.out.println(methodLog.toString());
        } catch (Exception e) {
        }

        return invocation.proceed();
    }
}
