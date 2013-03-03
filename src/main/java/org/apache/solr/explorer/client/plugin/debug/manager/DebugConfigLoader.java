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

package org.apache.solr.explorer.client.plugin.debug.manager;

import com.google.gwt.xml.client.Element;
import org.apache.solr.explorer.client.core.manager.configuration.loader.AbstractConfigLoader;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.plugin.debug.model.config.DebugConfig;
import org.gwtoolbox.ioc.core.client.annotation.Component;

import static org.gwtoolbox.commons.xml.client.DOMUtils.*;

/**
 * @author Uri Boness
 */
@Component
public class DebugConfigLoader extends AbstractConfigLoader {

    public boolean load(Element coreElement, SolrCoreConfiguration configuration) {
        Element debugElement = getSingleChild(coreElement, "debug");
        if (debugElement == null) {
            return false;
        }
        DebugConfig config = new DebugConfig();

        boolean enabled = getBooleanAttribute(debugElement, "query", true);
        config.setDebugQuery(enabled);

        configuration.setConfig(DebugConfig.class, config);

        return true;
    }

}
