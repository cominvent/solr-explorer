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

import org.apache.solr.explorer.client.core.model.context.SearchContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a solr search result.
 *
 * @author Uri Boness
 */
public class SearchResult {

    private SearchContext searchContext;
    private long searchTime;
    private String rawText;

    private Map<Class, Object> parts = new HashMap<Class, Object>();

    /**
     * Constructs a new SearchResult with the time the search took (in milliseconds) and the actual hits.
     *
     * @param searchTime The time it took to search.
     */
    public SearchResult(String rawText, long searchTime, SearchContext searchContext) {
        this.rawText = rawText;
        this.searchTime = searchTime;
        this.searchContext = searchContext;
    }

    /**
     * Returns the time it took to search in milliseconds.
     *
     * @return The time it took to search in milliseconds.
     */
    public long getSearchTime() {
        return searchTime;
    }

    /**
     * Returns the time it took to search in seconds.
     *
     * @return The time it took to search in seconds.
     */
    public double getSearchTimeInSeconds() {
        return ((double)getSearchTime())/1000.0;
    }

    public <T> T getPart(Class<T> clazz) {
      return (T) parts.get(clazz);
    }

    public <T> void setPart(Class<T> clazz, T t) {
      parts.put(clazz, t);
    }

    /**
     * Returns the search context that was used when executing the search of this result.
     *
     * @return The search context that was used when executing the search of this result.
     */
    public SearchContext getSearchContext() {
        return searchContext;
    }

    public String getRawText() {
        return rawText;
    }
}
