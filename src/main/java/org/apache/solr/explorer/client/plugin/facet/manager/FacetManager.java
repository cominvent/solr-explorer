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

package org.apache.solr.explorer.client.plugin.facet.manager;

import org.apache.solr.explorer.client.plugin.facet.model.context.DateFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.result.DateFacet;
import org.apache.solr.explorer.client.plugin.facet.model.result.QueryFacet;
import org.apache.solr.explorer.client.plugin.facet.model.result.FieldFacet;
import org.apache.solr.explorer.client.plugin.facet.model.context.QueryFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.context.FieldFacetDefinition;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Uri Boness
 */
public interface FacetManager {

    void searchQueryFacet(QueryFacetDefinition facetDefinition, AsyncCallback<QueryFacet> callback);

    void searchQueryFacet(QueryFacetDefinition facetDefinition, SearchContext context, AsyncCallback<QueryFacet> callback);

    void searchFieldFacet(FieldFacetDefinition facetDefinition, AsyncCallback<FieldFacet> callback);

    void searchFieldFacet(FieldFacetDefinition facetDefinition, SearchContext context, AsyncCallback<FieldFacet> callback);

    void searchDateFacet(DateFacetDefinition facetDefinition, AsyncCallback<DateFacet> callback);

    void searchDateFacet(DateFacetDefinition facetDefinition, SearchContext context, AsyncCallback<DateFacet> callback);

}
