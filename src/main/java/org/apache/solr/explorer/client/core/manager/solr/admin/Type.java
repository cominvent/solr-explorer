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

import java.util.Map;
import java.util.HashMap;

/**
 * @author Uri Boness
 */
public class Type {

    private String name;
    private String className;
    private boolean tokenized;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isTokenized() {
        return tokenized;
    }

    public void setTokenized(boolean tokenized) {
        this.tokenized = tokenized;
    }

    public String[] getPropertiesNames() {
        return new String[] { "Name", "Class", "Tokenized" };
    }

    public Map<String, Object> getProperties() {
        return new HashMap<String, Object>() {{
            put("Name", name);
            put("Class", className);
            put("Tokenized", tokenized);
        }};
    }

    public boolean isDateField() {
        return className.endsWith("DateField") || className.endsWith("TrieDateField");
    }

    public boolean isText() {
        return className.contains("TextField") || className.contains("StrField");
    }
}
