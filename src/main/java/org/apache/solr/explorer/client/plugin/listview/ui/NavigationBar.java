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

package org.apache.solr.explorer.client.plugin.listview.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import org.apache.solr.explorer.client.core.event.Multicaster;
import org.apache.solr.explorer.client.core.event.SearchResultUpdatedEvent;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.solr.search.SearchManager;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.context.query.QueryContext;
import org.apache.solr.explorer.client.core.model.result.Hit;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.util.Callback;
import org.gwtoolbox.commons.collections.client.Page;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Initializer;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.widget.client.button.SimpleLinkButton;

import static org.gwtoolbox.widget.client.panel.LayoutUtils.addGap;

/**
 * @author Uri Boness
 */
@Component
public class NavigationBar extends Composite {

    private HorizontalPanel main;

    private SimpleLinkButton prevButton;
    private SimpleLinkButton nextButton;
    private int currentPageIndex;

    // injected
    private SearchManager searchManager;
    private SearchContext searchContext;

    private Multicaster multicaster;

    public NavigationBar() {
        main = new HorizontalPanel();
        initWidget(main);
        setStyleName("NavigationBar");
    }

    @Initializer
    public void init() {
        prevButton = new SimpleLinkButton("Prev");
        prevButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                doPrev();
            }
        });
        prevButton.setStyleName("PrevButton");

        nextButton = new SimpleLinkButton("Next");
        nextButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                doNext();
            }
        });
        nextButton.setStyleName("NextButton");

        main.setVerticalAlignment(HorizontalPanel.ALIGN_BOTTOM);
        main.setVisible(false);
    }

    @EventHandler
    public void handleSearchResultUpdated(SearchResultUpdatedEvent event) {
        SearchResult result = event.getSearchResult();
        Page<Hit> page = result.getPart(Page.class);
        if (result == null || page.isSingle()) {
            main.setVisible(false);
            return;
        }
        currentPageIndex = page.getIndex();

        if (page.isFirst()) {
            prevButton.setEnabled(false);
            prevButton.addStyleDependentName("disabled");
        } else {
            prevButton.setEnabled(true);
            prevButton.removeStyleDependentName("disabled");
        }

        if (page.isLast()) {
            nextButton.setEnabled(false);
            nextButton.addStyleDependentName("disabled");
        } else {
            nextButton.setEnabled(true);
            nextButton.removeStyleDependentName("disabled");
        }

        int totalPageCount = page.getTotalPageCount();
        int currentPageNumber = page.getIndex() + 1;
        int firstPageNumber = Math.max(1, currentPageNumber - 10);
        int lastPageNubmer = Math.min(totalPageCount, currentPageNumber + 9);

        main.clear();
        Widget prevItem = createPrevLineItem();
        main.add(prevItem);
        main.setCellWidth(prevItem, "100px");
        addGap(main, "5px");
        main.setCellHorizontalAlignment(prevItem, HorizontalPanel.ALIGN_RIGHT);
        for (int i = firstPageNumber; i <= lastPageNubmer; i++) {
            main.add(createPageLineItem(i, currentPageNumber));
        }
        addGap(main, "5px");
        Widget nextItem = createNextLineItem();
        main.add(nextItem);
        main.setCellWidth(nextItem, "100px");

        main.setVisible(true);
    }

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        main.clear();
        setVisible(false);
    }

    //============================================== Helper Methods ====================================================

    private void doPrev() {
        doPageSelected(currentPageIndex - 1);
    }

    private void doNext() {
        doPageSelected(currentPageIndex + 1);
    }

    private void doPageSelected(int index) {
        QueryContext queryContext = searchContext.getContext(QueryContext.class);
        queryContext.setPageIndex(index);
        searchManager.search(new Callback<SearchResult>() {
            public void onSuccess(SearchResult result) {
                multicaster.notifySearchResultUpdated(NavigationBar.this, result);
            }
        });
    }

    private Widget createPrevLineItem() {
        Grid grid = new Grid(2, 1);
        grid.setWidth("100%");
        grid.setCellPadding(0);
        grid.setCellSpacing(0);
        grid.getCellFormatter().setHeight(0, 0, "39px");
        grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        grid.getCellFormatter().setStylePrimaryName(0, 0, "NavigationLogoLeft");
        grid.setWidget(1, 0, prevButton);
        grid.getCellFormatter().setHeight(1, 0, "25px");
        grid.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        return grid;
    }

    private Widget createNextLineItem() {
        Grid grid = new Grid(2, 1);
        grid.setWidth("100%");
        grid.setCellPadding(0);
        grid.setCellSpacing(0);
        grid.getCellFormatter().setHeight(0, 0, "39px");
        grid.getCellFormatter().setStylePrimaryName(0, 0, "NavigationLogoRight");
        grid.setWidget(1, 0, nextButton);
        grid.getCellFormatter().setHeight(1, 0, "25px");
        return grid;
    }

    private Widget createPageLineItem(int pageNumber, int currentPageNumber) {
        final int currentNumber = pageNumber;
        SimpleLinkButton pageNumberButton = new SimpleLinkButton(String.valueOf(pageNumber));
        pageNumberButton.setStylePrimaryName("PageNumber");
        pageNumberButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                doPageSelected(currentNumber - 1);
            }
        });

        if (currentNumber == currentPageNumber) {
            pageNumberButton.setEnabled(false);
            pageNumberButton.setStylePrimaryName("PageNumber-current");
        }
        Grid grid = new Grid(2, 1);
        grid.setCellPadding(0);
        grid.setCellSpacing(0);
        grid.getCellFormatter().setWidth(0, 0, "29px");
        grid.getCellFormatter().setHeight(0, 0, "29px");
        grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
        grid.getCellFormatter().setStylePrimaryName(0, 0, "NavigationLogoPageNumber");
        grid.setWidget(1, 0, pageNumberButton);
        grid.getCellFormatter().setHeight(1, 0, "25px");
        grid.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
        return grid;
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
}