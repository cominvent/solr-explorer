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

package org.apache.solr.explorer.client.plugin.spellcheck.model.context;

import org.apache.solr.explorer.client.core.model.context.Context;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.plugin.spellcheck.model.config.SpellCheckConfig;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;

/**
 * @author Uri Boness
 */
@Component
public class SpellCheckingContext implements Context {

    private boolean enabled;
    private int count = 1;
    private boolean onlyMorePopular = true;
    private boolean collate = true;
    private String dictionary = "default";


    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        SolrCoreConfiguration config = event.getSolrCore().getConfiguration();
        SpellCheckConfig specllcheckConfig = config.getConfig(SpellCheckConfig.class);
        if (specllcheckConfig == null) {
            return;
        }
        
        // initializing spellchecking

        setEnabled(specllcheckConfig.isEnabled());
        setDictionary(specllcheckConfig.getDictionary());
        setCount(specllcheckConfig.getCount());
        setCollate(specllcheckConfig.isCollate());
        setOnlyMorePopular(specllcheckConfig.isOnlyMorePopular());
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isOnlyMorePopular() {
        return onlyMorePopular;
    }

    public void setOnlyMorePopular(boolean onlyMorePopular) {
        this.onlyMorePopular = onlyMorePopular;
    }

    public boolean isCollate() {
        return collate;
    }

    public void setCollate(boolean collate) {
        this.collate = collate;
    }

    public String getDictionary() {
        return dictionary;
    }

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }
}
