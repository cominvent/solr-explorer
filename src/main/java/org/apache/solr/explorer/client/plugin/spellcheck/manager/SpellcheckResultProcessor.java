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

package org.apache.solr.explorer.client.plugin.spellcheck.manager;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.search.handler.AbstractSearchResultProcessor;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.plugin.spellcheck.model.config.SpellCheckConfig;
import org.apache.solr.explorer.client.plugin.spellcheck.model.result.SpellCheckResult;
import org.apache.solr.explorer.client.util.SolrDomUtils;
import org.gwtoolbox.ioc.core.client.annotation.Component;

import java.util.ArrayList;
import java.util.List;

import static org.apache.solr.explorer.client.util.SolrDomUtils.*;
import static org.gwtoolbox.commons.xml.client.DOMUtils.children;
import static org.gwtoolbox.commons.xml.client.DOMUtils.getTextValue;

/**
 * @author Uri Boness
 */
@Component
public class SpellcheckResultProcessor extends AbstractSearchResultProcessor {

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(SpellCheckConfig.class));
    }

    public void process(SearchContext context, Document document, SearchResult result) {
        Element element = getLstChild(document.getDocumentElement(), "spellcheck");
        if (element == null) {
            return;
        }
        Element suggestionsElement = getLstChild(element, "suggestions");
        SpellCheckResult spellCheckResult = new SpellCheckResult();
        for (Element termElement : children(suggestionsElement)) {
            String term = termElement.getAttribute("name");

            // if the key is collation then the next value is the collated suggestion.
            if ("collation".equals(term)) {
                String collation = getTextValue(termElement);
                spellCheckResult.setCollatedSuggestion(collation);
                continue;
            }

            if ("correctlySpelled".equals(term)) {
                continue;
            }

            // the key must be an original word
            Element suggestionElement = SolrDomUtils.getChild(termElement, "arr", "suggestion");
            List<SpellCheckResult.Suggestion> suggestionsList = new ArrayList<SpellCheckResult.Suggestion>();
            for (Element wordElement : children(suggestionElement)) {
                if ("lst".equals(wordElement.getTagName())) {
                    // collation - also container frequencies
                    String word = getStringChild(wordElement, "word");
                    int freq = getIntChild(wordElement, "freq");
                    SpellCheckResult.Suggestion suggestion = new SpellCheckResult.Suggestion(word, freq);
                    suggestionsList.add(suggestion);
                } else {
                    // no collation - only contains text
                    String word = getTextValue(wordElement);
                    SpellCheckResult.Suggestion suggestion = new SpellCheckResult.Suggestion(word, -1);
                    suggestionsList.add(suggestion);
                }
            }
            spellCheckResult.addSuggestions(term, suggestionsList);
        }

        result.setPart(SpellCheckResult.class, spellCheckResult);
    }
}