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

package org.apache.solr.explorer.client.plugin.debug.manager;

import org.apache.solr.explorer.client.core.manager.RequestParams;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.search.urlwriter.AbstractParamsSource;
import org.apache.solr.explorer.client.util.collection.Properties;
import org.apache.solr.explorer.client.plugin.debug.model.config.DebugConfig;
import org.apache.solr.explorer.client.plugin.debug.model.context.DebugContext;
import org.gwtoolbox.ioc.core.client.annotation.Component;

/**
 * @author Uri Boness
 */
@Component
public class DebugParamsSource extends AbstractParamsSource<DebugContext> {

    public DebugParamsSource() {
        super("Debug");
    }

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(DebugConfig.class));
    }

    public Class<DebugContext> getContextClass() {
        return DebugContext.class;
    }

    public void addParams(RequestParams params, DebugContext context, Properties hints) {
        params.addParameter("debugQuery", context.isDebugQuery());
    }
}
