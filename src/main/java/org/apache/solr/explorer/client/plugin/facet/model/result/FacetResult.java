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

package org.apache.solr.explorer.client.plugin.facet.model.result;

import org.apache.solr.explorer.client.util.collection.IndexedMap;
import org.apache.solr.explorer.client.util.collection.DefaultIndexedMap;

import java.util.Collection;

/**
 * @author Uri Boness
 */
public class FacetResult {

    private IndexedMap<String, FieldFacet> fieldFacets = new DefaultIndexedMap<String, FieldFacet>();
    private IndexedMap<String, DateFacet> dateFacets = new DefaultIndexedMap<String, DateFacet>();
    private IndexedMap<String, QueryFacet> queryFacets = new DefaultIndexedMap<String, QueryFacet>();

    /**
     * Returns all available fields facets facets.
     *
     * @return All available fields facets facets.
     */
    public Collection<FieldFacet> getFieldFacets() {
        return fieldFacets.values();
    }

    /**
     * Returns the field facet with the given name.
     *
     * @param facetName The name of the field facet.
     * @return The field facet with the given name.
     */
    public FieldFacet getFieldFacet(String facetName) {
        return fieldFacets.get(facetName);
    }

    /**
     * Adds the given field facet to this result.
     *
     * @param facet Adds the given field facet to this search result.
     */
    public void addFieldFacet(FieldFacet facet) {
        fieldFacets.put(facet.getName(), facet);
    }

    public Collection<QueryFacet> getQueryFacets() {
        return queryFacets.values();
    }

    public QueryFacet getQueryFacet(String facetName) {
        return queryFacets.get(facetName);
    }

    public void addQueryFacet(QueryFacet queryFacet) {
        queryFacets.put(queryFacet.getName(), queryFacet);
    }

    public Collection<DateFacet> getDateFacets() {
        return dateFacets.values();
    }

    public DateFacet getDateFacet(String facetName) {
        return dateFacets.get(facetName);
    }

    public void addDateFacet(DateFacet facet) {
        dateFacets.put(facet.getName(), facet);
    }

}
