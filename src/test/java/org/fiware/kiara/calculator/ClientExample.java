 /* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * @file ClientExample.java
 * This file contains the main client code example.
 *
 * This file was generated by using the tool Kiaragen.
 *
 */
package org.fiware.kiara.calculator;

import org.fiware.kiara.client.Connection;
import org.fiware.kiara.Context;
import org.fiware.kiara.Kiara;
import org.fiware.kiara.client.AsyncCallback;
import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicPrimitive;
import org.fiware.kiara.dynamic.services.DynamicFunctionRequest;
import org.fiware.kiara.dynamic.services.DynamicFunctionResponse;
import org.fiware.kiara.dynamic.services.DynamicProxy;

/**
 * Class that acts as the main client entry point.
 *
 * @author Kiaragen tool.
 *
 */
public class ClientExample {

    public static void main(String[] args) throws Exception {
        System.out.println("CalculatorClientExample");

        // Initialize your data here
        int n1 = 5;

        int n2 = 10;

        int ret = 0;

        Context context = Kiara.createContext();

        //Connection connection = context.connect("tcp://127.0.0.1:9090?serialization=cdr");
        Connection connection = context.connect("kiara://127.0.0.1:8080/service");

        Calculator client = connection.getServiceProxy(CalculatorClient.class);

        try {
            ret = client.add(n1, n2);
            System.out.println(n1 + " + " + n2 + " = " + ret);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return;
        }

        DynamicProxy dclient = connection.getDynamicProxy("Calculator");

        // synchronous

        DynamicFunctionRequest drequest = dclient.createFunctionRequest("add");
        ((DynamicPrimitive) drequest.getParameterAt(0)).set(n1);
        ((DynamicPrimitive) drequest.getParameterAt(1)).set(n2);

        DynamicFunctionResponse dresponse = drequest.execute();
        if (dresponse.isException()) {
            DynamicData result = dresponse.getReturnValue();
            System.out.println("Exception = " + (DynamicPrimitive) result);
        } else {
            DynamicData result = dresponse.getReturnValue();
            System.out.println("Result = " + ((DynamicPrimitive) result).get());
        }

        // asynchronous

        drequest = dclient.createFunctionRequest("add");
        ((DynamicPrimitive) drequest.getParameterAt(0)).set(n1);
        ((DynamicPrimitive) drequest.getParameterAt(1)).set(n2);

        drequest.executeAsync(new AsyncCallback<DynamicFunctionResponse>() {

            @Override
            public void onSuccess(DynamicFunctionResponse response) {
                if (response.isException()) {
                    DynamicData result = response.getReturnValue();
                    System.out.println("Async Exception = " + (DynamicPrimitive) result);
                } else {
                    DynamicData result = response.getReturnValue();
                    System.out.println("Async Result = " + ((DynamicPrimitive) result).get());
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                System.err.println("Error: "+caught);
            }
        });


        Kiara.shutdown();

    }
}
