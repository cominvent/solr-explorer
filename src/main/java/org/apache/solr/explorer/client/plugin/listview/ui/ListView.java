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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.*;
import org.apache.solr.explorer.client.core.event.NewSearchEvent;
import org.apache.solr.explorer.client.core.event.SearchResultUpdatedEvent;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.ui.resultspane.ResultsPaneView;
import org.gwtoolbox.ioc.core.client.annotation.*;
import org.gwtoolbox.widget.client.panel.CenterPanel;

import static org.gwtoolbox.widget.client.panel.LayoutUtils.addGap;

/**
 * @author Uri Boness
 */
@Order(0)
@Component
public class ListView extends Composite implements ResultsPaneView {

    private VerticalPanel main;
    private ScrollPanel contentScrollPanel;
    private Widget progressIndicator;

    private HitsPanel hitsPanel;
    private NavigationBar navigationBar;

    public ListView() {
        main = new VerticalPanel();
        contentScrollPanel = new ScrollPanel(main);
        initWidget(contentScrollPanel);
    }

    @Initializer
    public void init() {
        addGap(main, "10px");

        FlowPanel flowPanel = new FlowPanel();
        flowPanel.add(hitsPanel);

        main.add(flowPanel);
        main.add(navigationBar);
        main.setWidth("100%");
        main.setCellHorizontalAlignment(navigationBar, HasHorizontalAlignment.ALIGN_CENTER);

        progressIndicator = new CenterPanel(new Image("style/images/ajax-loader-bar.gif"));
        progressIndicator.setSize("100%", "100%");

    }

    public void init(SolrCore solrCore) {
        // do nothing
    }

    //TODO this needs to be active only when the configuration for this view is available. For now we assume it's always
    //TODO there but that shouldn't be the case.
    public boolean isActive() {
        return true;
    }

    @EventHandler
    public void handleNewSearch(NewSearchEvent event) {
        contentScrollPanel.setWidget(progressIndicator);
    }

    @EventHandler
    public void handleSearchResultUpdated(SearchResultUpdatedEvent event) {
        event.registerPostEventCommand(new Command() {
            public void execute() {
                contentScrollPanel.setWidget(main);
            }
        });
    }

    public String getId() {
        return "listView";
    }

    public String getName() {
        return "List View";
    }

    public String getDescription() {
        return "A Google-like list view of the search results";
    }

    public Widget getViewWidget() {
        return this;
    }

    public boolean isEnabled(SolrCore solrCore) {
        return true;
    }

    public void beforeHiding() {
    }

    public void afterShowing() {
    }

    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setHitsPanel(HitsPanel hitsPanel) {
        this.hitsPanel = hitsPanel;
    }

    @Inject
    public void setNavigationBar(NavigationBar navigationBar) {
        this.navigationBar = navigationBar;
    }
}
