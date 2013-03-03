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

import java.util.Set;

/**
 * A field facet definition.
 *
 * @author Uri Boness
 */
public class DateFacetDefinition extends FacetDefinition {

    private String fieldName;

    private String start;

    private String end;

    private DateGap gap;

    private boolean hardEnd;

    private Set<DateOther> others;

    /**
     * Default empty constructor.
     */
    public DateFacetDefinition() {
        this(null);
    }

    /**
     * Constructs a new FieldFacetDefinition without a name.
     *
     * @param fieldName The field name of the facet.
     */
    public DateFacetDefinition(String fieldName) {
        this(fieldName, fieldName);
    }

    /**
     * Constructs a new FieldFacetDefinition with a given field name.
     *
     * @param name The name of the facet
     * @param fieldName The name of the field.
     */
    public DateFacetDefinition(String name, String fieldName) {
        super(name);
        this.fieldName = fieldName;
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
     * Sets the field name of this facet.
     *
     * @param fieldName The field name of this facet.
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public DateGap getGap() {
        return gap;
    }

    public void setGap(DateGap gap) {
        this.gap = gap;
    }

    public boolean isHardEnd() {
        return hardEnd;
    }

    public void setHardEnd(boolean hardEnd) {
        this.hardEnd = hardEnd;
    }

    public Set<DateOther> getOthers() {
        return others;
    }

    public void setOthers(Set<DateOther> others) {
        this.others = others;
    }
}