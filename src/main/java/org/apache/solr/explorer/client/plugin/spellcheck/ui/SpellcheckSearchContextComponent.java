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

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.ui.consolepane.searchcontext.AbstractSearchContextComponent;
import org.apache.solr.explorer.client.plugin.spellcheck.model.config.SpellCheckConfig;
import org.apache.solr.explorer.client.plugin.spellcheck.model.context.SpellCheckingContext;
import org.gwtoolbox.ioc.core.client.annotation.Component;

/**
 * @author Uri Boness
 */
@Component
public class SpellcheckSearchContextComponent extends AbstractSearchContextComponent {

    private Grid grid;

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(SpellCheckConfig.class));
    }

    public SpellcheckSearchContextComponent() {
        super("Spellchecking");

        grid = new Grid(6, 2);
        grid.setHTML(0, 0, "<b>Enabled</b>");
        grid.setHTML(1, 0, "<b>Dictionary</b>");
        grid.setHTML(2, 0, "<b>Count</b>");
        grid.setHTML(3, 0, "<b>Only More Popular</b>");
        grid.setHTML(4, 0, "<b>Collate</b>");

        grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
        grid.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_CENTER);
        grid.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_CENTER);
        grid.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_CENTER);
        grid.getCellFormatter().setHorizontalAlignment(4, 1, HasHorizontalAlignment.ALIGN_CENTER);

        main.setContent(grid);
    }

    public void update(SearchContext context) {
        SpellCheckingContext spellChecking = context.getContext(SpellCheckingContext.class);
        boolean enabled = spellChecking.isEnabled();
        grid.setText(0, 1, String.valueOf(enabled));
        grid.setText(1, 1, enabled ? spellChecking.getDictionary() : "-");
        grid.setText(2, 1, enabled ? String.valueOf(spellChecking.getCount()) : "-");
        grid.setText(3, 1, enabled ? String.valueOf(spellChecking.isOnlyMorePopular()) : "-");
        grid.setText(4, 1, enabled ? String.valueOf(spellChecking.isCollate()) : "-");
    }
}
