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

package org.apache.solr.explorer.client.core.ui.consolepane.searchcontext;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import org.apache.solr.explorer.client.core.event.SearchResultUpdatedEvent;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.ui.consolepane.ConsoleTab;
import org.gwtoolbox.ioc.core.client.annotation.*;
import org.gwtoolbox.widget.client.event.custom.EnableEvent;
import org.gwtoolbox.widget.client.event.custom.EnableHandler;

import java.util.List;

/**
 * @author Uri Boness
 */
@Component
@Order(3)
public class SearchContextPane extends Composite implements ConsoleTab {

    private FlowPanel main;

    private List<SearchContextComponent> components;

    public SearchContextPane() {
        main = new FlowPanel();
        initWidget(new ScrollPanel(main));
        setStylePrimaryName("SearchContextPane");
    }

    @EventHandler
    public void handleSearchResultUpdated(final SearchResultUpdatedEvent event) {
        // deferring the update here so all other UI elements will be updated first. The debug inforation is the least
        // important and most likely to be hidden for 80% of the time.
        event.registerPostEventCommand(new Command() {
            public void execute() {
                SearchContext context = event.getSearchResult().getSearchContext();
                update(context);
            }
        });
    }

    @EventHandler
    public void handleSolrCoreChanged(final SolrCoreChangedEvent event) {
        event.registerPostEventCommand(new Command() {
            public void execute() {
                main.clear();
                for (SearchContextComponent component : components) {
                    if (component.isActive()) {
                        main.add(component.getWidget());
                    }
                }
            }
        });
    }

    public void init(SolrCore solrCore) {
        // do nothing
    }

    public boolean isActive() {
        return true;
    }

    public String getName() {
        return "Search Context";
    }

    public Widget getContent() {
        return this;
    }

    public boolean isEnabled() {
        return true;
    }

    public HandlerRegistration addEnableHandler(EnableHandler handler) {
        return addHandler(handler, EnableEvent.getType());
    }

    //============================================ Setter/Getter Methods ===============================================

    @Inject(by = InjectionType.TYPE)
    public void setComponents(List<SearchContextComponent> components) {
        this.components = components;
    }

    //================================================ Helper Methods ==================================================

    private void update(SearchContext searchContext) {
        for (SearchContextComponent component : components) {
            component.update(searchContext);
        }
    }

}
