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

package org.apache.solr.explorer.client.core.plugin;

import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.ioc.core.client.annotation.InjectionType;

import java.util.List;

/**
 * @author Uri Boness
 */
@Component
public class PluginManager {

    private List<Plugin> plugins;

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        for (Plugin plugin : plugins) {
            plugin.init(event.getSolrCore());
        }
    }

    //============================================ Setter/Getter Methods ===============================================

    @Inject(by = InjectionType.TYPE)
    public void setPlugins(List<Plugin> plugins) {
        this.plugins = plugins;
    }
}
