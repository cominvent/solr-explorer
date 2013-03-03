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

package org.apache.solr.explorer.client.plugin.highlight.manager;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.search.handler.AbstractSearchResultProcessor;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.result.Hit;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.plugin.highlight.model.config.HighlightingConfig;
import org.apache.solr.explorer.client.plugin.highlight.model.result.HighlightingResult;
import org.gwtoolbox.commons.collections.client.Page;
import org.gwtoolbox.commons.xml.client.DOMUtils;
import org.gwtoolbox.ioc.core.client.annotation.Component;

import static org.apache.solr.explorer.client.util.SolrDomUtils.getLstChild;
import static org.gwtoolbox.commons.xml.client.DOMUtils.children;

/**
 * @author Uri Boness
 */
@Component
public class HighlightingResultProcessor extends AbstractSearchResultProcessor {

    private String uniqueKeyField;

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(HighlightingConfig.class));
        if (isActive()) {
            uniqueKeyField = solrCore.getSchema().getUniqueKeyField();
        }
    }

    public void process(SearchContext context, Document document, SearchResult result) {
        HighlightingResult highlightingResult = new HighlightingResult();
        Element highlightingElement = getLstChild(document.getDocumentElement(), "highlighting");
        if (highlightingElement != null) {
            for (Element docElement : children(highlightingElement)) {
                String docId = docElement.getAttribute("name");
                for (Element fieldElement : children(docElement)) {
                    String fieldName = fieldElement.getAttribute("name");
                    String text = DOMUtils.getSingleChildValue(fieldElement, "str");
                    highlightingResult.addHighlightedField(docId, fieldName, text);
                }
            }
        }

        result.setPart(HighlightingResult.class, highlightingResult);
    }

    @Override
    public void postProcess(SearchContext context, SearchResult result) {
        HighlightingResult highlightingResult = result.getPart(HighlightingResult.class);
        Page<Hit> hits = result.getPart(Page.class);
        for (Hit hit : hits) {
            String uniqueKey = (String) hit.get(uniqueKeyField);
            for (String field : hit.keySet()) {
                if (highlightingResult.hasHighlightedText(uniqueKey,  field)) {
                    hit.put(field, highlightingResult.getHighlightedText(uniqueKey, field));
                }
            }
        }
    }
}
