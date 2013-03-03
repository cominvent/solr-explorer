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

package org.apache.solr.explorer.client.plugin.debug.ui;

import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.search.SearchManager;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.ui.searchpane.AbstractOptionMenuSource;
import org.apache.solr.explorer.client.plugin.debug.model.config.DebugConfig;
import org.apache.solr.explorer.client.plugin.debug.model.context.DebugContext;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.ioc.core.client.annotation.Order;
import org.gwtoolbox.widget.client.menu.Menu;
import org.gwtoolbox.widget.client.menu.item.CheckMenuItem;
import org.gwtoolbox.widget.client.menu.item.MenuItemSelectionListener;
import org.gwtoolbox.widget.client.menu.item.SelectionMenuItem;

/**
 * @author Uri Boness
 */
@Component
@Order(10)
public class DebugOptionMenuSource extends AbstractOptionMenuSource {

    private SearchContext searchContext;
    private SearchManager searchManager;

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(DebugConfig.class));
    }

    public void update(SolrCore solrCore, Menu menu) {
        DebugConfig config = solrCore.getConfiguration().getConfig(DebugConfig.class);
        Menu debugMenu = new Menu(true);
        CheckMenuItem item = new CheckMenuItem("Query", config.isDebugQuery());
        item.addListener(new MenuItemSelectionListener() {
            public void selectionChanged(SelectionMenuItem selectionMenuItem) {
                handleDebugQueryChanged(selectionMenuItem.isSelected());
            }
        });
        debugMenu.addItem(item);
        menu.addItem("Debug", debugMenu);
    }


    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setSearchContext(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    @Inject
    public void setSearchManager(SearchManager searchManager) {
        this.searchManager = searchManager;
    }

    //================================================ Helper Methods ==================================================

    private void handleDebugQueryChanged(boolean debugQuery) {
        DebugContext context = searchContext.getContext(DebugContext.class);
        context.setDebugQuery(debugQuery);
        searchManager.reexecuteSearch();
    }

}
