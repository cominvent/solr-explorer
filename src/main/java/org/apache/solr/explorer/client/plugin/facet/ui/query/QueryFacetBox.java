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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import org.apache.solr.explorer.client.core.manager.solr.search.SearchManager;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.context.query.QueryContext;
import org.apache.solr.explorer.client.core.model.context.query.QueryFilter;
import org.apache.solr.explorer.client.util.Callback;
import org.apache.solr.explorer.client.plugin.facet.manager.FacetManager;
import org.apache.solr.explorer.client.plugin.facet.model.context.FacetContext;
import org.apache.solr.explorer.client.plugin.facet.model.context.QueryFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.result.QueryFacet;
import org.gwtoolbox.widget.client.button.SimpleLinkButton;
import org.gwtoolbox.widget.client.menu.MenuPopup;
import org.gwtoolbox.widget.client.popup.Popup;

import static org.gwtoolbox.widget.client.panel.LayoutUtils.addGap;

/**
 * @author Uri Boness
 */
public class QueryFacetBox extends Composite {

    private final QueryFacetDefinition definition;
    private final SearchManager searchManager;
    private final FacetManager facetManager;

    private HorizontalPanel main;

    private Label countLabel;

    private MenuPopup menuPopup;

    public QueryFacetBox(
            QueryFacetDefinition definition,
            SearchManager searchManager,
            FacetManager facetManager) {

        this.definition = definition;
        this.searchManager = searchManager;
        this.facetManager = facetManager;

        main = new HorizontalPanel();

        SimpleLinkButton nameLabel = new SimpleLinkButton(definition.getName() + ":", true);
        nameLabel.setTitle(definition.getQuery());
        nameLabel.setStylePrimaryName("Label");
        nameLabel.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                handleQueryClicked();
            }
        });
        main.add(nameLabel);

        addGap(main, "3px");

        countLabel = new Label();
        countLabel.setStylePrimaryName("Count");
        main.add(countLabel);

        menuPopup = buildMenuPopup();

        SimplePanel simplePanel = new SimplePanel();
        simplePanel.setWidget(main);

        initWidget(simplePanel);
        setStyleName("QueryFacetBox");
        sinkEvents(Event.ONCONTEXTMENU);
    }

    public void onBrowserEvent(Event event) {
        if (event.getTypeInt() != Event.ONCONTEXTMENU) {
            return;
        }
        event.preventDefault();
        showMenuPopup(event.getClientX(), event.getClientY());
    }

    public void reset(QueryFacet queryFacet) {
        String count = (queryFacet == null) ? "" : String.valueOf(queryFacet.getCount());
        countLabel.setText(count);
    }


    //============================================== Helper Methods ====================================================

    private MenuPopup buildMenuPopup() {

        MenuPopup menu = new MenuPopup(true);
        menu.addItem("Refresh", new Command() {
            public void execute() {
                doRefresh();
            }
        });
        if (!definition.isFixed()) {
            menu.addItem("Edit", new Command() {
                public void execute() {
                    doEdit();
                }
            });
        }
        menu.addItem("Remove", new Command() {
            public void execute() {
                doRemove();
            }
        });

        return menu;
    }

    private void showMenuPopup(final int x, final int y) {
        menuPopup.setPopupPositionAndShow(new Popup.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                menuPopup.setPopupPosition(x, y);
            }
        });
    }

    private void handleQueryClicked() {
        SearchContext searchContext = searchManager.getCurrentSearchContext();
        QueryContext queryContext = searchContext.getContext(QueryContext.class);
        queryContext.addQueryFilter(new QueryFilter(definition.getId(), definition.getName(), definition.getQuery()));
        searchManager.reexecuteSearch();
    }

    private void doRemove() {
        menuPopup.hide();
        SearchContext searchContext = searchManager.getCurrentSearchContext();
        FacetContext facetContext = searchContext.getContext(FacetContext.class);
        facetContext.removeQueryFacetDefinition(definition.getId());
        removeFromParent();
    }

    private void doRefresh() {
        menuPopup.hide();
        facetManager.searchQueryFacet(definition, new Callback<QueryFacet>() {
            public void onSuccess(QueryFacet result) {
                reset(result);
            }
        });
    }

    private void doEdit() {
        menuPopup.hide();
        QueryFacetDialog dialog = new QueryFacetDialog(definition) {
            protected void doSubmit(QueryFacetDefinition queryFacetDefinition) {
                facetManager.searchQueryFacet(definition, new Callback<QueryFacet>() {
                    public void onSuccess(QueryFacet facet) {
                        reset(facet);
                    }
                });
            }
        };
        dialog.center();
        dialog.show();
    }
}
