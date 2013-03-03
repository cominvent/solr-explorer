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

import com.google.gwt.xml.client.Element;
import org.apache.solr.explorer.client.core.manager.logging.Logger;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.core.model.configuration.ServerConfig;
import org.apache.solr.explorer.client.plugin.dih.model.config.DIHConfig;
import org.apache.solr.explorer.client.core.manager.configuration.loader.AbstractConfigLoader;
import static org.gwtoolbox.commons.xml.client.DOMUtils.getSingleChild;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.ConfiguredProperty;
import org.gwtoolbox.ioc.core.client.annotation.Inject;

/**
 * @author Uri Boness
 */
@Component
public class DIHConfigLoader extends AbstractConfigLoader {

    private String defaultUri;

    private Logger logger;

    public boolean load(Element coreElement, SolrCoreConfiguration configuration) {
        if (logger.isInfoEnabled()) {
            logger.info("Looking for DIH configuration...");
        }

        Element element = getSingleChild(coreElement, "dih");
        if (element == null) {
            return false;
        }

        if (logger.isInfoEnabled()) {
            logger.info("Found DIH configuration - Loading...");
        }

        DIHConfig config = new DIHConfig();
        String uri = element.getAttribute("uri");
        if (uri == null) {
            uri = defaultUri;
        }
        config.setUri(uri);

        configuration.setConfig(DIHConfig.class, config);
        return true;
    }

    @Override
    public void postProcess(SolrCoreConfiguration configuration) {
        DIHConfig config = configuration.getConfig(DIHConfig.class);
        if (config == null) {
            return;
        }
        ServerConfig serverConfig = configuration.getConfig(ServerConfig.class);
        config.setUrl(serverConfig.getBaseUrl() + config.getUri());
    }

    //============================================ Setter/Getter Methods ===============================================

    @ConfiguredProperty("dih.default.uri")
    public void setDefaultUri(String defaultUri) {
        this.defaultUri = defaultUri;
    }

    @Inject
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}