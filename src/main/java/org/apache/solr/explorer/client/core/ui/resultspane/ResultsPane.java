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

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.ResizeComposite;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.gwtoolbox.ioc.core.client.annotation.*;
import org.gwtoolbox.widget.client.panel.layout.DockLayout;
import org.gwtoolbox.widget.client.panel.layout.tab.TabBar;
import org.gwtoolbox.widget.client.panel.layout.tab.TabLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The page in which all search results will be displayed.
 *
 * @author Uri Boness
 */
@Component
public class ResultsPane extends ResizeComposite {

    private DockLayout main;

    // injected
    private HeaderBar headerBar;

    private TabLayout tabs;

    private Map<String, ResultsPaneView> viewById = new HashMap<String, ResultsPaneView>();

    public ResultsPane() {
        main = new DockLayout();
        initWidget(main);
        setStyleName("ResultsPane");
    }

    @Initializer
    public void init() {
        main.addNorth(headerBar, 25);
        tabs = new TabLayout();
        main.add(tabs);
        tabs.addSelectionHandler(new SelectionHandler<TabBar.Tab>() {
            public void onSelection(SelectionEvent<TabBar.Tab> event) {
                String id = event.getSelectedItem().getId();
                ResultsPaneView view = viewById.get(id);
                view.afterShowing();
            }
        });
    }

    @EventHandler
    public void handleSolrCoreChanged(final SolrCoreChangedEvent event) {
        event.registerPostEventCommand(new Command() {
            public void execute() {
                SolrCore solrCore = event.getSolrCore();
                tabs.clear();
                String firstTabId = null;
                for (ResultsPaneView view : viewById.values()) {
                    if (view.isEnabled(solrCore)) {
                        if (firstTabId == null) {
                            firstTabId = view.getId();
                        }
                        tabs.addTab(view.getId(), view.getName(), view.getViewWidget());
                    }
                }
                if (firstTabId != null) {
                    tabs.setSelectedTab(firstTabId);
                }
            }
        });
    }

    public TabLayout getTabs() {
        return tabs;
    }

    //============================================== Setter/Getter =====================================================

    @Inject
    public void setHeaderBar(HeaderBar headerBar) {
        this.headerBar = headerBar;
    }

    @Inject(by = InjectionType.TYPE)
    public void setViews(List<ResultsPaneView> views) {
//        this.views = views;
        for (ResultsPaneView view : views) {
            viewById.put(view.getId(), view);
        }
    }
}
