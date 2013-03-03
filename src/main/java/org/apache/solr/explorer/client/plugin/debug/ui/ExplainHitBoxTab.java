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

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.model.result.Hit;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.core.plugin.PluginBase;
import org.apache.solr.explorer.client.plugin.debug.model.config.DebugConfig;
import org.apache.solr.explorer.client.plugin.debug.model.context.DebugContext;
import org.apache.solr.explorer.client.plugin.debug.model.result.DebugResult;
import org.apache.solr.explorer.client.plugin.debug.model.result.HitExplain;
import org.apache.solr.explorer.client.plugin.listview.ui.HitBoxTab;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Inject;

/**
 * @author Uri Boness
 */
@Component
public class ExplainHitBoxTab extends PluginBase implements HitBoxTab {

    private DebugContext debugContext;
    private String uniqueKeyField;

    public void init(SolrCore solrCore) {
        boolean active = solrCore.getConfiguration().isActive(DebugConfig.class);
        setActive(active);
        if (active) {
            uniqueKeyField = solrCore.getSchema().getUniqueKeyField();
        }
    }

    @Override
    public boolean isActive() {
        return super.isActive() && debugContext.isDebugQuery();
    }

    public String getName() {
        return "Explain";
    }

    public boolean isEnabled(SearchResult result, Hit hit) {
        return true;
    }

    public Widget createWidget(SearchResult result, Hit hit) {
        DebugResult debugResult = result.getPart(DebugResult.class);
        String key = (String) hit.get(uniqueKeyField);
        HitExplain explain = debugResult.getHitDebugInfo(key);
        return parseExplain(explain);
    }


    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setDebugContext(DebugContext debugContext) {
        this.debugContext = debugContext;
    }


    //================================================ Helper Methods ==================================================

    private Widget parseExplain(HitExplain explain) {
        String info = explain.getInfo();
        info = info.replace("\n", "<br/>")
                .replace(" ", "&nbsp;&nbsp;&nbsp;");
        return new HTML(info);
    }
}
