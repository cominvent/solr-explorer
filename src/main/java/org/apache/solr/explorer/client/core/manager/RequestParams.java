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

package org.apache.solr.explorer.client.core.manager;

import com.google.gwt.http.client.URL;
import org.apache.solr.explorer.client.util.LocalParams;
import org.apache.solr.explorer.client.util.SolrDateUtils;

import java.util.*;

/**
 * @author Uri Boness
 */
public class RequestParams {

    private Map<String, List<String>> valuesByName;

    public RequestParams() {
        valuesByName = new HashMap<String, List<String>>();
    }

/**
     * Adds an int parameter that will be added to the query string. The given name will be URL encoded.
     *
     * @param name The name of the parameter.
     * @param value The value of the parameter.
     * @return This URLBuilder to support fluent interface.
     */
    public RequestParams addParameter(String name, int value) {
        return addParameter(name, String.valueOf(value));
    }

    /**
     * Adds a double parameter that will be added to the query string. The given name will be URL encoded.
     *
     * @param name The name of the parameter.
     * @param value The value of the parameter.
     * @return This URLBuilder to support fluent interface.
     */
    public RequestParams addParameter(String name, double value) {
        return addParameter(name, String.valueOf(value));
    }

    /**
     * Adds a boolean parameter that will be added to the query string. The given name will be URL encoded.
     *
     * @param name The name of the parameter.
     * @param value The value of the parameter.
     * @return This URLBuilder to support fluent interface.
     */
    public RequestParams addParameter(String name, boolean value) {
        return addParameter(name, String.valueOf(value).toLowerCase());
    }

    public RequestParams addParameter(String name, LocalParams localParams) {
        return addParameter(name, localParams.getValue());
    }

    public RequestParams addParameter(String name, Date date) {
        return addParameter(name, SolrDateUtils.format(date));
    }

    /**
     * Adds a parameter that will be added to the query string. Both the name and the value will be URL encoded.
     *
     * @param name The name of the parameter.
     * @param value The value of the parameter.
     * @return This URLBuilder to support fluent interface.
     */
    public RequestParams addParameter(String name, String value) {
        List<String> values = valuesByName.get(name);
        if (values == null) {
            values = new ArrayList<String>();
            valuesByName.put(name, values);
        }
        values.add(value);
        return this;
    }

    public RequestParams setParameter(String name, String value) {
        List<String> values = valuesByName.get(name);
        if (values == null) {
            values = new ArrayList<String>();
            valuesByName.put(name, values);
        } else {
            values.clear();
        }
        values.add(value);
        return this;
    }

    public RequestParams setParameter(String name, int value) {
        return addParameter(name, Integer.valueOf(value));
    }

    public RequestParams setParameter(String name, long value) {
        return addParameter(name, Long.valueOf(value));
    }

    public RequestParams setParameter(String name, double value) {
        return addParameter(name, Double.valueOf(value));
    }

    public RequestParams setParameter(String name, float value) {
        return addParameter(name, Float.valueOf(value));

    }

    public RequestParams setParameter(String name, boolean value) {
        return addParameter(name, Boolean.valueOf(value));
    }

    public RequestParams setParameter(String name, Date value) {
        return addParameter(name, SolrDateUtils.format(value));
    }


    /**
     * Removes a parameter from the query string, returning the value of the parameter
     *
     * @param name Name of the parameter to remove
     * @return Value of the parameter removed, or {@code null} if the parameter was not found
     */
    public List<String> removeParameter(String name) {
        return valuesByName.remove(name);
    }


    public Map<String, List<String>> getParams() {
        return valuesByName;
    }

    public String buildQueryString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : valuesByName.entrySet()) {
            String name = entry.getKey();
            for (String value : entry.getValue()) {
                if (builder.length() != 0) {
                    builder.append("&");
                }
                builder.append(name).append("=").append(value);
            }
        }
        return builder.toString();
    }

    public String buildEncodedQueryString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : valuesByName.entrySet()) {
            String name = entry.getKey();
            for (String value : entry.getValue()) {
                if (builder.length() != 0) {
                    builder.append("&");
                }
                builder.append(encode(name)).append("=").append(encode(value));
            }
        }
        return builder.toString();
    }


    //================================================ Helper Methods ==================================================

    private native static String encode(String text) /*-{
        return encodeURIComponent(text);
    }-*/;
}
