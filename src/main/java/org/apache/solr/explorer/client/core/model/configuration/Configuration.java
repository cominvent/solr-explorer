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

package org.apache.solr.explorer.client.core.model.configuration;

import org.apache.solr.explorer.client.util.collection.IndexedMap;
import org.apache.solr.explorer.client.util.collection.DefaultIndexedMap;

import java.util.Collection;

/**
 * @author Uri Boness
 */
public class Configuration {

    private String helpUrl;

    private IndexedMap<String, SolrCoreConfiguration> coreConfigByName = new DefaultIndexedMap<String, SolrCoreConfiguration>();

    public void addSolrCoreConfiguration(SolrCoreConfiguration solrCoreConfiguration) {
        coreConfigByName.put(solrCoreConfiguration.getCoreName(), solrCoreConfiguration);
    }

    public SolrCoreConfiguration getCoreConfiguration(String coreName) {
        return coreConfigByName.get(coreName);
    }

    public Collection<String> getCoreNames() {
        return coreConfigByName.keySet();
    }

    public String getHelpUrl() {
        return helpUrl;
    }

    public void setHelpUrl(String helpUrl) {
        this.helpUrl = helpUrl;
    }

    public IndexedMap<String, SolrCoreConfiguration> getCoreConfigByName() {
        return coreConfigByName;
    }

    public void setCoreConfigByName(IndexedMap<String, SolrCoreConfiguration> coreConfigByName) {
        this.coreConfigByName = coreConfigByName;
    }
}
