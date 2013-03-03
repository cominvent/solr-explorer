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

package org.apache.solr.explorer.client.plugin.dih.manager;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.RequestManager;
import org.apache.solr.explorer.client.core.manager.RequestParams;
import org.apache.solr.explorer.client.core.manager.XmlResponse;
import org.apache.solr.explorer.client.core.manager.solr.search.urlwriter.CommonParamsSource;
import org.apache.solr.explorer.client.core.model.context.CommonContext;
import org.apache.solr.explorer.client.plugin.dih.model.config.DIHConfig;
import org.apache.solr.explorer.client.plugin.dih.model.result.DIHResult;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Inject;

import static org.apache.solr.explorer.client.util.SolrDomUtils.getLstChild;
import static org.apache.solr.explorer.client.util.SolrDomUtils.getStringChild;
import static org.gwtoolbox.commons.xml.client.DOMUtils.children;
import static org.gwtoolbox.commons.xml.client.DOMUtils.getTextValue;

/**
 * @author Uri Boness
 */
@Component(name = "dihManager")
public class DefaultDIHManager implements DIHManager {

    private RequestManager requestManager;

    private DIHConfig config;

    private CommonContext commonContext;
    private CommonParamsSource commonUrlWriter;

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        config = event.getSolrCore().getConfiguration().getConfig(DIHConfig.class);
    }

    public void executeFullImport(AsyncCallback<DIHResult> callback) {
        executeCommand("full-import", callback);
    }

    public void executeDeltaImport(AsyncCallback<DIHResult> callback) {
        executeCommand("delta-import", callback);
    }

    public void getStatus(AsyncCallback<DIHResult> callback) {
        executeCommand("status", callback);
    }

    public void reloadConfig(AsyncCallback<DIHResult> callback) {
        executeCommand("reload-config", callback);
    }

    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Inject
    public void setCommonUrlWriter(CommonParamsSource commonUrlWriter) {
        this.commonUrlWriter = commonUrlWriter;
    }

    @Inject
    public void setCommonContext(CommonContext commonContext) {
        this.commonContext = commonContext;
    }

    //================================================ Helper Methods ==================================================

    private void executeCommand(String command, AsyncCallback<DIHResult> callback) {
        RequestParams params = new RequestParams().addParameter("command", command);
        commonUrlWriter.addParams(params, commonContext);
        requestManager.send(config.getUrl(), params, callback, new RequestManager.XmlResponseParser<DIHResult>() {
            public DIHResult parse(XmlResponse xmlResponse) throws Exception {
                return parseResult(xmlResponse.getDocument());
            }
        });
    }

    private DIHResult parseResult(Document document) {
        DIHResult result = new DIHResult();
        String importantResponse = getStringChild(document.getDocumentElement(), "importResponse");
        result.setImportantMessage(importantResponse);
        Element statusMessagesElement = getLstChild(document.getDocumentElement(), "statusMessages");
        if (statusMessagesElement == null) {
            return null;
        }
        for (Element messageElement : children(statusMessagesElement)) {
            result.addMessage(messageElement.getAttribute("name"), getTextValue(messageElement));
        }
        return result;
    }
}
