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

package org.apache.solr.explorer.client.core.manager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.logging.Logger;
import org.apache.solr.explorer.client.core.model.configuration.ServerConfig;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Inject;

/**
 * @author Uri Boness
 */
@Component(name = "requestManager")
public class DefaultRequestManager implements RequestManager {
    
    public final static String TARGET_URL_HEADER = "Solr-Explorer-Target-URL";
    private final static String CONTENT_TYPE_HEADER = "Content-Type";

    private Logger logger;
    private int requestTimeout = 10 * 1000; // defaults to 10 seconds.

    private static int jsonRequestId = 0;

    public Request send(String url, RequestParams params, AsyncCallback<XmlResponse> callback) {
        return send(url, params, requestTimeout, callback);
    }

    public Request send(String url, RequestParams params, final int timeout, final AsyncCallback<XmlResponse> callback) {
        String data = params.buildEncodedQueryString();
        if (logger.isDebugEnabled()) {
            logger.debug("URL: " + url + "{" + params.buildQueryString() + "}");
        }
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, "/solr");
        requestBuilder.setHeader(TARGET_URL_HEADER, url);
        requestBuilder.setHeader(CONTENT_TYPE_HEADER, "application/x-www-form-urlencoded");
        requestBuilder.setRequestData(data);
        requestBuilder.setTimeoutMillis(timeout);
        requestBuilder.setCallback(new RequestCallback() {
            public void onResponseReceived(Request request, Response response) {
                String rawText = response.getText();
                Document document = XMLParser.parse(rawText);
                callback.onSuccess(new XmlResponse(rawText, document));
            }

            public void onError(Request request, Throwable exception) {
                callback.onFailure(exception);
            }
        });
        try {
            return requestBuilder.send();
        } catch (RequestException re) {
            callback.onFailure(re);
            return null;
        }
    }

    public <T> Request send(String url, RequestParams params, final AsyncCallback<T> callback, final XmlResponseParser<T> responseParser) {
        return send(url, params, requestTimeout, callback, responseParser);
    }

    public <T> Request send(String url, RequestParams params, int timeout, final AsyncCallback<T> callback, final XmlResponseParser<T> responseParser) {
        return send(url, params, requestTimeout, new AsyncCallback<XmlResponse>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(XmlResponse response) {
                try {
                    T object = responseParser.parse(response);
                    callback.onSuccess(object);
                } catch (Exception e) {
                    onFailure(e);
                }
            }
        });
    }

    public Request loadTextResource(String name, final AsyncCallback<String> callback) {
        String url = GWT.getHostPageBaseURL() + name;

        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);

        try {

            return requestBuilder.sendRequest(null, new RequestCallback() {
                public void onResponseReceived(Request request, Response response) {
                    callback.onSuccess(response.getText());
                }

                public void onError(Request request, Throwable exception) {
                    callback.onFailure(exception);
                }
            });
        } catch (RequestException re) {
            callback.onFailure(re);
            return null;
        }
    }

    public <T> Request loadTextResource(String name, final AsyncCallback<T> callback, final TextParser<T> parser) {
        return loadTextResource(name, new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(String text) {
                try {
                    callback.onSuccess(parser.parse(text));
                } catch (Throwable t) {
                    callback.onFailure(t);
                }
            }
        });
    }

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        ServerConfig serverConfig = event.getSolrCore().getConfiguration().getConfig(ServerConfig.class);
        requestTimeout = serverConfig.getConnectionTimeout();
    }

    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
