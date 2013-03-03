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
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.event.DefaultApplicationEventMulticaster;

/**
 * The multicaster for this application. Optimized to reuse event (creating object in javascript is too expensive).
 *
 * @author Uri Boness
 */
@Component
public class Multicaster extends DefaultApplicationEventMulticaster {

    private SearchResultUpdatedEvent searchResultUpdatedEvent = new SearchResultUpdatedEvent();
    private NewSearchEvent newSearchEvent = new NewSearchEvent();

    public void notifySearchResultUpdated(Object source, SearchResult result) {
        searchResultUpdatedEvent.reset(source, result);
        notifyEvent(searchResultUpdatedEvent);
    }

    public void notifyNewSearch(Object source) {
        newSearchEvent.setSource(source);
        notifyEvent(newSearchEvent);
    }

}
