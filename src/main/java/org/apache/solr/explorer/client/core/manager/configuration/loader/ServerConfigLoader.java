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

package org.apache.solr.explorer.client.core.manager.configuration.loader;

import com.google.gwt.xml.client.Element;
import org.apache.solr.explorer.client.core.model.configuration.ServerConfig;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import static org.gwtoolbox.commons.xml.client.DOMUtils.*;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.ConfiguredProperty;

/**
 * @author Uri Boness
 */
@Component
public class ServerConfigLoader extends AbstractConfigLoader {

    private int defaultConnectionTimeout;
    private String defaultSelectUri;
    private String defaultLukeUri;

    public boolean load(Element coreElement, SolrCoreConfiguration configuration) {
        Element element = getSingleChild(coreElement, "server");
        String baseUrl = element.getAttribute("baseUrl");
        int timeout = getIntegerAttribute(element, "connectionTimeout", defaultConnectionTimeout);
        ServerConfig serverConfig = new ServerConfig(baseUrl, timeout);
        String searchUri = getSingleChildValue(element, "select-uri", defaultSelectUri);
        serverConfig.setSearchUri(searchUri);
        serverConfig.setLukeUri(getSingleChildValue(element, "luke-uri", defaultLukeUri));

        configuration.setConfig(ServerConfig.class, serverConfig);
        return true;
    }


    //============================================ Setter/Getter Methods ===============================================

    @ConfiguredProperty("core.server.default.connection.timeout")
    public void setDefaultConnectionTimeout(int defaultConnectionTimeout) {
        this.defaultConnectionTimeout = defaultConnectionTimeout;
    }

    @ConfiguredProperty("core.server.default.selectUri")
    public void setDefaultSelectUri(String defaultSelectUri) {
        this.defaultSelectUri = defaultSelectUri;
    }

    @ConfiguredProperty("core.server.default.lukeUri")
    public void setDefaultLukeUri(String defaultLukeUri) {
        this.defaultLukeUri = defaultLukeUri;
    }
}
