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

package org.apache.solr.explorer.client.plugin.debug.model.context;

import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.model.context.Context;
import org.apache.solr.explorer.client.plugin.debug.model.config.DebugConfig;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;

/**
 * @author Uri Boness
 */
@Component
public class DebugContext implements Context {

    private boolean debugQuery;

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        DebugConfig config = event.getSolrCore().getConfiguration().getConfig(DebugConfig.class);
        if (config == null) {
            setDebugQuery(false);
            return;
        }
        setDebugQuery(config.isDebugQuery());
    }

    public boolean isDebugQuery() {
        return debugQuery;
    }

    public void setDebugQuery(boolean debugQuery) {
        this.debugQuery = debugQuery;
    }
}
