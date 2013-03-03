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

package org.apache.solr.explorer.client.plugin.listview.model.config;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * @author Uri Boness
 */
public class ShowFieldsConfiguration {

    private Set<String> includes;
    private Set<String> excludes;

    public void setIncludes(String[] includes) {
        this.includes = new HashSet<String>(Arrays.asList(includes));
    }

    public void setExcludes(String[] excludes) {
        this.excludes = new HashSet<String>(Arrays.asList(excludes));
    }

    public boolean shouldShow(String fieldName) {
        if (includes != null && !includes.contains(fieldName)) {
            return false;
        }
        return excludes == null || !excludes.contains(fieldName);
    }
}
