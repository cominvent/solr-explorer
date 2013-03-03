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

package org.apache.solr.explorer.client.plugin.highlight.manager;

import com.google.gwt.xml.client.Element;
import org.apache.solr.explorer.client.core.manager.configuration.loader.AbstractConfigLoader;
import org.apache.solr.explorer.client.core.manager.logging.Logger;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.plugin.highlight.model.config.HighlightingConfig;
import org.apache.solr.explorer.client.plugin.listview.model.config.ListViewConfig;

import static org.gwtoolbox.commons.xml.client.DOMUtils.*;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.ConfiguredProperty;
import org.gwtoolbox.ioc.core.client.annotation.Inject;

/**
 * @author Uri Boness
 */
@Component
public class HighlightingConfigLoader extends AbstractConfigLoader {

    private String defaultPrefix;
    private String defaultSuffix;

    private Logger logger;

    public boolean load(Element coreElement, SolrCoreConfiguration configuration) {
        if (logger.isInfoEnabled()) {
            logger.info("Looking for highlighing configuration...");
        }

        Element element = getSingleChild(coreElement, "highlighting");
        if (element == null) {
            return false;
        }

        if (logger.isInfoEnabled()) {
            logger.info("Found highlighting configuration - Loading...");
        }

        HighlightingConfig config = new HighlightingConfig();
        boolean enabled = (element == null) ? false : getBooleanAttribute(element, "enabled", true);
        config.setEnabled(enabled);

        config.setFields(getSingleChildValue(element, "fields", null));

        config.setHighlightMultiTerm(getSingleChildBooleanValue(element, "highlightMultiTerm", false));

        String prefix = defaultPrefix;
        String suffix = defaultSuffix;
        Element highlightElement = getSingleChild(element, "highlight");
        prefix = (highlightElement == null) ? defaultPrefix : getSingleChildValue(highlightElement, "prefix");
        suffix= (highlightElement == null) ? defaultSuffix : getSingleChildValue(highlightElement, "suffix");
        config.setPrefix(prefix);
        config.setSuffix(suffix);

        configuration.setConfig(HighlightingConfig.class, config);

        return true;
    }

    @Override
    public void postProcess(SolrCoreConfiguration configuration) {
        HighlightingConfig config = configuration.getConfig(HighlightingConfig.class);
        if (config == null || (config.getFields() != null && !config.getFields().isEmpty())) {
            return;
        }
        ListViewConfig renderingConfig = configuration.getConfig(ListViewConfig.class);
        if (renderingConfig != null) {
            String fields = renderingConfig.getTitleFieldName() + "," + renderingConfig.getSummaryFieldName();
            config.setFields(fields);
        }
    }

    //============================================ Setter/Getter Methods ===============================================

    @ConfiguredProperty("hightlight.defalut.prefix")
    public void setDefaultPrefix(String defaultPrefix) {
        this.defaultPrefix = defaultPrefix;
    }

    @ConfiguredProperty("hightlight.defalut.suffix")
    public void setDefaultSuffix(String defaultSuffix) {
        this.defaultSuffix = defaultSuffix;
    }

    @Inject
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
