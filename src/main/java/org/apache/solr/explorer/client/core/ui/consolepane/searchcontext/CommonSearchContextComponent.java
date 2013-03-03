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

package org.apache.solr.explorer.client.core.ui.consolepane.searchcontext;

import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Order;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.context.query.QueryContext;
import org.apache.solr.explorer.client.core.model.context.query.Sort;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

/**
 * @author Uri Boness
 */
@Component
@Order(-10000)
public class CommonSearchContextComponent extends AbstractSearchContextComponent {

    private Grid grid;

    public CommonSearchContextComponent() {
        super("Common");

        grid = new Grid(4, 2);
        grid.setHTML(0, 0, "<b>Text Query</b>");
        grid.setHTML(1, 0, "<b>Page Index</b>");
        grid.setHTML(2, 0, "<b>Page Size</b>");
        grid.setHTML(3, 0, "<b>Sort</b>");

        grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
        grid.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_CENTER);
        grid.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_CENTER);
        grid.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_CENTER);

        main.setContent(grid);
    }

    public void update(SearchContext context) {
        QueryContext queryContext = context.getContext(QueryContext.class);
        grid.setText(0, 1, queryContext.getTextQuery());
        grid.setText(1, 1, String.valueOf(queryContext.getPageIndex()));
        grid.setText(2, 1, String.valueOf(queryContext.getPageSize()));
        Sort sort = queryContext.getSort();
        if (sort == null) {
            grid.setText(3, 1, UNSPECIFIED);
        } else {
            grid.setText(3, 1, sort.getField() + " (" + sort.getDirection().name() + ")");
        }
    }
}
