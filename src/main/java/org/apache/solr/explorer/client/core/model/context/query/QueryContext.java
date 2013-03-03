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

package org.apache.solr.explorer.client.core.model.context.query;

import com.google.gwt.core.client.GWT;
import org.apache.solr.explorer.client.util.collection.IndexedMap;
import org.apache.solr.explorer.client.util.collection.DefaultIndexedMap;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.model.configuration.SearchConfig;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.core.model.context.Context;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author Uri Boness
 */
@Component
public class QueryContext implements Context {

    public final static String MATCH_ALL_QUERY = "*:*";
    private final static int DEFAULT_PAGE_SIZE = 10;
    private final static int DEFAULT_PAGE_INDEX = 0;

    // free text query
    private String defaultTextQuery = MATCH_ALL_QUERY;
    private String textQuery = MATCH_ALL_QUERY;

    // pagination
    private int pageSize = DEFAULT_PAGE_SIZE;
    private int pageIndex = DEFAULT_PAGE_INDEX;

    // filters
    private IndexedMap<String, FieldValueFilter> fieldValueFiltersById = new DefaultIndexedMap<String, FieldValueFilter>();
    private IndexedMap<String, QueryFilter> queryFiltersById = new DefaultIndexedMap<String, QueryFilter>();

    private Sort sort;

    private List<String> fields;

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        SolrCoreConfiguration config = event.getSolrCore().getConfiguration();
        SearchConfig searchConfig = config.getConfig(SearchConfig.class);
        
        fieldValueFiltersById.clear();
        queryFiltersById.clear();
        setPageSize(searchConfig.getPageSize());
        setDefaultTextQuery(searchConfig.getDefaultQuery());
    }

    /**
     * Returns the current free text query.
     *
     * @return The current free text query
     */
    public String getTextQuery() {
        return (textQuery != null && textQuery.length() > 0) ? textQuery : defaultTextQuery;
    }

    /**
     * Sets a new free text query.
     *
     * @param textQuery The new free text query.
     */
    public void setTextQuery(String textQuery) {
        this.textQuery = textQuery;
    }

    /**
     * Returns the default search query that will be executed when the actual query is empty.
     *
     * @return The default search query that will be executed when the actual query is empty.
     */
    public String getDefaultTextQuery() {
        return defaultTextQuery;
    }

    /**
     * Sets the default search query that will be executed when the actual query is empty.
     *
     * @param defaultTextQuery The default search query that will be executed when the actual query is empty.
     */
    public void setDefaultTextQuery(String defaultTextQuery) {
        this.defaultTextQuery = defaultTextQuery;
    }



    /**
     * Returns the size of the search hits page.
     *
     * @return The size of the search hits page.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the size of the search hits page.
     *
     * @param pageSize The size of the search hits page.
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Returns the search hits page index (zero-based).
     *
     * @return The search hits page index (zero-based).
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * Sets the search hits page index (zero-based).
     *
     * @param pageIndex The search hits page index (zero-based).
     */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void addFieldValueFilter(FieldValueFilter fieldValueFilter) {
        fieldValueFiltersById.put(fieldValueFilter.getId(), fieldValueFilter);
        pageIndex = 0;
    }

    public void removeFieldValueFilter(FieldValueFilter fieldValueFilter) {
        removeFieldValueFilter(fieldValueFilter.getId());
        pageIndex = 0;
    }

    public void removeFieldValueFilter(String id) {
        fieldValueFiltersById.remove(id);
    }

    public Collection<FieldValueFilter> getFieldValueFilters() {
        return fieldValueFiltersById.values();
    }

    //todo: optimize.
    public boolean containsFieldFilter(String fieldName) {
        for (FieldValueFilter filter : fieldValueFiltersById.values()) {
            if (filter.getFieldName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    public void addQueryFilter(QueryFilter queryFilter) {
        queryFiltersById.put(queryFilter.getId(), queryFilter);
        pageIndex = 0;
    }

    public void removeQueryFilter(QueryFilter queryFilter) {
        removeQueryFilter(queryFilter.getId());
    }

    public void removeQueryFilter(String filterId) {
        queryFiltersById.remove(filterId);
        pageIndex = 0;
    }

    public Collection<QueryFilter> getQueryFilters() {
        return queryFiltersById.values();
    }

    public boolean containsQueryFilter(String filterId) {
        return queryFiltersById.containsKey(filterId);
    }

    public boolean hasSort() {
        return sort != null;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public void clearSort() {
        setSort(null);
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
