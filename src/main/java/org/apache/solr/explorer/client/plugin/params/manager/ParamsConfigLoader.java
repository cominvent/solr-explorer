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

package org.apache.solr.explorer.client.plugin.params.manager;

import com.google.gwt.xml.client.Element;
import org.apache.solr.explorer.client.core.manager.configuration.ConfigLoader;
import org.apache.solr.explorer.client.core.manager.configuration.Verification;
import org.apache.solr.explorer.client.core.manager.solr.admin.Schema;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.plugin.params.model.ParamsContext;
import org.gwtoolbox.commons.xml.client.DOMUtils;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Inject;

import java.util.HashMap;
import java.util.Map;

import static org.gwtoolbox.commons.xml.client.DOMUtils.children;

/**
 * @author Uri Boness
 */
@Component
public class ParamsConfigLoader implements ConfigLoader {

    private ParamsContext context;

    public boolean load(Element coreElement, SolrCoreConfiguration configuration) {
        Element paramsElement = DOMUtils.getSingleChild(coreElement, "request-params");
        if (paramsElement == null) {
            return false;
        }
        Map<String, String> params = new HashMap<String, String>();
        for (Element paramElement : children(paramsElement)) {
            String name = paramElement.getAttribute("name");
            String value = DOMUtils.getTextValue(paramElement);
            params.put(name, value);
        }
        context.setParams(params);
        return true;
    }

    public void postProcess(SolrCoreConfiguration configuration) {
    }

    public void verify(Verification verification, SolrCoreConfiguration configuration, Schema schema) {
    }

    public void init(SolrCore solrCore) {
    }

    public boolean isActive() {
        return true;
    }

    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setContext(ParamsContext context) {
        this.context = context;
    }
}
