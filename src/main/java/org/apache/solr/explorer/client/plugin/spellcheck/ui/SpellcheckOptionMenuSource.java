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

package org.apache.solr.explorer.client.plugin.spellcheck.ui;

import com.google.gwt.user.client.Command;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.search.SearchManager;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.ui.searchpane.AbstractOptionMenuSource;
import org.apache.solr.explorer.client.util.NoOpCallback;
import org.apache.solr.explorer.client.plugin.spellcheck.manager.SpellCheckManager;
import org.apache.solr.explorer.client.plugin.spellcheck.model.config.SpellCheckConfig;
import org.apache.solr.explorer.client.plugin.spellcheck.model.context.SpellCheckingContext;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.widget.client.menu.Menu;

/**
 * @author Uri Boness
 */
@Component
public class SpellcheckOptionMenuSource extends AbstractOptionMenuSource {

    private SpellCheckManager spellCheckManager;
    private SearchManager searchManager;
    private SearchContext searchContext;

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(SpellCheckConfig.class));
    }

    public void update(SolrCore solrCore, Menu menu) {
        SpellCheckConfig config = solrCore.getConfiguration().getConfig(SpellCheckConfig.class);
        if (config == null) {
            return;
        }
        Menu spellingMenu = new Menu(true);
        spellingMenu.addItem("Configure...", new Command() {
            public void execute() {
                doEditSpellChecking();
            }
        });
        spellingMenu.addItem("Build", new Command() {
            public void execute() {
                doBuildSpellChecking();
            }
        });
        spellingMenu.addItem("Reload", new Command() {
            public void execute() {
                doReloadSpellChecking();
            }
        });
        menu.addItem("Spellchecking", spellingMenu);
    }


    //================================================ Helper Methods ==================================================

    protected void doEditSpellChecking() {
        SpellCheckingContext spellCheckingContext = searchContext.getContext(SpellCheckingContext.class);
        SpellCheckingFormDialog dialog = new SpellCheckingFormDialog(spellCheckingContext) {
            protected void doSubmit(SpellCheckingContext spellChecking) {
                searchManager.reexecuteSearch();
            }
        };
        dialog.center();
        dialog.show();
    }

    protected void doBuildSpellChecking() {
        spellCheckManager.executeRebuild(new NoOpCallback<Void>());
    }

    protected void doReloadSpellChecking() {
        spellCheckManager.executeReload(new NoOpCallback<Void>());
    }


    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setSearchManager(SearchManager searchManager) {
        this.searchManager = searchManager;
    }

    @Inject
    public void setSpellCheckManager(SpellCheckManager spellCheckManager) {
        this.spellCheckManager = spellCheckManager;
    }

    @Inject
    public void setSearchContext(SearchContext searchContext) {
        this.searchContext = searchContext;
    }
}

