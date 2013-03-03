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

package org.apache.solr.explorer.client.plugin.facet.model.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a field facet as returned by a field facet query. It holds a list of
 * {@link org.apache.solr.explorer.client.plugin.facet.model.result.FacetEntry facet entries} where each entry represents a value of the
 * facet and its associated count. The name of the facet corresponds to the field name.
 *
 * @author Uri Boness
 */
public class DateFacet extends Facet {

    private final String fieldName;

    private final List<FacetEntry> entries;

    /**
     * Constructs a new DateFacet with a given name.
     *
     * @param name The name of this facet.
     * @param fieldName The name of the field of this facet
     */
    public DateFacet(String id, String name, String fieldName) {
        super(id, name);
        this.fieldName = fieldName;
        entries = new ArrayList<FacetEntry>();
    }

    /**
     * Returns the name of the field of this facet.
     *
     * @return The name of the field of this facet.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Returns the entries of this facet.
     *
     * @return The entries of this facet.
     */
    public List<FacetEntry> getEntries() {
        return entries;
    }

    /**
     * Adds the given entry to this facet.
     *
     * @param entry The entry to be added to this facet.
     */
    public void addEntry(FacetEntry entry) {
        entries.add(entry);
    }

}