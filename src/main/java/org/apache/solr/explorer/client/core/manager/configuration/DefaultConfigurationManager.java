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

package org.apache.solr.explorer.client.core.manager.configuration;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.*;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.RequestManager;
import org.apache.solr.explorer.client.core.manager.solr.admin.Schema;
import org.apache.solr.explorer.client.core.model.configuration.Configuration;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.ioc.core.client.annotation.InjectionType;

import java.util.ArrayList;
import java.util.List;

import static org.gwtoolbox.commons.xml.client.DOMUtils.getSingleChildValue;

/**
 * @author Uri Boness
 */
@Component(name = "configurationManager")
public class DefaultConfigurationManager implements ConfigurationManager {

    private Configuration configuration;

    private String currentCoreName;

    private List<ConfigLoader> configLoaders;

    private RequestManager requestManager;

    public void load(final AsyncCallback<Configuration> callback) {
        loadConfiguration(new AsyncCallback<Configuration>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(Configuration result) {
                configuration = result;
                callback.onSuccess(configuration);
            }
        });
    }

    public void verifySolrCoreConfiguration(Verification verification, SolrCoreConfiguration config, Schema schema) {
        for (ConfigLoader loader : configLoaders) {
            if (loader.isActive()) {
                loader.verify(verification, config, schema);
            }
        }
    }

    public Configuration getConfiguration() {
        if (configuration == null) {
            throw new IllegalStateException("The configuration must be loaded before it can be fetched. " +
                    "Did you forget to call the load(...) method?");
        }
        return configuration;
    }

    public SolrCoreConfiguration getCurrentSolrCoreConfiguration() {
        return configuration.getCoreConfiguration(currentCoreName);
    }

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        currentCoreName = event.getSolrCore().getConfiguration().getCoreName();
    }


    //============================================ Setter/Getter Methods ===============================================

    @Inject(by = InjectionType.TYPE)
    public void setConfigLoaders(List<ConfigLoader> configLoaders) {
        this.configLoaders = configLoaders;
    }

    @Inject
    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    //================================================ Helper Methods ==================================================

    private void loadConfiguration(final AsyncCallback<Configuration> callback) {
        requestManager.loadTextResource("conf", callback, new RequestManager.TextParser<Configuration>() {
            public Configuration parse(String text) throws Exception {
                return parseConfiguration(text);
            }
        });
    }

    private Configuration parseConfiguration(String text) throws ConfigurationException {
        Configuration config = new Configuration();
        Document document = XMLParser.parse(text);
        Element root = document.getDocumentElement();

        String helpUrl = getSingleChildValue(root, "help-url");
        config.setHelpUrl(helpUrl);

        NodeList nodes = root.getElementsByTagName("solr-core");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            SolrCoreConfiguration coreConfig = parseSolrCoreConfiguration((Element) node);
            config.addSolrCoreConfiguration(coreConfig);
        }

        return config;
    }

    private SolrCoreConfiguration parseSolrCoreConfiguration(Element element) throws ConfigurationException {
        SolrCoreConfiguration config = new SolrCoreConfiguration();
        String name = element.getAttribute("name");
        config.setCoreName(name);

        List<ConfigLoader> activeLoaders = new ArrayList<ConfigLoader>();

        for (ConfigLoader loader : configLoaders) {
            boolean loaded = loader.load(element, config);
            if (loaded) {
                activeLoaders.add(loader);
            }
        }

        for (ConfigLoader loader : activeLoaders) {
            loader.postProcess(config);
        }

        return config;
    }

}
