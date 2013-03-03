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

package org.apache.solr.explorer.client.core.manager.solr.search.handler;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.core.model.result.Hit;

import static org.apache.solr.explorer.client.util.SolrDomUtils.getIntChild;
import static org.apache.solr.explorer.client.util.SolrDomUtils.getLstChild;
import static org.apache.solr.explorer.client.util.SolrDomUtils.getStringChild;
import static org.apache.solr.explorer.client.util.json.JSONUtils.integerFieldValue;
import static org.apache.solr.explorer.client.util.json.JSONUtils.stringFieldValue;
import static org.gwtoolbox.commons.xml.client.DOMUtils.*;

import org.gwtoolbox.commons.collections.client.Page;
import org.gwtoolbox.commons.xml.client.DOMUtils;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;

/**
 * @author Uri Boness
 */
@Component
public class HitsResultProcessor extends AbstractSearchResultProcessor {

    public void process(SearchContext context, Document document, SearchResult result) {

        Element root = document.getDocumentElement();

        Element responseHeaderElement = getLstChild(root, "responseHeader");
        Element paramsElement = getLstChild(responseHeaderElement, "params");
        Element resultElement = getSingleChild(root, "result");

        int numFound = getIntegerAttribute(resultElement, "numFound");
        int start = getIntegerAttribute(resultElement, "start");
        int rows = Integer.parseInt(getStringChild(paramsElement, "rows"));

        Page<Hit> hits = new Page<Hit>(start / rows, rows, numFound);
        for (Element docElement : children(resultElement)) {
            Hit hit = new Hit();
            for (Element fieldElement : children(docElement)) {
                String fieldName = fieldElement.getAttribute("name");
                String value = convertToString(fieldElement);
                hit.put(fieldName, value);
            }
            hits.addItem(hit);
        }

        result.setPart(Page.class, hits);
    }


    //================================================ Helper Methods ==================================================

    private String convertToString(Element fieldElement) {
        if (!"arr".equals(fieldElement.getTagName())) {
            return getTextValue(fieldElement);
        }
        StringBuilder builder = new StringBuilder();
        for (Element frangmentElement : children(fieldElement)) {
            String value = getTextValue(frangmentElement);
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(value);
        }
        return builder.toString();
    }
}
