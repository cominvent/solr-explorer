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

package org.apache.solr.explorer.client.plugin.facet.ui.field;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
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
import org.apache.solr.explorer.client.plugin.facet.model.context.FieldFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.result.FacetResult;
import org.apache.solr.explorer.client.plugin.facet.model.result.FieldFacet;
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
public class FieldFacetPane extends Composite implements Plugin, PanelLayout.PanelLayoutAware {

    private VerticalPanel main;

    private Map<String, FieldFacetBox> boxByFacetId = new HashMap<String, FieldFacetBox>();

    // injected
    private Logger logger;
    private SearchManager searchManager;
    private FacetManager facetManager;
    private SearchContext searchContext;

    private SolrCore solrCore;

    private boolean active;

    public FieldFacetPane() {
        main = new VerticalPanel();
        main.setWidth("100%");
        ScrollPanel sp = new ScrollPanel(main);
        sp.setSize("100%", "100%");
        initWidget(sp);
        setStylePrimaryName("FieldFacetPane");
    }

    public void beforePanelLayoutDetach(PanelLayout panelLayout) {
    }

    public void afterPanelLayoutAttached(final PanelLayout panel) {
        SimpleMenuButton menuButton = panel.addMenuTool(PanelLayout.Tools.ADD, new MenuBuilder() {
            public void build(Menu menu) {
                menu.addItem("New Facet...", new Command() {
                    public void execute() {
                        showFieldFacetDialog(panel.getAbsoluteLeft() + panel.getOffsetWidth(), panel.getAbsoluteTop());
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

                FacetsConfig facetsConfig = solrCore.getConfiguration().getConfig(FacetsConfig.class);

                boolean keepSeparator = false;
                for (final FieldFacetDefinition definition : facetsConfig.getFieldFacetDefinitions()) {
                    if (searchContext.getContext(FacetContext.class).getFieldFacetDefinition(definition.getId()) != null) {
                        continue;
                    }
                    keepSeparator = true;
                    SimpleMenuItem item = menu.addItem("Add '" + definition.getName() + "' Facet", new Command() {
                        public void execute() {
                            addFieldFacet(definition);
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
        QueryContext queryContext = searchContext.getContext(QueryContext.class);
        FacetContext facetContext = searchContext.getContext(FacetContext.class);
        for (FieldFacet fieldFacet : facetResult.getFieldFacets()) {
            FieldFacetDefinition definition = facetContext.getFieldFacetDefinition(fieldFacet.getId());

            // if the facet is mutually exclusive, then checking if there is already a filter for it, if so, then
            // removing the facet box.
            if (definition.isMutuallyExclusive() && queryContext.containsFieldFilter(fieldFacet.getFieldName())) {
                removeFieldFacetBox(fieldFacet.getId());
                continue;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("adding field facet '" + fieldFacet.getName() + "'");
            }
            addFieldFacetBox(fieldFacet);
        }
    }

    public void init(SolrCore solrCore) {
        active = solrCore.getConfiguration().isActive(FacetsConfig.class);
        if (active) {
            main.clear();
            boxByFacetId.clear();

            this.solrCore = solrCore;

            FacetsConfig facetsConfig = solrCore.getConfiguration().getConfig(FacetsConfig.class);
            for (FieldFacetDefinition definition : facetsConfig.getFieldFacetDefinitions()) {
                addFieldFacet(definition);
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    //================================================ Helper Methods ==================================================

    private void showFieldFacetDialog(final int x, final int y) {
        final FormDialog dialog = new FacetFieldDialog(solrCore.getSchema(), true) {
            protected void doSubmit(FieldFacetDefinition definition) {
                addFieldFacet(definition);
            }
        };
        dialog.setPopupPositionAndShow(new Popup.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                dialog.setPopupPosition(x, y);
            }
        });
    }

    private void addFieldFacet(FieldFacetDefinition definition) {
        FacetContext facetContext = searchContext.getContext(FacetContext.class);
        facetContext.addFieldFacetDefintiion(definition);

        facetManager.searchFieldFacet(definition, new Callback<FieldFacet>() {
            public void onSuccess(FieldFacet fieldFacet) {
                addFieldFacetBox(fieldFacet);
            }
        });
    }

    private void addFieldFacetBox(FieldFacet fieldFacet) {
        FieldFacetBox facetBox = boxByFacetId.get(fieldFacet.getId());
        if (facetBox != null) {
            facetBox.reset(fieldFacet);
            if (!facetBox.isAttached()) {
                main.add(facetBox);
                main.setCellWidth(facetBox, "100%");
            }
            return;
        }
        FacetContext facetContext = searchContext.getContext(FacetContext.class);
        FieldFacetDefinition fieldFacetDefinition = facetContext.getFieldFacetDefinition(fieldFacet.getId());
        facetBox = new FieldFacetBox(fieldFacetDefinition, solrCore, searchManager, facetManager);
        facetBox.reset(fieldFacet);
        facetBox.setWidth("100%");
        main.add(facetBox);
        main.setCellWidth(facetBox, "100%");
        boxByFacetId.put(fieldFacet.getId(), facetBox);
    }

    private void removeFieldFacetBox(String facetId) {
        FieldFacetBox facetBox = boxByFacetId.remove(facetId);
        if (facetBox != null) {
            main.remove(facetBox);
        }
    }

    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Inject
    public void setSearchManager(SearchManager searchManager) {
        this.searchManager = searchManager;
    }

    @Inject
    public void setSearchContext(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    @Inject
    public void setFacetManager(FacetManager facetManager) {
        this.facetManager = facetManager;
    }
}
