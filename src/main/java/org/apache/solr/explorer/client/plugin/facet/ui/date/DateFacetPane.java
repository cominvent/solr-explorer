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

package org.apache.solr.explorer.client.plugin.facet.ui.date;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.apache.solr.explorer.client.core.event.SearchResultUpdatedEvent;
import org.apache.solr.explorer.client.core.manager.configuration.ConfigurationManager;
import org.apache.solr.explorer.client.core.manager.logging.Logger;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.search.SearchManager;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.context.query.QueryContext;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.core.plugin.Plugin;
import org.apache.solr.explorer.client.plugin.facet.manager.FacetManager;
import org.apache.solr.explorer.client.plugin.facet.model.config.FacetsConfig;
import org.apache.solr.explorer.client.plugin.facet.model.context.DateFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.context.FacetContext;
import org.apache.solr.explorer.client.plugin.facet.model.result.DateFacet;
import org.apache.solr.explorer.client.plugin.facet.model.result.FacetResult;
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
@Component(lazyInit = true)
public class DateFacetPane extends Composite implements Plugin, PanelLayout.PanelLayoutAware {

    private VerticalPanel main;

    private Map<String, DateFacetBox> boxByFacetId = new HashMap<String, DateFacetBox>();

    // injected
    private Logger logger;
    private SearchManager searchManager;
    private FacetManager facetManager;
    private SearchContext searchContext;
    private ConfigurationManager configurationManager;

    private SolrCore solrCore;

    private boolean active;

    public DateFacetPane() {
        main = new VerticalPanel();
        main.setWidth("100%");
        initWidget(main);
        setStylePrimaryName("FieldFacetPane"); // should be styled exactly the same as the field facets.
    }

    public void beforePanelLayoutDetach(PanelLayout panelLayout) {
    }

    public void afterPanelLayoutAttached(final PanelLayout panel) {

        SimpleMenuButton menuButton = panel.addMenuTool(PanelLayout.Tools.ADD, new MenuBuilder() {
            public void build(Menu menu) {
                menu.addItem("New Facet...", new Command() {
                    public void execute() {
                        showDateFacetDialog(panel.getAbsoluteLeft() + panel.getOffsetWidth(), panel.getAbsoluteTop());
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
                for (final DateFacetDefinition definition : facetsConfig.getDateFacetDefinitions()) {
                    String facetId = definition.getId();
                    if (searchContext.getContext(FacetContext.class).getDateFacetDefinition(facetId) != null) {
                        continue;
                    }
                    keepSeparator = true;
                    SimpleMenuItem item = menu.addItem("Add '" + definition.getName() + "' Facet", new Command() {
                        public void execute() {
                            addDateFacet(definition);
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
    public void handleSearchResultUpdated(SearchResultUpdatedEvent event) {
        SearchResult result = event.getSearchResult();
        if (!isActive()) {
            return;
        }
        FacetResult facetResult = result.getPart(FacetResult.class);
        SearchContext searchContext = result.getSearchContext();
        QueryContext queryContext = searchContext.getContext(QueryContext.class);
        for (DateFacet dateFacet : facetResult.getDateFacets()) {

            if (queryContext.containsFieldFilter(dateFacet.getFieldName())) {
                removeDateFacetBox(dateFacet.getId());
                continue;
            }
            
            if (logger.isDebugEnabled()) {
                logger.debug("adding date facet '" + dateFacet.getName() + "'");
            }

            addDateFacetBox(dateFacet);
        }
    }

    public void init(SolrCore solrCore) {
        active = solrCore.getConfiguration().isActive(FacetsConfig.class);
        if (active) {
            main.clear();
            boxByFacetId.clear();

            this.solrCore = solrCore;

            FacetsConfig facetsConfig = solrCore.getConfiguration().getConfig(FacetsConfig.class);
            for (DateFacetDefinition definition : facetsConfig.getDateFacetDefinitions()) {
                addDateFacet(definition);
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    //================================================ Helper Methods ==================================================

    private void showDateFacetDialog(final int x, final int y) {
        final FormDialog dialog = new DateFacetDialog(solrCore.getSchema(), true) {
            @Override
            protected void doSubmit(DateFacetDefinition definition) {
                addDateFacet(definition);
            }
        };
        dialog.setPopupPositionAndShow(new Popup.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                dialog.setPopupPosition(x, y);
            }
        });
    }

    private void addDateFacet(DateFacetDefinition definition) {
        FacetContext facetContext = searchContext.getContext(FacetContext.class);
        facetContext.addDateFacetDefintion(definition);

        facetManager.searchDateFacet(definition, new Callback<DateFacet>() {
            public void onSuccess(DateFacet dateFacet) {
                addDateFacetBox(dateFacet);
            }
        });
    }

    private void addDateFacetBox(DateFacet dateFacet) {
        DateFacetBox facetBox = boxByFacetId.get(dateFacet.getId());
        if (facetBox != null) {
            facetBox.reset(dateFacet);
            if (!facetBox.isAttached()) {
                main.add(facetBox);
                main.setCellWidth(facetBox, "100%");
            }
            return;
        }
        FacetContext facetContext = searchContext.getContext(FacetContext.class);
        DateFacetDefinition fieldFacetDefinition = facetContext.getDateFacetDefinition(dateFacet.getId());
        facetBox = new DateFacetBox(fieldFacetDefinition, solrCore, searchManager, facetManager);
        facetBox.reset(dateFacet);
        facetBox.setWidth("100%");
        main.add(facetBox);
        main.setCellWidth(facetBox, "100%");
        boxByFacetId.put(dateFacet.getId(), facetBox);
    }

    private void removeDateFacetBox(String facetId) {
        DateFacetBox facetBox = boxByFacetId.remove(facetId);
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
    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    @Inject
    public void setFacetManager(FacetManager facetManager) {
        this.facetManager = facetManager;
    }
}