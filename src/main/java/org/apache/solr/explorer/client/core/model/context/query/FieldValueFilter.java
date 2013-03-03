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

package org.apache.solr.explorer.client.core.model.context.query;

/**
 * @author Uri Boness
 */
public class FieldValueFilter {

    private static long idCounter = 0;

    private final String id;
    private final String name;
    private final String fieldName;
    private final String value;
    private String valueDescription;
    private final boolean raw;

    public FieldValueFilter(String fieldName, String value) {
        this(fieldName, fieldName, value);
    }

    public FieldValueFilter(String name, String fieldName, String value) {
        this(nextId(), name, fieldName, value);
    }

    public FieldValueFilter(String id, String name, String fieldName, String value) {
        this(id, name, fieldName, value, false);
    }

    public FieldValueFilter(String id, String name, String fieldName, String value, boolean raw) {
        this.id = id;
        this.fieldName = fieldName;
        this.name = name;
        this.value = value;
        this.valueDescription = value;
        this.raw = raw;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getValue() {
        return value;
    }

    public String getValueDescription() {
        return valueDescription;
    }

    public boolean isRaw() {
        return raw;
    }

    public void setValueDescription(String valueDescription) {
        this.valueDescription = valueDescription;
    }

    //================================================ Helper Methods ==================================================

    private static String nextId() {
        return String.valueOf(++idCounter);
    }
}
