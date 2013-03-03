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
import org.apache.solr.explorer.client.core.model.configuration.sort.SortConfig;
import static org.gwtoolbox.commons.xml.client.DOMUtils.getSingleChild;
import static org.gwtoolbox.commons.xml.client.DOMUtils.getSingleChildValue;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;

/**
 * @author Uri Boness
 */
@Component
public class SortConfigLoader extends AbstractConfigLoader {

    public boolean load(Element coreElement, SolrCoreConfiguration configuration) {
        Element sorting = getSingleChild(coreElement, "sorting");
        if (sorting == null) {
            return false;
        }

        SortConfig sortConfig = new SortConfig();
        String sortableFields = getSingleChildValue(sorting, "sortable-fields");
        String[] tokens = sortableFields.split(",");
        for (String token : tokens) {
            sortConfig.addSortableField(token.trim());
        }

        Element sortButtons = getSingleChild(sorting, "sort-buttons");
        NodeList sortButtonElements = sortButtons.getElementsByTagName("sort-button");
        for (int i=0; i<sortButtonElements.getLength(); i++) {
            Element sortButtonElement = (Element) sortButtonElements.item(i);
            String name = sortButtonElement.getAttribute("name");
            String fieldName = sortButtonElement.getAttribute("fieldName");
            sortConfig.addDefaultSortButton(name, fieldName);
        }

        configuration.setConfig(SortConfig.class, sortConfig);
        return true;
    }
}
