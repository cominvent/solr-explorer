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

package org.apache.solr.explorer.client.plugin.dih.ui;

import com.google.gwt.user.client.Command;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.ui.searchpane.AbstractOptionMenuSource;
import org.apache.solr.explorer.client.plugin.dih.manager.DIHManager;
import org.apache.solr.explorer.client.plugin.dih.model.config.DIHConfig;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.widget.client.menu.Menu;

/**
 * @author Uri Boness
 */
@Component
public class DIHOptionMenuSource extends AbstractOptionMenuSource {

    private DIHManager dihManager;

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(DIHConfig.class));
    }

    public void update(SolrCore solrCore, Menu menu) {
        Menu dihMenu = new Menu(true);
        dihMenu.addItem("Full Import", new Command() {
            public void execute() {
                doFullDataImport();
            }
        });
        dihMenu.addItem("Status", new Command() {
            public void execute() {
                doDataStatusPoll();
            }
        });
        dihMenu.addItem("Reload Config.", new Command() {
            public void execute() {
                doReloadConfig();
            }
        });

        menu.addItem("Data Import Handler", dihMenu);
    }

    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setDihManager(DIHManager dihManager) {
        this.dihManager = dihManager;
    }

    //================================================ Helper Methods ==================================================

    protected void doFullDataImport() {
        DIHDialog dialog = new DIHDialog(dihManager);
        dialog.fullImport();
    }

    private void doDataStatusPoll() {
        DIHDialog dialog = new DIHDialog(dihManager);
        dialog.pollStatus();
    }

    private void doReloadConfig() {
        DIHDialog dialog = new DIHDialog(dihManager);
        dialog.reloadConfig();
    }
}
