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

package org.apache.solr.explorer.client.core.manager.solr.search;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Element;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.RequestManager;
import org.apache.solr.explorer.client.core.manager.RequestParams;
import org.apache.solr.explorer.client.core.manager.XmlResponse;
import org.apache.solr.explorer.client.core.manager.logging.Logger;
import org.apache.solr.explorer.client.core.manager.solr.ParamsSource;
import org.apache.solr.explorer.client.core.manager.solr.search.handler.SearchResultProcessor;
import org.apache.solr.explorer.client.core.manager.ui.IndicatingCallback;
import org.apache.solr.explorer.client.core.manager.ui.ProgressIndicator;
import org.apache.solr.explorer.client.core.manager.ui.UIManager;
import org.apache.solr.explorer.client.core.model.configuration.ServerConfig;
import org.apache.solr.explorer.client.core.model.context.Context;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Inject;

import java.util.List;

import static org.apache.solr.explorer.client.util.SolrDomUtils.getIntChild;
import static org.apache.solr.explorer.client.util.SolrDomUtils.getLstChild;

/**
 * @author Uri Boness
 */
@Component(name = "searchManager")
public class DefaultSearchManager extends AbstractSearchManager {

    private UIManager uiManager;
    private Logger logger;

    private List<SearchResultProcessor> searchResultProcessors;

    private String searchUrl;

    private List<ParamsSource> paramsSources;

    private RequestManager requestManager;

    /**
     * Empty default constructor.
     */
    public DefaultSearchManager() {
    }

    public void search(SearchContext context, AsyncCallback<SearchResult> callback) {
        RequestParams params = new RequestParams();
        for (ParamsSource source : paramsSources) {
            if (source.isActive()) {
                Context subContext = context.getContext(source.getContextClass());
                source.addParams(params, subContext);
            }
        }
        sendRequest(context, params, callback);
    }

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        ServerConfig config = event.getSolrCore().getConfiguration().getConfig(ServerConfig.class);
        searchUrl = config.getSearchUrl();
    }


    //============================================== Helper Methods ====================================================

    protected void sendRequest(SearchContext context, RequestParams params, AsyncCallback<SearchResult> callback) {
        getMulticaster().notifyNewSearch(this);
        requestManager.send(searchUrl, params, callback, new SearchResultResponseParser(context));
    }

    protected <T> AsyncCallback<T> wrapIfNeeded(String message, AsyncCallback<T> callback) {
        if (callback instanceof IndicatingCallback) {
            return callback;
        }
        ProgressIndicator indicator = uiManager.showProgressIndicator(message);
        return new IndicatingCallback<T>(callback, indicator);
    }

    //============================================== Setter/Getter =====================================================

    @Inject
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Inject
    public void setUiManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    @Inject
    public void setSearchResultPopulators(List<SearchResultProcessor> searchResultProcessors) {
        this.searchResultProcessors = searchResultProcessors;
    }

    @Inject
    public void setParamsSources(List<ParamsSource> paramsSources) {
        this.paramsSources = paramsSources;
    }

    @Inject
    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    //============================================== Inner Classes =====================================================

    protected class SearchResultResponseParser implements RequestManager.XmlResponseParser<SearchResult> {

        private final SearchContext context;

        public SearchResultResponseParser(SearchContext context) {
            this.context = context;
        }

        public SearchResult parse(XmlResponse xmlResponse) throws Exception {
            Element element = xmlResponse.getDocument().getDocumentElement();

            Element headerElement = getLstChild(element, "responseHeader");

            // response header
            int qTime = getIntChild(headerElement, "QTime");

            SearchResult result = new SearchResult(xmlResponse.getRawText(), qTime, context);

            for (SearchResultProcessor processor : searchResultProcessors) {
                if (processor.isActive()) {
                    processor.process(context, xmlResponse.getDocument(), result);
                }
            }

            for (SearchResultProcessor processor : searchResultProcessors) {
                if (processor.isActive()) {
                    processor.postProcess(context, result);
                }
            }

            return result;
        }
    }

}