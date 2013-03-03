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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.search.SearchManager;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.context.query.FieldValueFilter;
import org.apache.solr.explorer.client.core.model.context.query.QueryContext;
import org.apache.solr.explorer.client.plugin.facet.manager.FacetManager;
import org.apache.solr.explorer.client.plugin.facet.model.context.DateFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.context.FacetContext;
import org.apache.solr.explorer.client.plugin.facet.model.result.DateFacet;
import org.apache.solr.explorer.client.plugin.facet.model.result.FacetEntry;
import org.apache.solr.explorer.client.util.Callback;
import org.gwtoolbox.widget.client.button.SimpleLinkButton;
import org.gwtoolbox.widget.client.menu.MenuPopup;
import org.gwtoolbox.widget.client.panel.LayoutUtils;
import org.gwtoolbox.widget.client.popup.Popup;

import static org.gwtoolbox.widget.client.panel.LayoutUtils.addGap;

/**
 * @author Uri Boness
 */
public class DateFacetBox extends Composite {

    private final VerticalPanel entriesPanel;
    private final DateFacetDefinition definition;

    private final SearchManager searchManager;
    private final FacetManager facetManager;
    private final SolrCore solrCore;

    private Header header;

    public DateFacetBox(
            DateFacetDefinition definition,
            SolrCore solrCore,
            SearchManager searchManager,
            FacetManager facetManager) {

        this.definition = definition;
        this.searchManager = searchManager;
        this.facetManager = facetManager;
        this.solrCore = solrCore;

        header = new Header(definition.getName());
        header.setWidth("100%");

        DisclosurePanel main = new DisclosurePanel();
        main.setHeader(header);

        entriesPanel = new VerticalPanel();
        entriesPanel.setWidth("100%");

        main.setContent(entriesPanel);

        initWidget(main);
        setStyleName("FieldFacetBox");
    }

    public DateFacetDefinition getDefinition() {
        return definition;
    }

    public void reset(DateFacet dateFacet) {
        resetUI(dateFacet);
    }

    //============================================== Helper Methods ====================================================

    private void doRemove() {
        SearchContext searchContext = searchManager.getCurrentSearchContext();
        QueryContext queryContext = searchContext.getContext(QueryContext.class);
        queryContext.removeFieldValueFilter(definition.getFieldName());
        FacetContext facetContext = searchContext.getContext(FacetContext.class);
        facetContext.removeDateFacetDefinition(definition.getId());
        removeFromParent();
    }

    protected void doEdit() {
        final DateFacetDialog dialog = new DateFacetDialog(solrCore.getSchema(), definition, true) {
            @Override
            protected void doSubmit(DateFacetDefinition definition) {
                doRefresh();
            }
        };
        dialog.setPopupPositionAndShow(new Popup.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                dialog.setPopupPosition(header.getAbsoluteLeft() + header.getOffsetWidth(), header.getAbsoluteTop());
            }
        });
    }

    protected void doRefresh() {
        facetManager.searchDateFacet(definition, new Callback<DateFacet>() {
            public void onSuccess(DateFacet result) {
                resetUI(result);
            }
        });
    }

    protected void resetUI(final DateFacet facet) {
        entriesPanel.clear();
        if (facet == null) {
            return;
        }

        header.setEntryCount(facet.getEntries().size());
        for (final FacetEntry entry : facet.getEntries()) {
            String value = entry.getValue();
            value = definition.getGap().getUnit().fixFormat(value);
            final SimpleLinkButton facetEntry = new SimpleLinkButton(value + " (" + entry.getCount() + ")", true);
            facetEntry.setStyleName("FacetEntry");
            facetEntry.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    handleFacetEntrySelection(entry);
                }
            });
            entriesPanel.add(facetEntry);
            LayoutUtils.addGap(entriesPanel, "1px");
        }
    }

    protected void handleFacetEntrySelection(final FacetEntry entry) {
        SearchContext searchContext = searchManager.getCurrentSearchContext();
        String value = entry.getValue();
        String range = "[" + value + " TO " + value + "+" + definition.getGap().getCount() + definition.getGap().getUnit() + "]";
        QueryContext queryContext = searchContext.getContext(QueryContext.class);
        FieldValueFilter filter = new FieldValueFilter(definition.getId(), definition.getName(), definition.getFieldName(), range);
        filter.setValueDescription(definition.getGap().getUnit().fixFormat(value));
        queryContext.addFieldValueFilter(filter);
        searchManager.reexecuteSearch();
    }


    //================================================= Inner Classes ==================================================

    private class Header extends Composite {

        private Label titleLabel;
        private Label countLabel;

        private MenuPopup popup;

        private Header(String text) {
            titleLabel = new Label(text);
            titleLabel.setStylePrimaryName("TitleLabel");
            countLabel = new Label();
            countLabel.setStylePrimaryName("CountLabel");
            popup = new MenuPopup(true);
            popup.addItem("Refresh", new Command() {
                public void execute() {
                    doRefresh();
                }
            });
            if (!definition.isFixed()) {
                popup.addItem("Edit", new Command() {
                    public void execute() {
                        doEdit();
                    }
                });
            }
            popup.addItem("Remove", new Command() {
                public void execute() {
                    doRemove();
                }
            });

            HorizontalPanel main = new HorizontalPanel();
            main.add(titleLabel);
            main.setCellVerticalAlignment(titleLabel, HorizontalPanel.ALIGN_BOTTOM);
            addGap(main, "3px");
            main.add(countLabel);
            main.setCellVerticalAlignment(countLabel, HorizontalPanel.ALIGN_BOTTOM);

            SimplePanel simplePanel = new SimplePanel();
            simplePanel.setWidget(main);

            initWidget(simplePanel);
            setStylePrimaryName("Header");
            sinkEvents(Event.ONCONTEXTMENU);
        }

        @Override
        public void onBrowserEvent(final Event event) {
            if (event.getTypeInt() != Event.ONCONTEXTMENU) {
                return;
            }
            event.preventDefault();
            popup.setPopupPositionAndShow(new Popup.PositionCallback() {
                public void setPosition(int offsetWidth, int offsetHeight) {
                    popup.setPopupPosition(event.getClientX(), event.getClientY());
                }
            });
        }

        public void setEntryCount(int count) {
            countLabel.setText("(" + count + " Entries)");
        }

    }

}