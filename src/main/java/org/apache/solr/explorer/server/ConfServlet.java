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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Uri Boness
 */
public class ConfServlet extends HttpServlet {

    private final static Logger logger = LoggerFactory.getLogger(ConfServlet.class);

    private final static String CONF_FILE_PARAM = "config";

    private final static File configFile = resolveConfigFile();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        logger.info("Initializing configuration servlet");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (configFile == null) {
            resp.sendRedirect("cores-config.xml");
            return;
        }
        resp.setContentType("text/xml; charset=UTF-8");

        InputStream input = new FileInputStream(configFile);
        IOUtils.copy(input, resp.getOutputStream());
    }

    private static File resolveConfigFile() {
        String fileName = System.getProperty(CONF_FILE_PARAM);
        if (fileName == null) {
            return null;
        }
        return new File(fileName);
    }
}
