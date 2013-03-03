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

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a field facet as returned by a field facet query. It holds a list of
 * {@link FacetEntry facet entries} where each entry represents a value of the
 * facet and its associated count. The name of the facet corresponds to the field name.
 *
 * @author Uri Boness
 */
public class FieldFacet extends Facet {

    private final static String DEFAULT_MISSING_ENTRY_NAME = "<undefined>";

    private final String fieldName;

    private final List<FacetEntry> entries;

    private String missingEntryName;

    /**
     * Constructs a new FieldFacet with a given name.
     *
     * @param name The name of this facet.
     * @param fieldName The name of the field of this facet
     */
    public FieldFacet(String id, String name, String fieldName) {
        this(id, name, fieldName, DEFAULT_MISSING_ENTRY_NAME);
    }

    /**
     * Constructs a new FieldFacet with a given name.
     *
     * @param name The name of this facet.
     * @param fieldName The name of the field of this facet
     * @param missingEntryName The name to be used for the missing entry count.
     */
    public FieldFacet(String id, String name, String fieldName, String missingEntryName) {
        super(id, name);
        this.fieldName = fieldName;
        this.missingEntryName = missingEntryName;
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

    /**
     * Returns the name that should be used for the missing facet entry. The missing facet entry represents the count of
     * all hits which this facet does not apply to.
     *
     * @return The name that should be used for the missing facet entry.
     */
    public String getMissingEntryName() {
        return missingEntryName;
    }

    /**
     * Sets the name that should be used for the missing facet entry.
     *
     * @param missingEntryName The name that should be used for the missing facet entry.
     */
    public void setMissingEntryName(String missingEntryName) {
        this.missingEntryName = missingEntryName;
    }
}
