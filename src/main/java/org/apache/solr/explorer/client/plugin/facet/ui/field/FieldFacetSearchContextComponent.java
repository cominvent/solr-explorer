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

package org.apache.solr.explorer.client.plugin.facet.ui.field;

import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.ui.consolepane.searchcontext.AbstractSearchContextComponent;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.plugin.facet.model.config.FacetsConfig;
import org.apache.solr.explorer.client.plugin.facet.model.context.FacetContext;
import org.apache.solr.explorer.client.plugin.facet.model.context.FieldFacetDefinition;
import org.gwtoolbox.widget.client.table.basic.BasicTable;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Order;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

/**
 * @author Uri Boness
 */
@Component
@Order(10)
public class FieldFacetSearchContextComponent extends AbstractSearchContextComponent {

    private BasicTable table;

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(FacetsConfig.class));
    }

    public FieldFacetSearchContextComponent() {
        super("Field Facets");

        table = new BasicTable();
        table.setHeaderText(0, "Name");
        table.setHeaderText(1, "Field");
        table.setHeaderText(2, "Sort by Count");
        table.setHeaderText(3, "Limit");
        table.setHeaderText(4, "Min. Count");
        table.setHeaderText(5, "Show Missing");

        table.getColumnFormatter().setWidth(0, "100px");
        table.getColumnFormatter().setWidth(1, "100px");
        table.getColumnFormatter().setWidth(2, "150px");
        table.getColumnFormatter().setWidth(3, "50px");
        table.getColumnFormatter().setWidth(4, "100px");
        table.getColumnFormatter().setWidth(5, "150px");

        main.setContent(table);
    }

    public void update(SearchContext context) {
        FacetContext facetContext = context.getContext(FacetContext.class);
        table.removeAllRows();
        int row = 0;
        for (FieldFacetDefinition definition : facetContext.getFieldFacetDefinitions()) {
            table.setText(row, 0, definition.getName());
            table.setText(row, 1, definition.getFieldName());
            table.setText(row, 2, definition.hasSort() ? String.valueOf(definition.getSort().name()) : UNSPECIFIED);
            table.getCellFormatter().setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_CENTER);
            table.setText(row, 3, definition.hasLimit() ? String.valueOf(definition.getLimit()) : UNSPECIFIED);
            table.getCellFormatter().setHorizontalAlignment(row, 3, HasHorizontalAlignment.ALIGN_CENTER);
            table.setText(row, 4, definition.hasMinCount() ? String.valueOf(definition.getMinCount()) : UNSPECIFIED);
            table.getCellFormatter().setHorizontalAlignment(row, 4, HasHorizontalAlignment.ALIGN_CENTER);
            table.setText(row, 5, definition.hasMissing() ? String.valueOf(definition.isMissing()) : UNSPECIFIED);
            table.getCellFormatter().setHorizontalAlignment(row, 5, HasHorizontalAlignment.ALIGN_CENTER);
            row++;
        }

        if (row == 0) {
            table.setText(0, 0, "No Field Facets");
            table.getCellFormatter().setColSpan(0, 0, 5);
            table.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
        }
    }
}
