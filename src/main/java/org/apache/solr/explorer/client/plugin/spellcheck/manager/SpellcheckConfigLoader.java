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

package org.apache.solr.explorer.client.plugin.spellcheck.manager;

import org.apache.solr.explorer.client.plugin.spellcheck.model.config.SpellCheckConfig;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.core.model.configuration.ServerConfig;
import org.apache.solr.explorer.client.core.manager.configuration.loader.AbstractConfigLoader;
import org.gwtoolbox.commons.xml.client.DOMUtils;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.ConfiguredProperty;
import static org.gwtoolbox.commons.xml.client.DOMUtils.*;
import static org.gwtoolbox.commons.xml.client.DOMUtils.getSingleChildBooleanValue;
import com.google.gwt.xml.client.Element;

/**
 * @author Uri Boness
 */
@Component
public class SpellcheckConfigLoader extends AbstractConfigLoader {

    private int defaultTimeout;
    private String defaultDictionary;
    private int defaultCount;
    private boolean defaultCollate;
    private boolean defaultOnlyMorePopular;

    public boolean load(Element coreElement, SolrCoreConfiguration configuration) {
        Element element = getSingleChild(coreElement, "spellcheck");
        if (element == null) {
            return false;
        }

        SpellCheckConfig spellCheckConfig = new SpellCheckConfig();
        spellCheckConfig.setUri(element.getAttribute("commandUri"));
        int timeout = DOMUtils.getIntegerAttribute(element, "commandTimeout", defaultTimeout);
        spellCheckConfig.setTimeout(timeout);

        boolean enabled = (element == null) ? false : getBooleanAttribute(element, "enabled", false);
        spellCheckConfig.setEnabled(enabled);
        String dictionary = (element == null) ? defaultDictionary : getSingleChildValue(element, "dictionary", defaultDictionary);
        spellCheckConfig.setDictionary(dictionary);
        int count = (element == null) ? defaultCount : getSingleChildIntegerValue(element, "count", defaultCount);
        spellCheckConfig.setCount(count);
        boolean collate = (element == null) ? defaultCollate : getSingleChildBooleanValue(element, "collate", defaultCollate);
        spellCheckConfig.setCollate(collate);
        boolean onlyMorePopular = (element == null) ? defaultOnlyMorePopular : getSingleChildBooleanValue(element, "onlyMorePopular", defaultOnlyMorePopular);
        spellCheckConfig.setOnlyMorePopular(onlyMorePopular);

        configuration.setConfig(SpellCheckConfig.class, spellCheckConfig);

        return true;
    }

    @Override
    public void postProcess(SolrCoreConfiguration configuration) {
        SpellCheckConfig spellCheckConfig = configuration.getConfig(SpellCheckConfig.class);
        if (spellCheckConfig == null) {
            return;
        }
        ServerConfig serverConfig = configuration.getConfig(ServerConfig.class);
        if (spellCheckConfig.getUri() != null) {
            String baseUrl = serverConfig.getBaseUrl();
            spellCheckConfig.setUrl(baseUrl + spellCheckConfig.getUri());
        } else {
            spellCheckConfig.setUrl(serverConfig.getSearchUrl());
        }
    }

    //============================================ Setter/Getter Methods ===============================================

    @ConfiguredProperty("spellcheck.default.timeout")
    public void setDefaultTimeout(int defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    @ConfiguredProperty("spellcheck.default.dictionary")
    public void setDefaultDictionary(String defaultDictionary) {
        this.defaultDictionary = defaultDictionary;
    }

    @ConfiguredProperty("spellcheck.default.count")
    public void setDefaultCount(int defaultCount) {
        this.defaultCount = defaultCount;
    }

    @ConfiguredProperty("spellcheck.default.collate")
    public void setDefaultCollate(boolean defaultCollate) {
        this.defaultCollate = defaultCollate;
    }

    @ConfiguredProperty("spellcheck.default.onlyMorePopular")
    public void setDefaultOnlyMorePopular(boolean defaultOnlyMorePopular) {
        this.defaultOnlyMorePopular = defaultOnlyMorePopular;
    }
}
