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

package org.apache.solr.explorer.client.plugin.terms.model.result;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Uri Boness
 */
public class TermResult {

    private String fieldName;
    private List<TermEntry> entries;

    public TermResult(String fieldName) {
        this(fieldName, new ArrayList<TermEntry>());
    }

    public TermResult(String fieldName, List<TermEntry> entries) {
        this.fieldName = fieldName;
        this.entries = entries;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<TermEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<TermEntry> entries) {
        this.entries = entries;
    }

    public void addEntry(TermEntry entry) {
        entries.add(entry);
    }

    public void removeEntry(TermEntry entry) {
        entries.remove(entry);
    }
}
