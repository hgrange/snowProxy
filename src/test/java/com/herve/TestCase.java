// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
// end::copyright[]
package com.herve;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestCase {
    private static String siteURL;

    @BeforeAll
    public static void init() {
        String port = System.getProperty("http.port");
        String war = System.getProperty("war.name");
        //siteURL = "http://localhost:" + port + "/" + war + "/" + "servlet";
        siteURL = "http://localhost:" + port + "/health";
    }

    @Test
    public void testServlet() throws Exception {

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(siteURL);
        CloseableHttpResponse response = null;

        try {
            response = client.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            assertEquals(HttpStatus.SC_OK, statusCode, "HTTP GET failed");

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                                        response.getEntity().getContent()));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            assertTrue(buffer.toString().contains("UP"),
                "Unexpected response body: " + buffer.toString());
        } finally {
            response.close();
            httpGet.releaseConnection();
        }
    }
}