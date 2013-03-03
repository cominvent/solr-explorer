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

package org.apache.solr.explorer.client.plugin.facet.manager;

import org.apache.solr.explorer.client.core.manager.RequestParams;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.plugin.facet.model.config.FacetsConfig;
import org.apache.solr.explorer.client.plugin.facet.model.context.*;
import org.apache.solr.explorer.client.util.LocalParams;
import org.apache.solr.explorer.client.util.collection.Properties;
import org.apache.solr.explorer.client.core.manager.solr.search.urlwriter.AbstractParamsSource;
import org.gwtoolbox.ioc.core.client.annotation.Component;

/**
 * @author Uri Boness
 */
@Component
public class FacetParamsSource extends AbstractParamsSource<FacetContext> {

    public FacetParamsSource() {
        super("Facets");
    }

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(FacetsConfig.class));        
    }

    public Class<FacetContext> getContextClass() {
        return FacetContext.class;
    }

    public void addParams(RequestParams params, FacetContext context, Properties hints) {

        if (!context.hasFacetDefinitions()) {
            params.setParameter("facet", false);
            return;
        }

        params.setParameter("facet", true);

        for (FieldFacetDefinition facetDefinition : context.getFieldFacetDefinitions()) {
            writeFieldFacet(params, facetDefinition);
        }

        for (QueryFacetDefinition facetDefinition : context.getQueryFacetDefinitions()) {
            writeQueryFacet(params, facetDefinition);
        }

        for (DateFacetDefinition facetDefinition : context.getDateFacetDefinitions()) {
            writeDateFacet(params, facetDefinition);
        }
    }

    

    //============================================ Setter/Getter Methods ===============================================

    private void writeFieldFacet(RequestParams params, FieldFacetDefinition facetDefinition) {
        params.addParameter("facet.field", new LocalParams(facetDefinition.getFieldName()).set("key", facetDefinition.getId()));
        String prefix = "f." + facetDefinition.getFieldName();
        if (facetDefinition.hasSort()) {
            params.setParameter(prefix + ".facet.sort", facetDefinition.getSort().name().toLowerCase());
        }
        if (facetDefinition.hasMissing()) {
            params.setParameter(prefix + ".facet.missing", facetDefinition.isMissing());
        }
        if (facetDefinition.hasLimit()) {
            params.setParameter(prefix + ".facet.limit", facetDefinition.getLimit());
        }
        if (facetDefinition.hasMinCount()) {
            params.setParameter(prefix + ".facet.mincount", facetDefinition.getMinCount());
        }
    }

    private void writeDateFacet(RequestParams params, DateFacetDefinition facetDefinition) {
        params.addParameter("facet.date", new LocalParams(facetDefinition.getFieldName()).set("key", facetDefinition.getId()));
        String prefix = "f." + facetDefinition.getFieldName();
        params.setParameter(prefix + ".facet.date.start", facetDefinition.getStart());
        params.setParameter(prefix + ".facet.date.end", facetDefinition.getEnd());
        DateGap gap = facetDefinition.getGap();
        params.setParameter(prefix + ".facet.date.gap", "+" + String.valueOf(gap.getCount()) + gap.getUnit().name());
        params.setParameter(prefix + ".facet.date.hardend", facetDefinition.isHardEnd());
        if (facetDefinition.getOthers() != null && !facetDefinition.getOthers().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (DateOther other : facetDefinition.getOthers()) {
                if (sb.length() != 0) {
                    sb.append(",");
                }
                sb.append(other.getValue());
            }
            params.setParameter(prefix + ".facet.date.other", sb.toString());
        }
    }

    private void writeQueryFacet(RequestParams params, QueryFacetDefinition facetDefinition) {
        params.addParameter("facet.query", new LocalParams(facetDefinition.getQuery()).set("key", facetDefinition.getId()));
    }
}
