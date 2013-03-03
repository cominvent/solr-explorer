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

package org.apache.solr.explorer.client.plugin.facet.manager;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import org.apache.solr.explorer.client.core.manager.configuration.Message;
import org.apache.solr.explorer.client.core.manager.configuration.Verification;
import org.apache.solr.explorer.client.core.manager.configuration.loader.AbstractConfigLoader;
import org.apache.solr.explorer.client.core.manager.solr.admin.Schema;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.plugin.facet.model.config.FacetsConfig;
import org.apache.solr.explorer.client.plugin.facet.model.context.*;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.ConfiguredProperty;

import java.util.HashSet;
import java.util.Set;

import static org.gwtoolbox.commons.xml.client.DOMUtils.*;

/**
 * @author Uri Boness
 */
@Component
public class FacetsConfigLoader extends AbstractConfigLoader {

    private int deafultFacetMinCount;

    public boolean load(Element coreElement, SolrCoreConfiguration configuration) {
        Element element = getSingleChild(coreElement, "facets");
        if (element == null) {
            return false;
        }

        FacetsConfig facetsConfig = new FacetsConfig();

        NodeList fieldFacets = element.getElementsByTagName("field-facet");
        for (int i=0; i<fieldFacets.getLength(); i++) {
            Element fieldFacet = (Element) fieldFacets.item(i);
            FieldFacetDefinition definition = new FieldFacetDefinition();
            definition.setFixed(true);

            definition.setName(fieldFacet.getAttribute("name"));
            definition.setMutuallyExclusive(getBooleanAttribute(fieldFacet, "mutuallyExclusive", true));
            definition.setShowAsCloud(getBooleanAttribute(fieldFacet, "showAsCloud", false));

            definition.setFieldName(getSingleChildValue(fieldFacet, "field"));
            String sort = getSingleChildValue(fieldFacet, "sort", null);
            if (sort != null) {
                definition.setSort(FieldFacetDefinition.Sort.valueOf(sort.toUpperCase()));
            }
            definition.setMissing(getSingleChildBooleanValue(fieldFacet, "show-missing"));
            definition.setMinCount(getSingleChildIntegerValue(fieldFacet, "min-count", deafultFacetMinCount));
            definition.setLimit(getSingleChildIntegerValue(fieldFacet, "max-entries"));

            facetsConfig.addFieldFacetDefinition(definition);
        }

        NodeList queryFacets = element.getElementsByTagName("query-facet");
        for (int i=0; i<queryFacets.getLength(); i++) {
            Element queryFacet = (Element) queryFacets.item(i);
            QueryFacetDefinition definition = new QueryFacetDefinition();
            definition.setFixed(true);

            definition.setName(queryFacet.getAttribute("name"));
            definition.setQuery(getSingleChildValue(queryFacet, "query"));

            facetsConfig.addQueryFacetDefinition(definition);
        }

        NodeList dateFacets = element.getElementsByTagName("date-facet");
        for (int i=0; i<dateFacets.getLength(); i++) {
            Element dateFacet = (Element) dateFacets.item(i);
            DateFacetDefinition definition = new DateFacetDefinition();
            definition.setFixed(true);
            
            definition.setName(dateFacet.getAttribute("name"));
            definition.setFieldName(getSingleChildValue(dateFacet, "field"));
            definition.setStart(getSingleChildValue(dateFacet, "start"));
            definition.setEnd(getSingleChildValue(dateFacet, "end"));
            Element gap = getSingleChild(dateFacet, "gap");
            DateGap.Unit unit = DateGap.Unit.valueOf(getTextValue(gap).toUpperCase());
            int count = getIntegerAttribute(gap, "count");
            definition.setGap(new DateGap(count, unit));
            definition.setHardEnd(getSingleChildBooleanValue(dateFacet, "hard-end", false));
            Set<DateOther> others = new HashSet<DateOther>();
            for (Element other : children(dateFacet, "others")) {
                others.add(DateOther.valueOf(getTextValue(other).toUpperCase()));
            }
            definition.setOthers(others);
            facetsConfig.addDateFacetDefinition(definition);
        }

        configuration.setConfig(FacetsConfig.class, facetsConfig);

        return true;
    }

    @Override
    public void verify(Verification verification, SolrCoreConfiguration configuration, Schema schema) {
        FacetsConfig facetsConfig = configuration.getConfig(FacetsConfig.class);

        for (FieldFacetDefinition definition : facetsConfig.getFieldFacetDefinitions()) {
            String field = definition.getFieldName();
            if (!schema.hasField(field)) {
                String title = "Invalid configuration for facet '" + definition.getName() + "'";
                String body = "Unknown field '" + field + "'";
                verification.registerError(new Message("Facets", title, body));
            }
        }

        for (DateFacetDefinition definition : facetsConfig.getDateFacetDefinitions()) {
            String field = definition.getFieldName();
            if (!schema.hasField(field)) {
                String title = "Invalid configuration for facet '" + definition.getName() + "'";
                String body = "Unknown field '" + field + "'";
                verification.registerError(new Message("Facets", title, body));
            }
            if (!schema.getField(field).getType().isDateField()) {
                String title = "Invalid configuration for facet '" + definition.getName() + "'";
                String body = "Field '" + field + "' is not a date filed";
                verification.registerError(new Message("Facets", title, body));
            }
        }
    }

    //============================================ Setter/Getter Methods ===============================================

    @ConfiguredProperty("facet.default.minCount")
    public void setDeafultFacetMinCount(int deafultFacetMinCount) {
        this.deafultFacetMinCount = deafultFacetMinCount;
    }
}
