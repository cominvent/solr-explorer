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

package org.apache.solr.explorer.client.core.ui.consolepane;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import org.apache.solr.explorer.client.core.event.SearchResultUpdatedEvent;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.util.XmlViewPane;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Order;
import org.gwtoolbox.widget.client.event.custom.EnableEvent;
import org.gwtoolbox.widget.client.event.custom.EnableHandler;

/**
 * @author Uri Boness
 */
@Component
@Order(5)
public class RawResultPane extends Composite implements ConsoleTab {

    private XmlViewPane main;

    public void init(SolrCore solrCore) {
    }

    public boolean isActive() {
        return true;
    }

    public RawResultPane() {
        main = new XmlViewPane();
        initWidget(new ScrollPanel(main));
    }

    public String getName() {
        return "Raw XML";
    }

    public Widget getContent() {
        return this;
    }

    public boolean isEnabled() {
        return false;
    }

    public HandlerRegistration addEnableHandler(EnableHandler handler) {
        return addHandler(handler, EnableEvent.getType());
    }

    @EventHandler
    public void handleSearchResultUpdated(SearchResultUpdatedEvent event) {
        EnableEvent.fire(this, true);
        String rawResult = event.getSearchResult().getRawText();
        main.setXmlText(event.getSearchResult().getRawText());
//        main.getElement().setInnerText(rawResult);
    }

}
