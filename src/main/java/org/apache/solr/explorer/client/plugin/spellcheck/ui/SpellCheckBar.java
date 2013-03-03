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

package org.apache.solr.explorer.client.plugin.spellcheck.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.*;
import org.apache.solr.explorer.client.core.event.Multicaster;
import org.apache.solr.explorer.client.core.event.SearchResultUpdatedEvent;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.solr.search.SearchManager;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.context.query.QueryContext;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.core.ui.resultspane.ResultsPaneTopComponent;
import org.apache.solr.explorer.client.core.ui.searchpane.SearchPane;
import org.apache.solr.explorer.client.util.Callback;
import org.apache.solr.explorer.client.plugin.spellcheck.model.config.SpellCheckConfig;
import org.apache.solr.explorer.client.plugin.spellcheck.model.result.SpellCheckResult;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Initializer;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.widget.client.button.SimpleLinkButton;
import org.gwtoolbox.widget.client.list.DropDownBox;

import java.util.List;
import java.util.Map;

import static org.gwtoolbox.widget.client.panel.LayoutUtils.addGap;

/**
 * @author Uri Boness
 */
@Component
@ResultsPaneTopComponent
public class SpellCheckBar extends Composite {

    private boolean active;

    private FlowPanel main;

    private FlowPanel suggestionsPane;
    private FlowPanel collatePane;
    private FlowPanel collatedLine;

    // injected
    private SearchContext searchContext;
    private SearchManager searchManager;
    private SearchPane searchPane;

    private Multicaster multicaster;


    public SpellCheckBar() {
        main = new FlowPanel();
        initWidget(main);
        setStyleName("SpellCheckBar");
    }

    @Initializer
    public void init() {
        FlowPanel panel = new FlowPanel();
        panel.addStyleName("inline");
        Label label = new Label("Did you mean:");
        label.setStylePrimaryName("Label");
        label.addStyleName("inline");
        panel.add(label);
        suggestionsPane = new FlowPanel();
        suggestionsPane.addStyleName("inline");
        panel.add(suggestionsPane);
        main.add(panel);

        collatedLine = new FlowPanel();
        collatedLine.addStyleName("inline");
        collatedLine.getElement().getStyle().setMarginLeft(7, Style.Unit.PX);
        label = new Label("Collated:");
        label.setStylePrimaryName("Label");
        label.addStyleName("inline");
        collatedLine.add(label);
        collatePane = new FlowPanel();
        collatePane.addStyleName("inline");
        collatedLine.add(collatePane);
        main.add(collatedLine);

        main.setWidth("100%");
    }

    @EventHandler
    public void handle(SearchResultUpdatedEvent event) {
        if (!active) {
            return;
        }
        SearchResult searchResult = event.getSearchResult();
        SpellCheckResult result = searchResult.getPart(SpellCheckResult.class);
        if (result == null || !result.hasSuggestions()) {
            hide();
            return;
        }
        suggestionsPane.clear();
        collatePane.clear();
        processSuggestions(result);
    }

    public void hide() {
        searchPane.removeHint(this);
    }

    public void show() {
        searchPane.addHint(this);
    }

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        active = event.getSolrCore().getConfiguration().isActive(SpellCheckConfig.class);
        suggestionsPane.clear();
        collatePane.clear();
        hide();
    }

    //============================================== Helper Methods ====================================================

    protected void processSuggestions(SpellCheckResult result) {
        final String collatedSuggestion = result.getCollatedSuggestion();

        Map<String, List<SpellCheckResult.Suggestion>> suggestionsByWord = result.getSuggestionsByOriginal();
        for (Map.Entry<String, List<SpellCheckResult.Suggestion>> entry : suggestionsByWord.entrySet()) {
            String word = entry.getKey();
            List<SpellCheckResult.Suggestion> suggestions = entry.getValue();
            if (suggestions.isEmpty()) {
                continue;
            }

            // if there's only one suggestion then showing as a link
            if (suggestions.size() == 1) {
                final SpellCheckResult.Suggestion suggestion = suggestions.get(0);
                SimpleLinkButton label = new SimpleLinkButton(suggestion.getWord(), new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        doSpellSuggestionChosen(suggestion.getWord());
                    }
                });
                suggestionsPane.add(label);
                continue;
            }

            // if there're multiple suggestions then showing them as a drop down box

            DropDownBox<SpellCheckResult.Suggestion> dropDown = new DropDownBox<SpellCheckResult.Suggestion>();
            dropDown.addOption(word, null);
            for (SpellCheckResult.Suggestion suggestion : suggestions) {
                dropDown.addOption(suggestion.getWord(), suggestion);
            }
            dropDown.addSelectionHandler(new SelectionHandler<SpellCheckResult.Suggestion>() {
                public void onSelection(SelectionEvent<SpellCheckResult.Suggestion> event) {
                    if (event.getSelectedItem() != null) {
                        doSpellSuggestionChosen(event.getSelectedItem().getWord());
                    }
                }

                public void valueSelected(Widget sender, SpellCheckResult.Suggestion suggestion) {

                }
            });
            suggestionsPane.add(dropDown);
        }
        
        if (collatedSuggestion != null) {
            SimpleLinkButton label = new SimpleLinkButton(collatedSuggestion, new ClickHandler() {
                public void onClick(ClickEvent event) {
                    doSpellSuggestionChosen(collatedSuggestion);
                }
            });
            label.addStyleName("inline");
            collatePane.add(label);
            collatedLine.setVisible(true);
        } else {
            collatedLine.setVisible(false);
        }

        show();
    }

    //============================================== Helper Methods ====================================================

    private void doSpellSuggestionChosen(String suggestion) {
        QueryContext queryContext = searchContext.getContext(QueryContext.class);
        queryContext.setTextQuery(suggestion);
        searchManager.search(new Callback<SearchResult>() {
            public void onSuccess(SearchResult result) {
                multicaster.notifySearchResultUpdated(SpellCheckBar.this, result);
            }
        });
    }

    //============================================== Setter/Getter =====================================================

    @Inject
    public void setMulticaster(Multicaster multicaster) {
        this.multicaster = multicaster;
    }

    @Inject
    public void setSearchContext(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    @Inject
    public void setSearchManager(SearchManager searchManager) {
        this.searchManager = searchManager;
    }

    @Inject
    public void setSearchPane(SearchPane searchPane) {
        this.searchPane = searchPane;
    }
}
