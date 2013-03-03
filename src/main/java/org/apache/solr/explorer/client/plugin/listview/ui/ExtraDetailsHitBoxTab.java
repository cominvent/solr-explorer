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

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.model.result.Hit;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.core.plugin.PluginBase;
import org.apache.solr.explorer.client.plugin.listview.model.config.ListViewConfig;
import org.gwtoolbox.commons.util.client.template.MapModel;
import org.gwtoolbox.commons.util.client.template.Template;
import org.gwtoolbox.ioc.core.client.annotation.Component;

/**
 * @author Uri Boness
 */
@Component
public class ExtraDetailsHitBoxTab extends PluginBase implements HitBoxTab {

    private ListViewConfig config;

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(ListViewConfig.class));
        if (isActive()) {
            config = solrCore.getConfiguration().getConfig(ListViewConfig.class);
        }
    }

    public String getName() {
        return config.getDetailsHtmlSnippet().getCaption();
    }

    public boolean isEnabled(SearchResult result, Hit hit) {
        return config.getDetailsHtmlSnippet() != null;
    }

    public Widget createWidget(SearchResult result, Hit hit) {
        Template template = config.getDetailsHtmlSnippet().getTemplate();
        String details = template.render(new MapModel(hit));
        return new HTML(details);
    }
}
