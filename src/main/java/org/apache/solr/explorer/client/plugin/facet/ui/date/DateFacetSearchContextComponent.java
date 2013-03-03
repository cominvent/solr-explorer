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

package org.apache.solr.explorer.client.plugin.facet.ui.date;

import com.google.gwt.user.client.ui.Label;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.ui.consolepane.searchcontext.AbstractSearchContextComponent;
import org.apache.solr.explorer.client.plugin.facet.model.config.FacetsConfig;
import org.apache.solr.explorer.client.plugin.facet.model.context.DateFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.context.DateOther;
import org.apache.solr.explorer.client.plugin.facet.model.context.FacetContext;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Order;
import org.gwtoolbox.widget.client.data.DataTypes;
import org.gwtoolbox.widget.client.data.MapRecord;
import org.gwtoolbox.widget.client.grid.Column;
import org.gwtoolbox.widget.client.grid.Columns;
import org.gwtoolbox.widget.client.grid.DataGrid;

import java.util.Set;

/**
 * @author Uri Boness
 */
@Component
@Order(10)
public class DateFacetSearchContextComponent extends AbstractSearchContextComponent {

    private DataGrid grid;

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(FacetsConfig.class));
    }

    public DateFacetSearchContextComponent() {
        super("Date Facets");

        Columns columns = new Columns(
                new Column("name", "Name", DataTypes.TEXT, 100, Column.Feature.SORTABLE),
                new Column("field", "Field", DataTypes.TEXT, 100, Column.Feature.SORTABLE),
                new Column("start", "Start Date", DataTypes.TEXT, 100),
                new Column("end", "End Date", DataTypes.TEXT, 100),
                new Column("gap", "Gap", DataTypes.TEXT, 100),
                new Column("hardEnd", "Hard End", DataTypes.BOOLEAN, 100, Column.Feature.SORTABLE),
                new Column("other", "Other", DataTypes.TEXT, 150)
        );
        grid = new DataGrid(columns);
        grid.setHeight("100px");
        main.setContent(grid);
    }

    public void update(SearchContext context) {
        FacetContext facetContext = context.getContext(FacetContext.class);
        grid.clearAllRows();
        int row = 0;
        for (DateFacetDefinition definition : facetContext.getDateFacetDefinitions()) {
            grid.addRecord(new MapRecord()
                    .setValue("name", definition.getName())
                    .setValue("field", definition.getFieldName())
                    .setValue("start", definition.getStart())
                    .setValue("end", definition.getEnd())
                    .setValue("gap", String.valueOf(definition.getGap().getCount()) + " " + definition.getGap().getUnit().name())
                    .setBooleanValue("hardEnd", definition.isHardEnd())
                    .setValue("other", getOthersAsString(definition.getOthers()))
            );
            row++;
        }

        if (row == 0) {
            main.setContent(new Label("No date facets"));
        } else {
            main.setContent(grid);
        }
    }


    //================================================ Helper Methods ==================================================

    private static String getOthersAsString(Set<DateOther> others) {
        if (others == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (DateOther other : others) {
            if (builder.length() != 0) {
                builder.append(", ");
            }
            builder.append(other.getValue());
        }
        return builder.toString();
    }
}