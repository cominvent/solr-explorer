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

package org.apache.solr.explorer.client.core.manager.solr.search;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.core.event.Multicaster;
import org.apache.solr.explorer.client.util.Callback;
import org.gwtoolbox.ioc.core.client.annotation.Inject;

/**
 * A base class for implementations of the {@link SearchManager} interface.
 *
 * @author Uri Boness
 */
public abstract class AbstractSearchManager implements SearchManager {

    private Multicaster multicaster;

    private SearchContext searchContext;

    protected AbstractSearchManager() {
        this(new SearchContext());
    }

    protected AbstractSearchManager(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    public void reexecuteSearch() {
        search(new Callback<SearchResult>() {
            public void onSuccess(SearchResult result) {
                multicaster.notifySearchResultUpdated(AbstractSearchManager.this, result);
            }
        });
    }

    public final void search(AsyncCallback<SearchResult> callback) {
        search(searchContext, callback);
    }

    public SearchContext getCurrentSearchContext() {
        return searchContext;
    }

    //============================================== Setter/Getter =====================================================

    @Inject
    public void setSearchContext(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    public SearchContext getSearchContext() {
        return searchContext;
    }

    @Inject
    public void setMulticaster(Multicaster multicaster) {
        this.multicaster = multicaster;
    }

    public Multicaster getMulticaster() {
        return multicaster;
    }

}
