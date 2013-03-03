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

import org.apache.solr.explorer.client.core.manager.RequestParams;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.search.urlwriter.AbstractParamsSource;
import org.apache.solr.explorer.client.plugin.spellcheck.model.config.SpellCheckConfig;
import org.apache.solr.explorer.client.plugin.spellcheck.model.context.SpellCheckingContext;
import org.apache.solr.explorer.client.util.collection.Properties;
import org.gwtoolbox.ioc.core.client.annotation.Component;

/**
 * @author Uri Boness
 */
@Component
public class SpellCheckingParamsSource extends AbstractParamsSource<SpellCheckingContext> {

    public SpellCheckingParamsSource() {
        super("Spellcheck");
    }

    public Class<SpellCheckingContext> getContextClass() {
        return SpellCheckingContext.class;
    }

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(SpellCheckConfig.class));
    }

    public void addParams(RequestParams params, SpellCheckingContext context, Properties hints) {
        if (!context.isEnabled()) {
            return;
        }
        params.addParameter("spellcheck", true);
        params.addParameter("spellcheck.count", context.getCount());
        params.addParameter("spellcheck.collate", context.isCollate());
        params.addParameter("spellcheck.dictionary", context.getDictionary());
        params.addParameter("spellcheck.onlyMorePopular", context.isOnlyMorePopular());
//            addParameter("spellcheck.extendedResults", true);
    }
}
