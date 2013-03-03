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

package org.apache.solr.explorer.client.core.model.result;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple representation of the found items (hits). Basically this is just a map implementation where the entries are
 * represent the document fields.
 *
 * @author Uri Boness
 */
public class Hit extends HashMap<String, Object> {

    private float score;

    /**
     * @deprecated with the new plugin architecure, there is no longer a need to store the highlighted text separately.
     * Instead, the highlighting plugin just replaces the field values with the highlighted values.
     */
    private Map<String, String> highlightedFields = new HashMap<String, String>();

    /**
     * Returns the score of this hit.
     *
     * @return The score of this hit.
     */
    public float getScore() {
        return score;
    }

    /**
     * Sets the score of this hit.
     *
     * @param score The score of this hit.
     */
    public void setScore(float score) {
        this.score = score;
    }

    /**
     * @deprecated with the new plugin architecure, there is no longer a need to store the highlighted text separately.
     * Instead, the highlighting plugin just replaces the field values with the highlighted values.
     */
    public boolean isHighlighted(String fieldName) {
        return highlightedFields.containsKey(fieldName);
    }

    /**
     * @deprecated with the new plugin architecure, there is no longer a need to store the highlighted text separately.
     * Instead, the highlighting plugin just replaces the field values with the highlighted values.
     */
    public String getHighlightedText(String fieldName) {
        return highlightedFields.get(fieldName);
    }

    /**
     * @deprecated with the new plugin architecure, there is no longer a need to store the highlighted text separately.
     * Instead, the highlighting plugin just replaces the field values with the highlighted values.
     */
    public void setHighlightedText(String fieldName, String text) {
        highlightedFields.put(fieldName, text);
    }

}
