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

package org.apache.solr.explorer.client.plugin.facet.model.context;

import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.model.context.Context;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Uri Boness
 */
@Component
public class FacetContext implements Context {

    // facets
    private Map<String, FieldFacetDefinition> fieldFacetDefinitionById = new HashMap<String, FieldFacetDefinition>();
    private Map<String, QueryFacetDefinition> queryFacetDefinitionById = new HashMap<String, QueryFacetDefinition>();
    private Map<String, DateFacetDefinition> dateFacetDefinitionById = new HashMap<String, DateFacetDefinition>();

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        fieldFacetDefinitionById.clear();
        queryFacetDefinitionById.clear();
        dateFacetDefinitionById.clear();
    }

    public boolean hasFacetDefinitions() {
        return !fieldFacetDefinitionById.isEmpty() || !queryFacetDefinitionById.isEmpty() || !dateFacetDefinitionById.isEmpty();
    }


    //================================================ Field Facets ====================================================

    public void addFieldFacetDefintiion(FieldFacetDefinition fieldFacetDefinition) {
        fieldFacetDefinitionById.put(fieldFacetDefinition.getId(), fieldFacetDefinition);
    }

    public void removeFieldFacetDefintiion(FieldFacetDefinition fieldFacetDefinition) {
        removeFieldFacetDefinition(fieldFacetDefinition.getId());
    }

    public void removeFieldFacetDefinition(String facetId) {
        fieldFacetDefinitionById.remove(facetId);
    }

    public FieldFacetDefinition getFieldFacetDefinition(String facetId) {
        return fieldFacetDefinitionById.get(facetId);
    }

    public Collection<FieldFacetDefinition> getFieldFacetDefinitions() {
        return fieldFacetDefinitionById.values();
    }

    public void addQueryFacetDefintiion(QueryFacetDefinition queryFacetDefinition) {
        queryFacetDefinitionById.put(queryFacetDefinition.getId(), queryFacetDefinition);
    }


    //================================================ Query Facets ====================================================

    public void removeQueryFacetDefintiion(QueryFacetDefinition queryFacetDefinition) {
        removeQueryFacetDefinition(queryFacetDefinition.getId());
    }

    public void removeQueryFacetDefinition(String facetId) {
        queryFacetDefinitionById.remove(facetId);
    }

    public QueryFacetDefinition getQueryFacetDefinition(String facetId) {
        return queryFacetDefinitionById.get(facetId);
    }

    public Collection<QueryFacetDefinition> getQueryFacetDefinitions() {
        return queryFacetDefinitionById.values();
    }


    //================================================= Date Facets ====================================================

     public void addDateFacetDefintion(DateFacetDefinition dateFacetDefinition) {
        dateFacetDefinitionById.put(dateFacetDefinition.getId(), dateFacetDefinition);
    }

    public void removeDateFacetDefintiion(DateFacetDefinition dateFacetDefinition) {
        removeDateFacetDefinition(dateFacetDefinition.getId());
    }

    public void removeDateFacetDefinition(String facetId) {
        dateFacetDefinitionById.remove(facetId);
    }

    public DateFacetDefinition getDateFacetDefinition(String facetId) {
        return dateFacetDefinitionById.get(facetId);
    }

    public Collection<DateFacetDefinition> getDateFacetDefinitions() {
        return dateFacetDefinitionById.values();
    }
}
