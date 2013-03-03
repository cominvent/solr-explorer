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

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.RequestManager;
import org.apache.solr.explorer.client.core.manager.RequestParams;
import org.apache.solr.explorer.client.core.manager.XmlResponse;
import org.apache.solr.explorer.client.core.manager.logging.Logger;
import org.apache.solr.explorer.client.core.manager.solr.search.urlwriter.CommonParamsSource;
import org.apache.solr.explorer.client.core.manager.solr.search.urlwriter.QueryParamsSource;
import org.apache.solr.explorer.client.core.model.configuration.ServerConfig;
import org.apache.solr.explorer.client.core.model.context.CommonContext;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.context.query.QueryContext;
import org.apache.solr.explorer.client.plugin.facet.model.context.DateFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.context.FacetContext;
import org.apache.solr.explorer.client.plugin.facet.model.context.FieldFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.context.QueryFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.result.DateFacet;
import org.apache.solr.explorer.client.plugin.facet.model.result.FieldFacet;
import org.apache.solr.explorer.client.plugin.facet.model.result.QueryFacet;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Inject;

/**
 * @author Uri Boness
 */
@Component(name = "facetManager")
public class DefaultFacetManager implements FacetManager {

    private SearchContext searchContext;
    private Logger logger;
    private RequestManager requestManager;

    private QueryParamsSource queryUrlWriter;
    private CommonParamsSource commonUrlWriter;
    private FacetParamsSource facetUrlWriter;

    private String searchUrl;

    private FacetsResultProcessor facetsResultHandler;

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        ServerConfig serverConfig = event.getSolrCore().getConfiguration().getConfig(ServerConfig.class);
        searchUrl = serverConfig.getSearchUrl();
    }

    public void searchQueryFacet(QueryFacetDefinition facetDefintion, AsyncCallback<QueryFacet> callback) {
        searchQueryFacet(facetDefintion, searchContext, callback);
    }

    public void searchQueryFacet(final QueryFacetDefinition facetDefinition, SearchContext context, AsyncCallback<QueryFacet> callback) {
        RequestParams params = new RequestParams();
        commonUrlWriter.addParams(params, context.getContext(CommonContext.class));

        QueryContext queryContext = context.getContext(QueryContext.class);
        queryUrlWriter.addParams(params, queryContext, new QueryParamsSource.Hints().setOverrideRows(0));

        FacetContext tempContext = new FacetContext();
        tempContext.addQueryFacetDefintiion(facetDefinition);
        facetUrlWriter.addParams(params, tempContext);

        requestManager.send(searchUrl, params, callback, new RequestManager.XmlResponseParser<QueryFacet>() {
            public QueryFacet parse(XmlResponse xmlResponse) throws Exception {
                return facetsResultHandler.parseQueryFacet(facetDefinition, xmlResponse.getDocument());
            }
        });
    }

    public void searchFieldFacet(FieldFacetDefinition facetDefinition, AsyncCallback<FieldFacet> callback) {
        searchFieldFacet(facetDefinition, searchContext, callback);
    }

    public void searchFieldFacet(final FieldFacetDefinition facetDefinition, SearchContext context, AsyncCallback<FieldFacet> callback) {
        RequestParams params = new RequestParams();

        commonUrlWriter.addParams(params, context.getContext(CommonContext.class));

        QueryContext queryContext = context.getContext(QueryContext.class);
        queryUrlWriter.addParams(params, queryContext, new QueryParamsSource.Hints().setOverrideRows(0));

        FacetContext tempContext = new FacetContext();
        tempContext.addFieldFacetDefintiion(facetDefinition);
        facetUrlWriter.addParams(params, tempContext);

        requestManager.send(searchUrl, params, callback, new RequestManager.XmlResponseParser<FieldFacet>() {
            public FieldFacet parse(XmlResponse xmlResponse) throws Exception {
                return facetsResultHandler.parseFieldFacet(facetDefinition, xmlResponse.getDocument());
            }
        });
    }

    public void searchDateFacet(DateFacetDefinition facetDefinition, AsyncCallback<DateFacet> callback) {
        searchDateFacet(facetDefinition, searchContext, callback);
    }

    public void searchDateFacet(final DateFacetDefinition facetDefinition, SearchContext context, AsyncCallback<DateFacet> callback) {
        RequestParams params = new RequestParams();

        commonUrlWriter.addParams(params, context.getContext(CommonContext.class));

        QueryContext queryContext = context.getContext(QueryContext.class);
        queryUrlWriter.addParams(params, queryContext, new QueryParamsSource.Hints().setOverrideRows(0));

        FacetContext tempContext = new FacetContext();
        tempContext.addDateFacetDefintion(facetDefinition);
        facetUrlWriter.addParams(params, tempContext);

        requestManager.send(searchUrl, params, callback, new RequestManager.XmlResponseParser<DateFacet>() {
            public DateFacet parse(XmlResponse xmlResponse) throws Exception {
                return facetsResultHandler.parseDateFacet(facetDefinition, xmlResponse.getDocument());
            }
        });
    }

    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setSearchContext(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    @Inject
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Inject
    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Inject
    public void setQueryUrlWriter(QueryParamsSource queryUrlWriter) {
        this.queryUrlWriter = queryUrlWriter;
    }

    @Inject
    public void setCommonUrlWriter(CommonParamsSource commonUrlWriter) {
        this.commonUrlWriter = commonUrlWriter;
    }

    @Inject
    public void setFacetUrlWriter(FacetParamsSource facetUrlWriter) {
        this.facetUrlWriter = facetUrlWriter;
    }

    @Inject
    public void setFacetsResultHandler(FacetsResultProcessor facetsResultHandler) {
        this.facetsResultHandler = facetsResultHandler;
    }
}
