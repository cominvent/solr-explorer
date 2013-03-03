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

package org.apache.solr.explorer.client.plugin.terms.ui;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.plugin.PluginBase;
import org.apache.solr.explorer.client.core.ui.searchpane.SearchPane;
import org.apache.solr.explorer.client.util.Callback;
import org.apache.solr.explorer.client.plugin.terms.manager.TermsManager;
import org.apache.solr.explorer.client.plugin.terms.model.config.TermsConfig;
import org.apache.solr.explorer.client.plugin.terms.model.context.TermsContext;
import org.apache.solr.explorer.client.plugin.terms.model.result.TermEntry;
import org.apache.solr.explorer.client.plugin.terms.model.result.TermResult;
import org.apache.solr.explorer.client.plugin.terms.model.result.TermsResult;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.widget.client.event.EnterKeyHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Uri Boness
 */
@Component
public class SearchAutoCompletionPlugin extends PluginBase {

    private SearchPane searchPane;

    private TermsManager termsManager;

    private TermsContext termsContext;

    private SuggestOracle oracle;

    private SuggestBox suggestBox;

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(TermsConfig.class));

        if (!isActive()) {
            searchPane.setSearchTextBox(new TextBox());
            return;
        }

        oracle = new SuggestOracle() {
            public void requestSuggestions(final Request request, final Callback callback) {
                findSuggestions(request.getQuery(), new org.apache.solr.explorer.client.util.Callback<List<Suggestion>>() {
                    public void onSuccess(List<Suggestion> suggestions) {
                        callback.onSuggestionsReady(request, new Response(suggestions));
                    }
                });

            }
        };
        suggestBox = new SuggestBox(oracle);
        suggestBox.setPopupStyleName("AutoCompletionPopup");

        suggestBox.addKeyDownHandler(new EnterKeyHandler() {
            @Override
            protected void onEnter(KeyDownEvent keyDownEvent) {
                if (!suggestBox.isSuggestionListShowing()) {
                    searchPane.executeSearch();
                }
            }
        });
        suggestBox.setAutoSelectEnabled(false);
        suggestBox.setAnimationEnabled(true);

        searchPane.setSearchTextBox(suggestBox);
    }


    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setSearchPane(SearchPane searchPane) {
        this.searchPane = searchPane;
    }

    @Inject
    public void setTermsManager(TermsManager termsManager) {
        this.termsManager = termsManager;
    }

    @Inject
    public void setTermsContext(TermsContext termsContext) {
        this.termsContext = termsContext;
    }

    //================================================ Helper Methods ==================================================

    private void findSuggestions(String query, final AsyncCallback<List<SuggestOracle.Suggestion>> callback) {
        if (!termsContext.isEnabled()) {
            callback.onSuccess(new ArrayList<SuggestOracle.Suggestion>());
            return;
        }
        String[] terms = query.trim().split("\\s");
        String lastTerm = terms[terms.length - 1];
        termsContext.setPrefix(lastTerm);
        termsManager.getTerms(new Callback<TermsResult>() {
            public void onSuccess(TermsResult termsResult) {
                List<SuggestOracle.Suggestion> suggestions = new ArrayList<SuggestOracle.Suggestion>();
                for (TermResult termResult : termsResult.getTermResultByFieldName().values()) {
                    for (TermEntry termEntry : termResult.getEntries()) {
                        suggestions.add(new TermSuggestion(termEntry));
                    }
                }
                callback.onSuccess(suggestions);
            }
        });
    }

    //================================================= Inner Classes ==================================================

    private class TermSuggestion implements SuggestOracle.Suggestion {

        private final TermEntry termEntry;

        private TermSuggestion(TermEntry termEntry) {
            this.termEntry = termEntry;
        }

        public String getDisplayString() {
            return termEntry.getValue() + " (" + termEntry.getCount() + ")";
        }

        public String getReplacementString() {
            String query = suggestBox.getText();
            int index = query.lastIndexOf(' ');
            if (index > 0) {
                query = query.substring(0, index) + " ";
            } else {
                query = "";
            }
            String entryValue = termEntry.getValue();
            if (entryValue.matches(".+\\s.+")) {
                entryValue = "\"" + entryValue + "\"";
            }
            return query + entryValue;
        }
    }
}
