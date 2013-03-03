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

package org.apache.solr.explorer.client.plugin.highlight.model.result;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Uri Boness
 */
public class HighlightingResult {

    private Map<String, Map<String, String>> highlightedFieldsByDocId = new HashMap<String, Map<String, String>>();

    public void addHighlightedField(String docId, String fieldName, String text) {
        Map<String, String> textByField = highlightedFieldsByDocId.get(docId);
        if (textByField == null) {
            textByField = new HashMap<String, String>();
            highlightedFieldsByDocId.put(docId, textByField);
        }
        textByField.put(fieldName, text);
    }

    public boolean hasHighlightedText(String docId, String fieldName) {
        Map<String, String> textByField = highlightedFieldsByDocId.get(docId);
        if (textByField == null) {
            return false;
        }
        return textByField.containsKey(fieldName);
    }

    public String getHighlightedText(String docId, String fieldName) {
        Map<String, String> textByField = highlightedFieldsByDocId.get(docId);
        if (textByField == null) {
            return null;
        }
        return textByField.get(fieldName);
    }

}
