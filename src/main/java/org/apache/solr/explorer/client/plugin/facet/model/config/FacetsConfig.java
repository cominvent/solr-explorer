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

package org.apache.solr.explorer.client.plugin.facet.model.config;

import org.apache.solr.explorer.client.plugin.facet.model.context.DateFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.context.FieldFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.context.QueryFacetDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Uri Boness
 */
public class FacetsConfig {

    private List<FieldFacetDefinition> fieldFacetDefinitions = new ArrayList<FieldFacetDefinition>();
    private List<DateFacetDefinition> dateFacetDefinitions = new ArrayList<DateFacetDefinition>();
    private List<QueryFacetDefinition> queryFacetDefinitions = new ArrayList<QueryFacetDefinition>();

    public void addFieldFacetDefinition(FieldFacetDefinition definition) {
        fieldFacetDefinitions.add(definition);
    }

    public List<FieldFacetDefinition> getFieldFacetDefinitions() {
        return fieldFacetDefinitions;
    }

    public void addQueryFacetDefinition(QueryFacetDefinition definition) {
        queryFacetDefinitions.add(definition);
    }

    public List<QueryFacetDefinition> getQueryFacetDefinitions() {
        return queryFacetDefinitions;
    }

    public void addDateFacetDefinition(DateFacetDefinition definition) {
        dateFacetDefinitions.add(definition);
    }

    public List<DateFacetDefinition> getDateFacetDefinitions() {
        return dateFacetDefinitions;
    }
}