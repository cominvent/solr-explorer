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

import org.apache.solr.explorer.client.core.manager.RequestParams;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.search.urlwriter.AbstractParamsSource;
import org.apache.solr.explorer.client.plugin.highlight.model.config.HighlightingConfig;
import org.apache.solr.explorer.client.plugin.highlight.model.context.HighlightingContext;
import org.apache.solr.explorer.client.util.collection.Properties;
import org.gwtoolbox.ioc.core.client.annotation.Component;

/**
 * @author Uri Boness
 */
@Component
public class HighlightingParamsSource extends AbstractParamsSource<HighlightingContext> {

    public HighlightingParamsSource() {
        super("Highlight");
    }

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(HighlightingConfig.class));
    }

    public Class<HighlightingContext> getContextClass() {
        return HighlightingContext.class;
    }

    public void addParams(RequestParams params, HighlightingContext context, Properties hints) {
        if (!context.isEnabled()) {
            params.setParameter("hl", false);
            return;
        }
        params.setParameter("hl", true);
        params.setParameter("hl.fl", context.getFields());
        params.setParameter("hl.simple.pre", context.getWrappingPrefix());
        params.setParameter("hl.simple.post", context.getWrappingSuffix());
        params.setParameter("hl.highlightMultiTerm", context.isHighlightMultiTerm());
    }
}