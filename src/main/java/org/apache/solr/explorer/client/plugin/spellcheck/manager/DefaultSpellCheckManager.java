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

package org.apache.solr.explorer.client.plugin.spellcheck.manager;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.RequestManager;
import org.apache.solr.explorer.client.core.manager.RequestParams;
import org.apache.solr.explorer.client.core.manager.XmlResponse;
import org.apache.solr.explorer.client.core.manager.solr.search.urlwriter.CommonParamsSource;
import org.apache.solr.explorer.client.core.model.context.CommonContext;
import org.apache.solr.explorer.client.plugin.spellcheck.model.config.SpellCheckConfig;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.widget.client.notification.GGrowl;

/**
 * @author Uri Boness
 */
@Component(name = "spellCheckManager")
public class DefaultSpellCheckManager implements SpellCheckManager {

    private RequestManager requestManager;

    private String spellCheckUrl;

    private int timeout;

    private CommonContext commonContext;
    private CommonParamsSource commonUrlWriter;

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        SpellCheckConfig config = event.getSolrCore().getConfiguration().getConfig(SpellCheckConfig.class);
        if (config == null) {
            return;
        }
        spellCheckUrl = config.getUrl();
        timeout = config.getTimeout();
    }

    public void executeRebuild(AsyncCallback<Void> callback) {
        RequestParams params = new RequestParams()
                .addParameter("spellcheck", true)
                .addParameter("spellcheck.build", true)
                .addParameter("q", "*:*")
                .addParameter("rows", 0);
        commonUrlWriter.addParams(params, commonContext);
        requestManager.send(spellCheckUrl, params, timeout, callback, new RequestManager.XmlResponseParser<Void>() {
            public Void parse(XmlResponse xmlResponse) throws Exception {
                GGrowl.showMessage("Spellchecking", "Spellchecking dictionary built successfully");
                return null;
            }
        });
    }

    public void executeReload(AsyncCallback<Void> callback) {
        RequestParams params = new RequestParams()
                .addParameter("spellcheck", true)
                .addParameter("spellcheck.reload", true)
                .addParameter("q", "*:*")
                .addParameter("rows", 0);

        commonUrlWriter.addParams(params, commonContext);

        requestManager.send(spellCheckUrl, params, timeout, callback, new RequestManager.XmlResponseParser<Void>() {
            public Void parse(XmlResponse xmlResponse) throws Exception {
                GGrowl.showMessage("Spellchecking", "Spellchecking dictionary reloaded successfully");
                return null;
            }
        });
    }


    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Inject
    public void setCommonContext(CommonContext commonContext) {
        this.commonContext = commonContext;
    }

    @Inject
    public void setCommonUrlWriter(CommonParamsSource commonUrlWriter) {
        this.commonUrlWriter = commonUrlWriter;
    }
}
