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

package org.apache.solr.explorer.client.plugin.facet.model.context;

/**
 * Defines a query facet.
 *
 * @author Uri Boness
 */
public class QueryFacetDefinition extends FacetDefinition {

    private String query;

    public QueryFacetDefinition() {
        this(null, null);
    }

    /**
     * Constructs a new query facet where the query also serves as the name of the facet.
     *
     * @param name the name of the facet
     * @param query The query.
     */
    public QueryFacetDefinition(String name, String query) {
        super(name);
        this.query = query;
    }

    /**
     * Sets the query for this facet.
     *
     * @param query The query for this facet.
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Returns the query of this facet.
     *
     * @return The query of this facet.
     */
    public String getQuery() {
        return query;
    }

}
