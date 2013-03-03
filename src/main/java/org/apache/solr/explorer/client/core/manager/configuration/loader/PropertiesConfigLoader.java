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

import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.util.collection.Properties;
import static org.gwtoolbox.commons.xml.client.DOMUtils.getSingleChild;
import org.gwtoolbox.commons.xml.client.DOMUtils;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.Node;

/**
 * @author Uri Boness
 */
@Component
public class PropertiesConfigLoader extends AbstractConfigLoader {

    public boolean load(Element coreElement, SolrCoreConfiguration configuration) {
        Properties properties = new Properties();
        configuration.setProperties(properties);

        Element element = getSingleChild(coreElement, "properties");
        if (element == null) {
            return true;
        }
        NodeList nodes = element.getChildNodes();
        for (int i=0; i<nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element propertyElement = (Element) node;
            if ("property".equals(propertyElement.getNodeName())) {
                String name = propertyElement.getAttribute("name");
                String value = DOMUtils.getTextValue(propertyElement);
                properties.put(name, value);
            }
        }
        return true;
    }
}
