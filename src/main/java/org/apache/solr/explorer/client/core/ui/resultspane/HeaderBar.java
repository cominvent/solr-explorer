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

package org.apache.solr.explorer.client.core.ui.resultspane;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import org.apache.solr.explorer.client.core.event.SearchResultUpdatedEvent;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.context.query.QueryContext;
import org.apache.solr.explorer.client.core.model.context.query.Sort;
import org.apache.solr.explorer.client.core.model.result.Hit;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.core.event.Multicaster;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.image.SolrExplorerImages;
import org.apache.solr.explorer.client.core.manager.solr.search.SearchManager;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.util.Callback;
import org.gwtoolbox.commons.collections.client.Page;
import org.gwtoolbox.commons.util.client.template.MapModel;
import org.gwtoolbox.commons.util.client.template.Template;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Initializer;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.widget.client.menu.Menu;
import org.gwtoolbox.widget.client.menu.MenuPopup;
import org.gwtoolbox.widget.client.popup.Popup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Uri Boness
 */
@Component
public class HeaderBar extends Composite {

    private static final Template infoTemplate = Template.compile("Results <b>${from}</b> - <b>${to}</b> of <b>${totalCount}</b> (<b>${time}</b> seconds)");

    private FlowPanel main;
    private HTML infoLabel;
    private static SortButton activeSortButton;
    private SortToolBar toolbar;

    // injected
    private SearchContext searchContext;
    private SearchManager searchManager;

    private SolrCore solrCore;
    private Multicaster multicaster;

    public HeaderBar() {
        main = new FlowPanel();
        initWidget(main);
        setStyleName("HeaderBar");
        sinkEvents(Event.ONCONTEXTMENU);
    }

    @Initializer
    public void init() {

        toolbar = new SortToolBar();

        infoLabel = new HTML();
        infoLabel.setStyleName("InfoLabel");

        HTML html = new HTML();
        html.setHeight("23px");

        main.add(toolbar);
        main.add(infoLabel);
        main.add(html);
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (event.getTypeInt() != Event.ONCONTEXTMENU) {
            return;
        }
        event.preventDefault();
        showMenu(event.getClientX(), event.getClientY());
    }

    public void showMenu(final int x, final int y) {
        final MenuPopup menu = buildMenu(x, y);
        menu.setPopupPositionAndShow(new Popup.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                menu.setPopupPosition(x, y);
            }
        });
    }

    @EventHandler
    public void handle(SearchResultUpdatedEvent event) {
        SearchResult result = event.getSearchResult();
        Page<Hit> hits = result.getPart(Page.class);
        MapModel model = new MapModel();
        model.set("from", hits.getFirstItemIndex() + 1);
        model.set("to", hits.getLastItemIndex() + 1);
        model.set("totalCount", hits.getTotalCount());
        model.set("time", result.getSearchTimeInSeconds());
        infoLabel.setHTML(infoTemplate.render(model));
    }

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        solrCore = event.getSolrCore();
        infoLabel.setHTML("");
        toolbar.reset();
    }

    public void setMessageText(String html) {
        infoLabel.setHTML(html);
    }

    //============================================== Helper Methods ====================================================

    private void doSortChanged(String fieldName, Boolean asc) {
        QueryContext queryContext = searchContext.getContext(QueryContext.class);
        Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = new Sort(fieldName, direction);
        queryContext.setSort(sort);
        searchManager.search(new Callback<SearchResult>() {
            public void onSuccess(SearchResult result) {
                multicaster.notifySearchResultUpdated(HeaderBar.this, result);
            }
        });
    }

    private void clearSort() {
        QueryContext queryContext = searchContext.getContext(QueryContext.class);
        queryContext.clearSort();
        searchManager.search(new Callback<SearchResult>() {
            public void onSuccess(SearchResult result) {
                multicaster.notifySearchResultUpdated(HeaderBar.this, result);
            }
        });
    }

    private MenuPopup buildMenu(final int x, final int y) {
        MenuPopup menu = new MenuPopup(true);

        menu.addItem("Add Sort...", new Command() {
            public void execute() {
                final SortFormDialog dialog = new SortFormDialog(solrCore.getSchema(), toolbar) {
                    protected void doSubmit(String name, String fieldName) {
                        toolbar.add(new SortButton(name, fieldName));
                    }
                };
                dialog.setPopupPositionAndShow(new Popup.PositionCallback() {
                    public void setPosition(int offsetWidth, int offsetHeight) {
                        dialog.setPopupPosition(x, y);
                    }
                });
            }
        });

        Menu subMenu = new Menu(true);
        for (final SortButton button : toolbar.getButtons()) {
            subMenu.addItem(button.getCaption(), new Command() {
                public void execute() {
                    toolbar.remove(button);
                    if (activeSortButton == button) {
                        activeSortButton = null;
                        clearSort();
                    }
                }
            });
        }
        menu.getMenu().addItem("Remove Sort", subMenu);

        return menu;
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


    //============================================== Inner Classes =====================================================

    class SortToolBar extends Composite {

        private FlowPanel buttonsPane;

        private List<SortButton> buttons = new ArrayList<SortButton>();
        private Set<String> sortFields = new HashSet<String>();

        private SortToolBar() {
            buttonsPane = new FlowPanel();
            initWidget(buttonsPane);
            reset();
            setStyleName("SortToolBar");
        }

        public void add(SortButton button) {
            buttonsPane.add(button);
            buttons.add(button);
            sortFields.add(button.getFieldName());
        }

        public void remove(SortButton button) {
            buttonsPane.remove(button);
            buttons.remove(button);
            sortFields.remove(button.getFieldName());
        }

        public List<SortButton> getButtons() {
            return buttons;
        }

        public boolean hasSortField(String fieldName) {
            return sortFields.contains(fieldName);
        }

        public void reset() {
            buttons.clear();
            buttonsPane.clear();
            sortFields.clear();
            add(new SortButton("Relevance", "score"));
        }
    }

    class SortButton extends Composite {

        private final String fieldName;

        private final Image image;

        private final Label label;

        private Boolean asc;

        public SortButton(String caption, String fieldName) {
            this(caption, fieldName, null);
        }

        public SortButton(String caption, String fieldName, Boolean ascByDefault) {
            this.fieldName = fieldName;
            this.asc = ascByDefault;
            label = new Label(caption);
            label.setStyleName("Label");
            image = SolrExplorerImages.Instance.get().sortHollowIcon().createImage();
            image.setStyleName("Icon");

            HorizontalPanel main = new HorizontalPanel();
            main.add(label);
            main.add(image);
            main.setCellVerticalAlignment(label, HorizontalPanel.ALIGN_MIDDLE);
            main.setCellVerticalAlignment(image, HorizontalPanel.ALIGN_MIDDLE);

            initWidget(main);
            setStyleName("SortButton");
            sinkEvents(Event.ONCLICK);
        }

        public String getCaption() {
            return label.getText();
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setTitle(String title) {
            label.setTitle(title);
        }

        @Override
        public void onBrowserEvent(Event event) {
            if (event.getTypeInt() == Event.ONCLICK) {
                handleClick();
            }
        }

        public void reset() {
            reset(null);
        }

        public void reset(Boolean asc) {
            this.asc = asc;
            if (asc == null) {
                label.removeStyleDependentName("on");
                label.setTitle("");
                SolrExplorerImages.Instance.get().sortHollowIcon().applyTo(image);
                return;
            }
            label.addStyleDependentName("on");
            if (asc) {
                SolrExplorerImages.Instance.get().sortAscIcon().applyTo(image);
                label.setTitle("Ascending");
            } else {
                SolrExplorerImages.Instance.get().sortDescIcon().applyTo(image);
                label.setTitle("Descending");
            }
        }

        //============================================== Helper Methods ================================================

        private void handleClick() {
            if (activeSortButton != null && activeSortButton != this) {
                activeSortButton.reset();
            }
            activeSortButton = this;
            asc = (asc == null) ? Boolean.TRUE : !asc;
            reset(asc);
            doSortChanged(fieldName, asc);
        }
    }
}
