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
 * Defines a facet. A facet can be one of three types (field, query, or date) and it has a unique name.
 *
 * @author Uri Boness
 */
public abstract class FacetDefinition {

    private static int idCounter = 0;

    private final String id;
    private String name;
    private boolean fixed;

    /**
     * Default empty constructor
     */
    protected FacetDefinition() {
        this(null);
    }

    /**
     * Constructs a new FacetDefinition with a given name.
     *
     * @param name The name of the facet.
     */
    protected FacetDefinition(String name) {
        this(nextId(), name);
    }

    /**
     * Constructs a new FacetDefinition with a given name and id.
     *
     * @param id The id of this facet.
     * @param name The name of the facet.
     */
    public FacetDefinition(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the id of this facet definition.
     *
     * @return The id of this facet definition.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the facet.
     *
     * @return The name of the facet.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this facet.
     *
     * @param name The name of this facet.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns whether this facet definition is fixed, that is, it cannot be modified/edited by the user.
     *
     * @return true if this facet definition is fixed.
     */
    public boolean isFixed() {
        return fixed;
    }

    /**
     * Determines whether this facet definition is fixed
     *
     * @param fixed indicated whehter this facet definition is fixed
     * @see #isFixed()
     */
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    //================================================ Helper Methods ==================================================

    private static String nextId() {
        return "__facet_" + idCounter++;
    }
}
