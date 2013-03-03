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

package org.apache.solr.explorer.client.core.model.configuration.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Uri Boness
 */
public class SortConfig {

    private List<String> sortableFields = new ArrayList<String>();
    private List<SortButtonConfiguration> defaultSortButtons = new ArrayList<SortButtonConfiguration>();

    public SortConfig() {
    }

    public void addSortableField(String fieldName) {
        sortableFields.add(fieldName);
    }

    public void addDefaultSortButton(String name, String fieldName) {
        addDefaultSortButton(new SortButtonConfiguration(name, fieldName));
    }

    public void addDefaultSortButton(SortButtonConfiguration sortButtonConfiguration) {
        defaultSortButtons.add(sortButtonConfiguration);
    }

    public List<String> getSortableFields() {
        return sortableFields;
    }

    public List<SortButtonConfiguration> getDefaultSorts() {
        return defaultSortButtons;
    }
}
