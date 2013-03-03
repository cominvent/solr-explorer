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

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.context.query.QueryContext;
import org.apache.solr.explorer.client.core.model.context.query.FieldValueFilter;
import org.apache.solr.explorer.client.core.model.context.query.QueryFilter;
import org.apache.solr.explorer.client.util.ui.widget.UnorderedList;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Order;

/**
 * @author Uri Boness
 */
@Component
@Order(-9000)
public class FiltersSearchContextComponent extends AbstractSearchContextComponent {

    private FlowPanel content;

    public FiltersSearchContextComponent() {
        super("Filters");
        content = new FlowPanel();
        main.setContent(content);
    }

    public void update(SearchContext context) {
        QueryContext queryContext = context.getContext(QueryContext.class);
        content.clear();
        if (queryContext.getFieldValueFilters().isEmpty() && queryContext.getQueryFilters().isEmpty()) {
            content.add(new Label("No Filters"));
            return;
        }
        UnorderedList list = new UnorderedList();
        for (FieldValueFilter filter : queryContext.getFieldValueFilters()) {
            list.addListItem(filter.getFieldName() + ":" + filter.getValue());
        }
        for (QueryFilter filter : queryContext.getQueryFilters()) {
            list.addListItem(filter.getQuery());
        }
        content.add(list);
    }
}
