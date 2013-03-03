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

package org.apache.solr.explorer.client.plugin.terms.ui;

import com.google.gwt.user.client.Command;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.ui.searchpane.AbstractOptionMenuSource;
import org.apache.solr.explorer.client.plugin.terms.model.config.TermsConfig;
import org.apache.solr.explorer.client.plugin.terms.model.context.TermsContext;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.widget.client.menu.Menu;

/**
 * @author Uri Boness
 */
@Component
public class TermsOptionMenuSource extends AbstractOptionMenuSource {

    private SearchContext searchContext;

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(TermsConfig.class));
    }

    public void update(SolrCore solrCore, Menu menu) {
        menu.addItem("Terms...", new Command() {
            public void execute() {
                doEditTerms();
            }
        });
    }


    //================================================ Helper Methods ==================================================

    protected void doEditTerms() {
        TermsContext termsContext = searchContext.getContext(TermsContext.class);
        TermsFormDialog dialog = new TermsFormDialog(termsContext);
        dialog.center();
        dialog.show();
    }


    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setSearchContext(SearchContext searchContext) {
        this.searchContext = searchContext;
    }
}