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

import org.apache.solr.explorer.client.core.model.configuration.SearchConfig;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.gwtoolbox.ioc.core.client.annotation.ConfiguredProperty;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import static org.gwtoolbox.commons.xml.client.DOMUtils.*;
import com.google.gwt.xml.client.Element;

/**
 * @author Uri Boness
 */
@Component
public class SearchConfigLoader extends AbstractConfigLoader {

    private String defaultSearchQuery;
    private int defaultPageSize;

    public boolean load(Element coreElement, SolrCoreConfiguration configuration) {
        String searchQuery = defaultSearchQuery;
        int pageSize = defaultPageSize;
        boolean useQaltForEmptyQueries = false;

        Element element = getSingleChild(coreElement, "search");
        if (element != null) {
            searchQuery = getSingleChildValue(element, "default-query", searchQuery);
            pageSize = getSingleChildIntegerValue(element, "page-size", pageSize);
            useQaltForEmptyQueries = getSingleChildBooleanValue(element, "use-q.alt", useQaltForEmptyQueries);
        }

        configuration.setConfig(SearchConfig.class, new SearchConfig(searchQuery, pageSize, useQaltForEmptyQueries));

        return true;
    }


    //============================================ Setter/Getter Methods ===============================================

    @ConfiguredProperty("core.search.default.query")
    public void setDefaultSearchQuery(String defaultSearchQuery) {
        this.defaultSearchQuery = defaultSearchQuery;
    }

    @ConfiguredProperty("core.search.default.pageSize")
    public void setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }
}
