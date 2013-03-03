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

package org.apache.solr.explorer.client.core.model.context;

import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.ioc.core.client.annotation.InjectionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Uri Boness
 */
@Component
public class SearchContext {

    private SolrCore solrCore;

    private Map<Class, Object> contextByClass = new HashMap<Class, Object>();

    /**
     * Default empty constructor
     */
    public SearchContext() {
    }

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        this.solrCore = event.getSolrCore();
    }

    //============================================== Setter/Getter =====================================================

    public SolrCore getSolrCore() {
        return solrCore;
    }

    @Inject(by = InjectionType.TYPE)
    public void setContexts(List<Context> contexts) {
        for (Context context : contexts) {
            contextByClass.put(context.getClass(), context);
        }
    }

    public <T extends Context> T getContext(Class<T> contextClass) {
        return (T) contextByClass.get(contextClass);
    }

}
