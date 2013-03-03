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

/**
 * A facet entry which holds a unique field value for the field facet and a count to represent how many documents hold
 * this value.
 *
 * @author Uri Boness
 */
public class FacetEntry {

    private final String value;
    private final int count;

    /**
     * Constructs a new FacetValue with a given value and a count.
     *
     * @param value The value.
     * @param count The count.
     */
    public FacetEntry(String value, int count) {
        this.value = value;
        this.count = count;
    }

    /**
     * Returns the value of this entry.
     *
     * @return The value of this entry.
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns the count of this entry. This count represents how many document hold the associated value for the
     * appropriate field.
     *
     * @return The count of this entry.
     */
    public int getCount() {
        return count;
    }

}
