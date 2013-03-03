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

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.search.handler.AbstractSearchResultProcessor;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.plugin.facet.model.config.FacetsConfig;
import org.apache.solr.explorer.client.plugin.facet.model.context.DateFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.context.FacetContext;
import org.apache.solr.explorer.client.plugin.facet.model.context.FieldFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.context.QueryFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.result.*;
import org.gwtoolbox.ioc.core.client.annotation.Component;

import static org.apache.solr.explorer.client.util.SolrDomUtils.getIntChild;
import static org.apache.solr.explorer.client.util.SolrDomUtils.getLstChild;
import static org.gwtoolbox.commons.xml.client.DOMUtils.children;
import static org.gwtoolbox.commons.xml.client.DOMUtils.getIntValue;

/**
 * @author Uri Boness
 */
@Component
public class FacetsResultProcessor extends AbstractSearchResultProcessor {

    @Override
    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(FacetsConfig.class));
    }

    /**
     * TODO: remove duplicate code... use the parse functions below.
     */
    public void process(SearchContext context, Document document, SearchResult result) {

        FacetContext facetContext = context.getContext(FacetContext.class);

        FacetResult facetResult = new FacetResult();

        Element facetCountsElement = getLstChild(document.getDocumentElement(), "facet_counts");
        if (facetCountsElement != null) {
            Element facetFieldsElement = getLstChild(facetCountsElement, "facet_fields");
            for (Element facetElement : children(facetFieldsElement)) {
                String facetId = facetElement.getAttribute("name");
                FieldFacetDefinition facetDefinition = facetContext.getFieldFacetDefinition(facetId);
                FieldFacet facet = new FieldFacet(facetId, facetDefinition.getName(), facetDefinition.getFieldName());
                for (Element entryElement : children(facetElement)) {
                    String value = entryElement.getAttribute("name");
                    int count = getIntValue(entryElement);
                    FacetEntry facetEntry = new FacetEntry(value, count);
                    facet.addEntry(facetEntry);
                }
                facetResult.addFieldFacet(facet);
            }
            Element facetDatesElement = getLstChild(facetCountsElement, "facet_dates");
            for (Element facetElement : children(facetDatesElement)) {
                String facetId = facetElement.getAttribute("name");
                DateFacetDefinition facetDefinition = facetContext.getDateFacetDefinition(facetId);
                DateFacet facet = new DateFacet(facetId, facetDefinition.getName(), facetDefinition.getFieldName());
                for (Element entryElement : children(facetElement)) {
                    String name = entryElement.getAttribute("name");
                    if ("gap".equals(name) || "end".equals(name)) {
                        continue;
                    }
                    int count = getIntValue(entryElement);
                    FacetEntry facetEntry = new FacetEntry(name, count);
                    facet.addEntry(facetEntry);
                }
                facetResult.addDateFacet(facet);
            }
            Element facetQueriesElement = getLstChild(facetCountsElement, "facet_queries");
            for (Element facetElement : children(facetQueriesElement)) {
                String facetId = facetElement.getAttribute("name");
                QueryFacetDefinition facetDefinition = facetContext.getQueryFacetDefinition(facetId);
                int count = getIntValue(facetElement);
                QueryFacet queryFacet = new QueryFacet(facetId, facetDefinition.getName(), facetDefinition.getQuery(), count);
                facetResult.addQueryFacet(queryFacet);
            }
        }

        result.setPart(FacetResult.class, facetResult);
    }

    public QueryFacet parseQueryFacet(QueryFacetDefinition facetDefinition, Document document) {
        Element facetCountsElement = getLstChild(document.getDocumentElement(), "facet_counts");
        Element facetQueriesElement = getLstChild(facetCountsElement, "facet_queries");
        String facetId = facetDefinition.getId();
        int count = getIntChild(facetQueriesElement, facetId);
        return new QueryFacet(facetId, facetDefinition.getName(), facetDefinition.getQuery(), count);
    }

    public FieldFacet parseFieldFacet(FieldFacetDefinition facetDefinition, Document document) {
        Element facetCountsElement = getLstChild(document.getDocumentElement(), "facet_counts");
        Element facetFieldsElement = getLstChild(facetCountsElement, "facet_fields");
        String facetId = facetDefinition.getId();
        FieldFacet facet = new FieldFacet(facetId, facetDefinition.getName(), facetDefinition.getFieldName());
        Element facetElement = getLstChild(facetFieldsElement, facetId);
        for (Element entryElement : children(facetElement)) {
            String value = entryElement.getAttribute("name");
            int count = getIntValue(entryElement);
            FacetEntry facetEntry = new FacetEntry(value, count);
            facet.addEntry(facetEntry);
        }
        return facet;
    }

    public DateFacet parseDateFacet(DateFacetDefinition facetDefinition, Document document) {
        Element facetCountsElement = getLstChild(document.getDocumentElement(), "facet_counts");
        Element facetDatesElement = getLstChild(facetCountsElement, "facet_dates");

        String facetId = facetDefinition.getId();
        DateFacet facet = new DateFacet(facetId, facetDefinition.getName(), facetDefinition.getFieldName());
        Element facetElement = getLstChild(facetDatesElement, facetId);
        for (Element entryElement : children(facetElement)) {
            String name = entryElement.getAttribute("name");
            if ("gap".equals(name) || "end".equals(name)) {
                continue;
            }
            int count = getIntValue(entryElement);
            FacetEntry facetEntry = new FacetEntry(name, count);
            facet.addEntry(facetEntry);
        }

        return facet;
    }

}
