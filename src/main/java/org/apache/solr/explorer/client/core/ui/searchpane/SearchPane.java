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

package org.apache.solr.explorer.client.core.ui.searchpane;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.apache.solr.explorer.client.core.event.ConfigurationLoadedEvent;
import org.apache.solr.explorer.client.core.event.SearchResultUpdatedEvent;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrAdminManager;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.search.SearchManager;
import org.apache.solr.explorer.client.core.manager.ui.UIManager;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.context.query.FieldValueFilter;
import org.apache.solr.explorer.client.core.model.context.query.QueryContext;
import org.apache.solr.explorer.client.core.model.context.query.QueryFilter;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.util.Callback;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Initializer;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.widget.client.button.SimpleButton;
import org.gwtoolbox.widget.client.button.SimpleLinkButton;
import org.gwtoolbox.widget.client.button.SimpleMenuButton;
import org.gwtoolbox.widget.client.event.EnterKeyHandler;
import org.gwtoolbox.widget.client.list.DropDownBox;
import org.gwtoolbox.widget.client.panel.LayoutUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.gwtoolbox.widget.client.panel.LayoutUtils.addGap;

/**
 * The page where the user will be able to enter free text and execute search.
 *
 * @author Uri Boness
 */
@Component
public class SearchPane extends Composite {

    private VerticalPanel main;

    private Widget searchTextBox;
    private SimplePanel searchTextBoxHolder;
    private HorizontalPanel filtersPanel;
    private DropDownBox<SolrCoreConfiguration> coresDropDown;
    private VerticalPanel hintsPanel;

    private SimpleMenuButton optionsButton;
    private List<OptionMenuSource> optionMenuSources;

    private SimpleLinkButton aboutLink;
    private String helpUrl;

    // autowired
    private SearchContext searchContext;
    private SearchManager searchManager;
    private SolrAdminManager adminManager;
    private UIManager uiManager;

    private org.apache.solr.explorer.client.core.event.Multicaster multicaster;

    public SearchPane() {
        main = new VerticalPanel();
        initWidget(main);
        setStyleName("SearchPane");
    }

    @Initializer
    public void init() {

        HorizontalPanel coresSelectionPanel = new HorizontalPanel();
        coresSelectionPanel.setStylePrimaryName("CoresSelectionPanel");
        coresSelectionPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        coresSelectionPanel.add(new Label("Solr Core: "));
        addGap(coresSelectionPanel, "3px");
        coresDropDown = new DropDownBox<SolrCoreConfiguration>();
        coresDropDown.addSelectionHandler(new SelectionHandler<SolrCoreConfiguration>() {
            public void onSelection(SelectionEvent<SolrCoreConfiguration> event) {
                switchCore(event.getSelectedItem());
            }
        });
        coresSelectionPanel.add(coresDropDown);
        addGap(coresSelectionPanel, "5px");
        
        aboutLink = new SimpleLinkButton("About", new ClickHandler() {
            public void onClick(ClickEvent event) {
//                Window.open(helpUrl, "_blank", "scrollbars=yes");
                AboutDialog dialog = new AboutDialog();
                dialog.setClosable(true);
                dialog.center();
                dialog.show();
            }
        });
        aboutLink.setVisible(false);
        coresSelectionPanel.add(aboutLink);
        addGap(coresSelectionPanel, "5px");

        main.add(coresSelectionPanel);
        main.setCellHorizontalAlignment(coresSelectionPanel, VerticalPanel.ALIGN_RIGHT);

        searchTextBoxHolder = new SimplePanel();

        TextBox box = new TextBox();
        box.addKeyDownHandler(new EnterKeyHandler() {
            protected void onEnter(KeyDownEvent keyDownEvent) {
                executeSearch();
            }
        });
        setSearchTextBox(box);

        SimpleButton searchButton = new SimpleButton("Search", new ClickHandler() {
            public void onClick(ClickEvent event) {
                executeSearch();
            }
        });

        optionsButton = new SimpleMenuButton("Options", true);

        HorizontalPanel queryPanel = new HorizontalPanel();
        queryPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        queryPanel.add(new Label("Query:"));
        LayoutUtils.addGap(queryPanel, "10px");
        queryPanel.add(searchTextBoxHolder);
        LayoutUtils.addGap(queryPanel, "15px");
        queryPanel.add(searchButton);
        LayoutUtils.addGap(queryPanel, "15px");
        queryPanel.add(optionsButton);

        filtersPanel = new HorizontalPanel();

        hintsPanel = new VerticalPanel();
        hintsPanel.setWidth("100%");
        hintsPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
        hintsPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

        main.add(queryPanel);
        main.setCellHorizontalAlignment(queryPanel, VerticalPanel.ALIGN_CENTER);
        main.setCellVerticalAlignment(queryPanel, VerticalPanel.ALIGN_MIDDLE);
        addGap(main, "5px");
        main.add(hintsPanel);
        addGap(main, "10px");
        main.add(filtersPanel);
        main.setCellHorizontalAlignment(filtersPanel, VerticalPanel.ALIGN_CENTER);
        main.setCellVerticalAlignment(filtersPanel, VerticalPanel.ALIGN_MIDDLE);

    }

    @EventHandler
    public void handle(SearchResultUpdatedEvent event) {
        SearchResult result = event.getSearchResult();
        QueryContext queryContext = result.getSearchContext().getContext(QueryContext.class);
        String textQuery = queryContext.getTextQuery();
        if (searchTextBox instanceof HasText) {
            ((HasText)searchTextBox).setText(textQuery);
        }

        // updating the filters panel
        filtersPanel.clear();
        Map<String, HorizontalPanel> panelByField = new HashMap<String, HorizontalPanel>();
        for (final FieldValueFilter filter : queryContext.getFieldValueFilters()) {
            HorizontalPanel panel = panelByField.get(filter.getFieldName());
            if (panel == null) {
                panel = new HorizontalPanel();
                panelByField.put(filter.getFieldName(), panel);
                HTML filterLabel = new HTML(filter.getName() + ":&nbsp;");
                filterLabel.setStylePrimaryName("FiltersLabel");
                panel.add(filterLabel);
                if (filtersPanel.getWidgetCount() != 0) {
                    addGap(filtersPanel, "5px");
                    filtersPanel.add(new Label("|"));
                    addGap(filtersPanel, "5px");
                }
                filtersPanel.add(panel);
            }

            if (panel.getWidgetCount() > 1) {
                panel.add(new HTML(",&nbsp;"));
            }
            
            SimpleLinkButton link = new SimpleLinkButton(filter.getValueDescription(), true);
            link.setTitle(filter.getValue());
            link.setStylePrimaryName("Filter");
            link.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    doRemoveFieldFilter(filter);
                }
            });
            panel.add(link);
        }

        for (final QueryFilter filter : queryContext.getQueryFilters()) {
            if (filtersPanel.getWidgetCount() != 0) {
                addGap(filtersPanel, "5px");
                filtersPanel.add(new Label("|"));
                addGap(filtersPanel, "5px");
            }
            SimpleLinkButton link = new SimpleLinkButton(filter.getName(), true);
            link.setStylePrimaryName("Filter");
            link.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    doRemoveQueryFilter(filter);
                }
            });
            filtersPanel.add(link);
        }
    }

    @EventHandler
    public void handleConfigurationLoaded(ConfigurationLoadedEvent event) {
        helpUrl = event.getConfiguration().getHelpUrl();
        if (helpUrl != null) {
            aboutLink.setVisible(true);
        }

        for (String coreName : event.getConfiguration().getCoreNames()) {
            coresDropDown.addOption(coreName, event.getConfiguration().getCoreConfiguration(coreName));
        }
    }

    @EventHandler
    public void handleSolrCoreChanged(final SolrCoreChangedEvent event) {

        SolrCoreConfiguration coreConfig = event.getSolrCore().getConfiguration();

        // updating the core dropdown according to the selected core.
        if (event.getSource() != coresDropDown) {
            coresDropDown.setSelectedItem(coreConfig);
        }

        // we defer the menu construction after all menu sources were initialized with the new core.
        event.registerPostEventCommand(new Command() {
            public void execute() {
                optionsButton.getMenu().clearItems();
                for (OptionMenuSource source : optionMenuSources) {
                    if (source.isActive()) {
                        source.update(event.getSolrCore(), optionsButton.getMenu());
                    }
                }
                if (optionsButton.getMenu().getItems().isEmpty()) {
                    optionsButton.setVisible(false);
                }
            }
        });

        if (searchTextBox instanceof HasText) {
            ((HasText)searchTextBox).setText("");
        }
    }

    public void addHint(Widget hint) {
        hintsPanel.add(hint);
    }

    public void removeHint(Widget hint) {
        hintsPanel.remove(hint);
    }


    public Widget getSearchTextBox() {
        return searchTextBox;
    }

    public void setSearchTextBox(Widget searchTextBox) {
        this.searchTextBox = searchTextBox;
        searchTextBox.addStyleName("SearchBox");
        searchTextBoxHolder.setWidget(searchTextBox);
    }

    public void executeSearch() {
        QueryContext queryContext = searchContext.getContext(QueryContext.class);
        String textQuery = ((HasText)searchTextBox).getText();
        queryContext.setTextQuery(textQuery);
        queryContext.setPageIndex(0);
        searchManager.reexecuteSearch();
    }


    //============================================== Helper Methods ====================================================

    private void switchCore(SolrCoreConfiguration coreConfiguration) {
        adminManager.loadSolrCore(coreConfiguration, new Callback<SolrCore>() {
            public void onSuccess(SolrCore solrCore) {
                SolrCoreChangedEvent event = new SolrCoreChangedEvent(coresDropDown, solrCore);
                multicaster.notifyEvent(event);
            }

            public void onFailure(Throwable caught) {
                uiManager.showErrorMessage("Could not switch core: " + caught.getMessage(), new Callback<Void>() {
                    public void onSuccess(Void result) {
                        coresDropDown.setSelectedItem(searchContext.getSolrCore().getConfiguration());
                    }
                });
            }
        });
    }

    protected void doRemoveFieldFilter(FieldValueFilter filter) {
        QueryContext queryContext = searchContext.getContext(QueryContext.class);
        queryContext.removeFieldValueFilter(filter);
        searchManager.reexecuteSearch();
    }

    protected void doRemoveQueryFilter(QueryFilter filter) {
        QueryContext queryContext = searchContext.getContext(QueryContext.class);
        queryContext.removeQueryFilter(filter);
        searchManager.reexecuteSearch();
    }



    //============================================== Setter/Getter =====================================================

    @Inject
    public void setMulticaster(org.apache.solr.explorer.client.core.event.Multicaster multicaster) {
        this.multicaster = multicaster;
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
    public void setAdminManager(SolrAdminManager adminManager) {
        this.adminManager = adminManager;
    }

    @Inject
    public void setUiManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    @Inject
    public void setOptionMenuSources(List<OptionMenuSource> optionMenuSources) {
        this.optionMenuSources = optionMenuSources;
    }
}
