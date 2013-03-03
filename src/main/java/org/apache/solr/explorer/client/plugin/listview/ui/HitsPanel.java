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

package org.apache.solr.explorer.client.plugin.listview.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.apache.solr.explorer.client.core.event.SearchResultUpdatedEvent;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.model.result.Hit;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.core.ui.resultspane.FilterableHitsView;
import org.gwtoolbox.commons.collections.client.Page;
import org.gwtoolbox.commons.predicate.client.Predicate;
import org.gwtoolbox.commons.predicate.client.Predicates;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.ioc.core.client.annotation.InjectionType;

import java.util.List;

/**
 * @author Uri Boness
 */
@Component
public class HitsPanel extends Composite implements FilterableHitsView {

    private VerticalPanel main;

    private List<HitBoxTab> hitBoxTabs;
    private List<HitBoxLink> hitBoxLinks;

    private SolrCore solrCore;

    private SearchResult searchResult;

    private Predicate<Hit> filterPredicate = Predicates.All_TRUE;

    public HitsPanel() {
        main = new VerticalPanel();
        initWidget(main);
        setStyleName("HitsPanel");
    }

    @EventHandler
    public void handle(SearchResultUpdatedEvent event) {
        this.searchResult = event.getSearchResult();
        reset(searchResult);
    }

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        solrCore = event.getSolrCore();
        main.clear();
    }

    public void filter(Predicate<Hit> predicate) {
        filterPredicate = predicate;
        reset(searchResult);
    }

    public void clearFilter() {
        filter(Predicates.All_TRUE);
    }

    public void clearFilters() {
        reset(searchResult);
    }

    //================================================ Helper Methods ==================================================

    private void reset(SearchResult result) {
        main.clear();
        Page<Hit> hits = searchResult.getPart(Page.class);
        for (final Hit hit : hits) {
            if (filterPredicate.eval(hit)) {
                HitBox hitBox = new HitBox(searchResult, hit, solrCore, hitBoxTabs, hitBoxLinks);
                main.add(hitBox);
            }
        }
    }


    //============================================== Setter/Getter =====================================================

    @Inject(by = InjectionType.TYPE)
    public void setHitBoxTabs(List<HitBoxTab> hitBoxTabs) {
        this.hitBoxTabs = hitBoxTabs;
    }

    @Inject(by = InjectionType.TYPE)
    public void setHitBoxLinks(List<HitBoxLink> hitBoxLinks) {
        this.hitBoxLinks = hitBoxLinks;
    }
}
