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

package org.apache.solr.explorer.client.plugin.highlight.model.context;

import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.plugin.highlight.model.config.HighlightingConfig;
import org.apache.solr.explorer.client.core.model.context.Context;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;

/**
 * @author Uri Boness
 */
@Component
public class HighlightingContext implements Context {

    public static final String DEFAULT_PREFIX = "<em>";
    public static final String DEFAULT_SUFFIX = "</em>";

    private boolean enabled;

    private String fields;

    private String wrappingPrefix = DEFAULT_PREFIX;
    private String wrappingSuffix = DEFAULT_SUFFIX;

    private boolean highlightMultiTerm;

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        SolrCoreConfiguration config = event.getSolrCore().getConfiguration();
        HighlightingConfig highlightingConfig = config.getConfig(HighlightingConfig.class);
        if (highlightingConfig == null) {
            setEnabled(false);
            return;
        }

        // initializing the highlighting
        setEnabled(highlightingConfig.isEnabled());
        setFields(highlightingConfig.getFields());
        setWrappingPrefix(highlightingConfig.getPrefix());
        setWrappingSuffix(highlightingConfig.getSuffix());
        setHighlightMultiTerm(highlightingConfig.isHighlightMultiTerm());
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getWrappingPrefix() {
        return wrappingPrefix;
    }

    public void setWrappingPrefix(String wrappingPrefix) {
        this.wrappingPrefix = wrappingPrefix;
    }

    public String getWrappingSuffix() {
        return wrappingSuffix;
    }

    public void setWrappingSuffix(String wrappingSuffix) {
        this.wrappingSuffix = wrappingSuffix;
    }

    public void setHighlightMultiTerm(boolean highlightMultiTerm) {
        this.highlightMultiTerm = highlightMultiTerm;
    }

    public boolean isHighlightMultiTerm() {
        return highlightMultiTerm;
    }
}
