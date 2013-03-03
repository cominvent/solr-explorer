/*
 * Copyright 2011 SearchWorkings.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.explorer.server;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Uri Boness
 */
public class SolrProxyServlet extends HttpServlet {

    private final static Logger logger = LoggerFactory.getLogger(SolrProxyServlet.class);

    private final static String TARGET_URL_PARAM = "Solr-Explorer-Target-URL";

    private final HttpClient client;

    public SolrProxyServlet() {
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        client = new HttpClient(connectionManager);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        logger.info("Initializing solr proxy servlet");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException {
        logger.info("Incoming request: " + req.getRequestURL());
        String url = req.getHeader(TARGET_URL_PARAM);
        if (url == null ) {
            throw new RuntimeException("A target url must be specified as a parameter named '" + TARGET_URL_PARAM + "'");
        }

        rsp.setContentType("text/plain; charset=utf-8");

        PostMethod post = new PostMethod(url);
        List<NameValuePair> params = extractAllParams(req);
        for (NameValuePair param : params) {
            post.addParameter(param);
        }

        try {
            int code = client.executeMethod(post);
            if (code != 200) {
                throw new RuntimeException("Bad respond from server (status: " + code + ")");
            }
            InputStream input = post.getResponseBodyAsStream();
            IOUtils.copy(input, rsp.getOutputStream());
        } finally {
            // making sure the connection is released back to the connection manager
            post.releaseConnection();
        }
    }


    //================================================ Helper Methods ==================================================

    private List<NameValuePair> extractAllParams(HttpServletRequest req) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Enumeration<String> paramNames = req.getParameterNames(); paramNames.hasMoreElements();) {
            String name = paramNames.nextElement();
            String[] values = req.getParameterValues(name);
            for (String value : values) {
                params.add(new NameValuePair(name, value));
            }
        }
        return params;
    }
}
