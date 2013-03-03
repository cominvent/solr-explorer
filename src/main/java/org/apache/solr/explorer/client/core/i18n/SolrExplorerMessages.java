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

package org.apache.solr.explorer.client.core.i18n;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.core.client.GWT;

/**
 * @author Uri Boness
 */
public interface SolrExplorerMessages extends Messages {

    String FieldFacetDefinition_name();
    String FieldFacetDefinition_sort();
    String FieldFacetDefinition_minCount();
    String FieldFacetDefinition_missing();
    String FieldFacetDefinition_limit();

    //================================================= Accessor =======================================================

    public static class Instance {
        private static SolrExplorerMessages instance;

        public static SolrExplorerMessages get() {
            if (instance == null) {
                instance = (SolrExplorerMessages) GWT.create(SolrExplorerMessages.class);
            }
            return instance;
        }

    }
}
