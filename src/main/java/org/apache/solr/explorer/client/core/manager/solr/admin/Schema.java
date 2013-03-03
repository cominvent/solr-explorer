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

package org.apache.solr.explorer.client.core.manager.solr.admin;

import java.util.*;

/**
 * @author Uri Boness
 */
public class Schema {

    private Map<String, Field> fieldByName = new HashMap<String, Field>();
    private Map<String, Type> typeByName = new HashMap<String, Type>();
    private String defaultSearchField;
    private String uniqueKeyField;

    public Collection<Field> getFields() {
        return fieldByName.values();
    }

    public void addFields(Field... fields) {
        for (Field field : fields) {
            fieldByName.put(field.getName(), field);
        }
    }

    public Field getField(String fieldName) {
        return fieldByName.get(fieldName);
    }

    public Collection<Type> getTypes() {
        return typeByName.values();
    }

    public void addTypes(Type... types) {
        for (Type type : types) {
            typeByName.put(type.getName(), type);
        }
    }

    public Type getType(String typeName) {
        return typeByName.get(typeName);
    }

    public String getDefaultSearchField() {
        return defaultSearchField;
    }

    public void setDefaultSearchField(String defaultSearchField) {
        this.defaultSearchField = defaultSearchField;
    }

    public String getUniqueKeyField() {
        return uniqueKeyField;
    }

    public void setUniqueKeyField(String uniqueKeyField) {
        this.uniqueKeyField = uniqueKeyField;
    }

    public boolean hasField(String field) {
        return fieldByName.containsKey(field);
    }
}
