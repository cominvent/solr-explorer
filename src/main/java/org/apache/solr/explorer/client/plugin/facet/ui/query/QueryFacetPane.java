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

package org.apache.solr.explorer.client.plugin.facet.ui.query;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.apache.solr.explorer.client.core.event.SearchResultUpdatedEvent;
import org.apache.solr.explorer.client.core.manager.logging.Logger;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.search.SearchManager;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.context.query.QueryContext;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.core.plugin.Plugin;
import org.apache.solr.explorer.client.plugin.facet.manager.FacetManager;
import org.apache.solr.explorer.client.plugin.facet.model.config.FacetsConfig;
import org.apache.solr.explorer.client.plugin.facet.model.context.FacetContext;
import org.apache.solr.explorer.client.plugin.facet.model.context.QueryFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.result.FacetResult;
import org.apache.solr.explorer.client.plugin.facet.model.result.QueryFacet;
import org.apache.solr.explorer.client.util.Callback;
import org.apache.solr.explorer.client.util.ui.widget.FormDialog;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.widget.client.button.SimpleMenuButton;
import org.gwtoolbox.widget.client.menu.Menu;
import org.gwtoolbox.widget.client.menu.MenuBuilder;
import org.gwtoolbox.widget.client.menu.MenuPopup;
import org.gwtoolbox.widget.client.menu.MenuSeparator;
import org.gwtoolbox.widget.client.menu.item.SimpleMenuItem;
import org.gwtoolbox.widget.client.panel.layout.PanelLayout;
import org.gwtoolbox.widget.client.popup.Popup;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Uri Boness
 */
@Component
public class QueryFacetPane extends Composite implements Plugin, PanelLayout.PanelLayoutAware {

    private VerticalPanel main;

    private Map<String, QueryFacetBox> boxByFacetId = new HashMap<String, QueryFacetBox>();

    // injected
    private SearchManager searchManager;
    private FacetManager facetManager;
    private FacetContext facetContext;
    private Logger logger;

    private FacetsConfig facetsConfig;

    private boolean active;

    public QueryFacetPane() {

        main = new VerticalPanel();
        main.setWidth("100%");
        initWidget(main);
        setStylePrimaryName("QueryFacetPane");
    }

    public void beforePanelLayoutDetach(PanelLayout panelLayout) {
    }

    public void afterPanelLayoutAttached(final PanelLayout panel) {
        SimpleMenuButton menuButton = panel.addMenuTool(PanelLayout.Tools.ADD, new MenuBuilder() {
            public void build(Menu menuBar) {
                menuBar.addItem("New Facet...", new Command() {
                    public void execute() {
                        showQueryFacetDialog(panel.getAbsoluteLeft() + panel.getOffsetWidth(), panel.getAbsoluteTop());
                    }
                });
            }
        });

        menuButton.setCallback(new SimpleMenuButton.Callback() {
            public boolean beforeOpen(MenuPopup menuPopup) {
                Menu menu = menuPopup.getMenu();
                menu.removeItemsInCategory("contextAware");

                MenuSeparator separator = menu.addSeparator();
                separator.setCategory("contextAware");

                boolean keepSeparator = false;
                for (final QueryFacetDefinition definition : facetsConfig.getQueryFacetDefinitions()) {
                    if (facetContext.getQueryFacetDefinition(definition.getId()) != null) {
                        continue;
                    }
                    keepSeparator = true;
                    SimpleMenuItem item = menu.addItem("Add '" + definition.getName() + "' Facet", new Command() {
                        public void execute() {
                            addQueryFacet(definition);
                        }
                    });
                    item.setCategory("contextAware");
                }

                if (!keepSeparator) {
                    menu.removeSeparator(separator);
                }
                return true;
            }
        });
    }

    public void beforePanelLayoutDetach() {
    }

    @EventHandler
    public void handle(SearchResultUpdatedEvent event) {
        if (!isActive()) {
            return;
        }
        SearchResult result = event.getSearchResult();
        FacetResult facetResult = result.getPart(FacetResult.class);
        SearchContext searchContext = result.getSearchContext();
        FacetContext facetContext = searchContext.getContext(FacetContext.class);
        QueryContext queryContext = searchContext.getContext(QueryContext.class);
        for (QueryFacetDefinition queryFacetDefinition : facetContext.getQueryFacetDefinitions()) {
            String facetId = queryFacetDefinition.getId();
            if (boxByFacetId.containsKey(facetId)) {
                removeQueryFacetBox(facetId);
            }
        }

        for (QueryFacet queryFacet : facetResult.getQueryFacets()) {
            if (!queryContext.containsQueryFilter(queryFacet.getId())) {
                if (logger.isDebugEnabled()) {
                    logger.debug("adding query facet '" + queryFacet.getName() + "'");
                }
                addQueryFacetBox(queryFacet);
            }
        }
    }

    public void init(SolrCore solrCore) {
        active = solrCore.getConfiguration().isActive(FacetsConfig.class);
        if (active) {
            main.clear();
            boxByFacetId.clear();

            facetsConfig = solrCore.getConfiguration().getConfig(FacetsConfig.class);

            for (QueryFacetDefinition definition : facetsConfig.getQueryFacetDefinitions()) {
                addQueryFacet(definition);
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    //================================================ Helper Methods ==================================================

    private void showQueryFacetDialog(final int x, final int y) {
        final FormDialog dialog = new QueryFacetDialog(true) {
            protected void doSubmit(QueryFacetDefinition definition) {
                addQueryFacet(definition);
            }
        };
        dialog.setPopupPositionAndShow(new Popup.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                dialog.setPopupPosition(x, y);
            }
        });
    }

    protected void addQueryFacet(QueryFacetDefinition definition) {
        facetContext.addQueryFacetDefintiion(definition);
        facetManager.searchQueryFacet(definition, new Callback<QueryFacet>() {
            public void onSuccess(QueryFacet queryFacet) {
                addQueryFacetBox(queryFacet);
            }
        });
    }

    protected void addQueryFacetBox(QueryFacet queryFacet) {
        QueryFacetDefinition definition = facetContext.getQueryFacetDefinition(queryFacet.getId());
        QueryFacetBox facetBox = boxByFacetId.get(definition.getId());
        if (facetBox != null) {
            facetBox.reset(queryFacet);
            if (!facetBox.isAttached()) {
                main.add(facetBox);
            }
            return;
        }
        facetBox = new QueryFacetBox(definition, searchManager, facetManager);
        facetBox.reset(queryFacet);
        facetBox.setWidth("100%");
        main.add(facetBox);
        boxByFacetId.put(definition.getId(), facetBox);
    }

    private void removeQueryFacetBox(String facetid) {
        QueryFacetBox facetBox = boxByFacetId.remove(facetid);
        main.remove(facetBox);
    }


    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setSearchManager(SearchManager searchManager) {
        this.searchManager = searchManager;
    }

    @Inject
    public void setSearchContext(SearchContext searchContext) {
        this.facetContext = searchContext.getContext(FacetContext.class);
    }

    @Inject
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Inject
    public void setFacetManager(FacetManager facetManager) {
        this.facetManager = facetManager;
    }
}