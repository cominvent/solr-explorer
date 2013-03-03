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

package org.apache.solr.explorer.client.plugin.facet.ui.query;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.ui.consolepane.searchcontext.AbstractSearchContextComponent;
import org.apache.solr.explorer.client.plugin.facet.model.config.FacetsConfig;
import org.apache.solr.explorer.client.plugin.facet.model.context.FacetContext;
import org.apache.solr.explorer.client.plugin.facet.model.context.QueryFacetDefinition;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Order;
import org.gwtoolbox.widget.client.table.basic.BasicTable;

/**
 * @author Uri Boness
 */
@Component
@Order(11)
public class QueryFacetSearchContextComponent extends AbstractSearchContextComponent {

    private BasicTable table;

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(FacetsConfig.class));
    }

    public QueryFacetSearchContextComponent() {
        super("Query Facets");

        table = new BasicTable();
        table.setHeaderText(0, "Name");
        table.setHeaderText(1, "Query");

        table.getColumnFormatter().setWidth(0, "100px");
        table.getColumnFormatter().setWidth(1, "250px");

        main.setContent(table);
    }

    public void update(SearchContext context) {
        FacetContext facetContext = context.getContext(FacetContext.class);
        table.removeAllRows();
        int row = 0;
        for (QueryFacetDefinition definition : facetContext.getQueryFacetDefinitions()) {
            table.setText(row, 0, definition.getName());
            table.setText(row, 1, definition.getQuery());
            row++;
        }
        if (row == 0) {
            table.setText(0, 0, "No Query Facets");
            table.getCellFormatter().setColSpan(0, 0, 5);
            table.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
        }
    }
}