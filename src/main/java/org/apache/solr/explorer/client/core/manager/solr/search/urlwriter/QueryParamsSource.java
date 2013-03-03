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

package org.apache.solr.explorer.client.core.manager.solr.search.urlwriter;

import org.apache.solr.explorer.client.core.manager.RequestParams;
import org.apache.solr.explorer.client.core.model.context.query.FieldValueFilter;
import org.apache.solr.explorer.client.core.model.context.query.QueryContext;
import org.apache.solr.explorer.client.core.model.context.query.QueryFilter;
import org.apache.solr.explorer.client.core.model.context.query.Sort;
import org.apache.solr.explorer.client.core.model.configuration.SearchConfig;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.util.LocalParams;
import org.apache.solr.explorer.client.util.collection.Properties;
import org.gwtoolbox.commons.util.client.StringUtils;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Uri Boness
 */
@Order(0)
@Component
public class QueryParamsSource extends AbstractParamsSource<QueryContext> {

    private SearchConfig searchConfig;

    public QueryParamsSource() {
        super("Query");
    }

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        searchConfig = event.getSolrCore().getConfiguration().getConfig(SearchConfig.class);
    }

    public Class<QueryContext> getContextClass() {
        return QueryContext.class;
    }

    public void addParams(RequestParams params, QueryContext context, Properties properties) {
        Hints hints = new Hints(properties);

        int pageSize = context.getPageSize();
        int overrideRows = hints.getOverrideRows();
        if (overrideRows >= 0) {
            pageSize = overrideRows;
        }
        if (pageSize > 0) {
            params.setParameter("rows", pageSize);
        }
        
        int pageIndex = context.getPageIndex();
        params.setParameter("start", (pageSize > 0) ? pageIndex * pageSize : 0);

        String textQuery = context.getTextQuery();
        if (!searchConfig.isUseQaltForEmptyQueries() || !QueryContext.MATCH_ALL_QUERY.equals(textQuery)) {
            params.setParameter("q", textQuery);
        } else {
            params.setParameter("q.alt", textQuery);
        }

        Sort sort = context.getSort();
        if (sort != null) {
            String direction = (sort.getDirection() == Sort.Direction.ASC) ? "asc" : "desc";
            params.setParameter("sort", sort.getField() + " " + direction);
        }

        List<String> fields = context.getFields();
        if (fields != null && !fields.isEmpty()) {
            params.setParameter("fl", StringUtils.collectionToDelimetedString(fields, ","));
        }

        List<String> filters = buildFilters(context);
        for (String filterQuery : filters) {
            params.addParameter("fq", filterQuery);
        }
    }


    //================================================ Helper Methods ==================================================

    private List<String> buildFilters(QueryContext context) {
        List<String> filters = new ArrayList<String>();
        for (FieldValueFilter filter : context.getFieldValueFilters()) {
            if (filter.isRaw()) {
                filters.add(new LocalParams(filter.getValue()).setType("raw").set("f", filter.getFieldName()).getValue());
            } else {
                filters.add(filter.getFieldName() + ":" + filter.getValue());
            }
        }
        for (QueryFilter filter : context.getQueryFilters()) {
            filters.add(filter.getQuery());
        }
        return filters;
    }


    //================================================= Inner Classes ==================================================

    public static class Hints extends Properties {

        public Hints() {
        }

        public Hints(Properties properties) {
            super(properties);
        }

        public Hints setOverrideRows(int rows) {
            put("overrideRows", rows);
            return this;
        }

        public int getOverrideRows() {
            return getInt("overrideRows", -1);
        }
    }
}
