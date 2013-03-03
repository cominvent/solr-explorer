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

package org.apache.solr.explorer.client.core.event;

import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.core.model.result.Hit;
import org.gwtoolbox.ioc.core.client.event.AbstractApplicationEvent;
import org.gwtoolbox.commons.collections.client.Page;

/**
 * Fired whenever a new search result was reuturned from the server.
 *
 * @author Uri Boness
 */
public class SearchResultUpdatedEvent extends AbstractApplicationEvent {

    private SearchResult searchResult;

    /**
     * Constructs a new SearchResultUpdatedEvent.
     */
    public SearchResultUpdatedEvent() {
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        Page<Hit> hits = searchResult.getPart(Page.class);
        return "Found " + hits.getTotalCount() +
            " items (took " + searchResult.getSearchTimeInSeconds() + " sec.)";
    }

    /**
     * Resets this event wiht new source and search result. Visible only to this package (ie. to the {@link Multicaster}).
     *
     * @param source The source of this event.
     * @param searchResult The updated search result.
     */
    void reset(Object source, SearchResult searchResult) {
        setSource(source);
        this.searchResult = searchResult;
    }

    /**
     * Returns the new search result.
     *
     * @return The new search result.
     */
    public SearchResult getSearchResult() {
        return searchResult;
    }

}
