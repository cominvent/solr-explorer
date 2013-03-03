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

import org.apache.solr.explorer.client.util.collection.Properties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Uri Boness
 */
public class SolrCoreConfiguration {

    private String coreName;
    private Properties properties;

    private Map<Class, Object> configByClass = new HashMap<Class, Object>();

    public <T> void setConfig(Class<T> clazz, T config) {
        configByClass.put(clazz, config);
    }

    @SuppressWarnings("unchecked")
    public <T> T getConfig(Class configClass) {
        return (T) configByClass.get(configClass);
    }

    public String getCoreName() {
        return coreName;
    }

    public void setCoreName(String coreName) {
        this.coreName = coreName;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public boolean isActive(Class configClass) {
        return configByClass.containsKey(configClass);
    }

}
