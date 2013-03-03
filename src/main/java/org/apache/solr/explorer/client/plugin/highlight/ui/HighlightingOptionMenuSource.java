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

package org.apache.solr.explorer.client.plugin.highlight.ui;

import com.google.gwt.user.client.Command;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.search.SearchManager;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.ui.searchpane.AbstractOptionMenuSource;
import org.apache.solr.explorer.client.plugin.highlight.model.config.HighlightingConfig;
import org.apache.solr.explorer.client.plugin.highlight.model.context.HighlightingContext;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.widget.client.menu.Menu;

/**
 * @author Uri Boness
 */
@Component
public class HighlightingOptionMenuSource extends AbstractOptionMenuSource {

    private SearchContext searchContext;
    private SearchManager searchManager;

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(HighlightingConfig.class));
    }

    public void update(SolrCore solrCore, Menu menu) {
        menu.addItem("Highlighting...", new Command() {
            public void execute() {
                doEditHighlighting();
            }
        });
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

    protected void doEditHighlighting() {
        HighlightingContext highlighting = searchContext.getContext(HighlightingContext.class);
        HighlightingFormDialog dialog = new HighlightingFormDialog(highlighting) {
            protected void doSubmit(HighlightingContext highlighting) {
                searchManager.reexecuteSearch();
            }
        };
        dialog.center();
        dialog.show();
    }
}
