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

package org.apache.solr.explorer.client.core.ui.consolepane.urlparameters;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import org.apache.solr.explorer.client.core.event.SearchResultUpdatedEvent;
import org.apache.solr.explorer.client.core.manager.RequestParams;
import org.apache.solr.explorer.client.core.manager.solr.ParamsSource;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.model.context.Context;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.ui.consolepane.ConsoleTab;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.ioc.core.client.annotation.Order;
import org.gwtoolbox.widget.client.event.custom.EnableEvent;
import org.gwtoolbox.widget.client.event.custom.EnableHandler;
import org.gwtoolbox.widget.client.panel.layout.tab.TabLayout;

import java.util.List;
import java.util.Map;

import static org.gwtoolbox.widget.client.panel.LayoutUtils.hBuilder;

/**
 * @author Uri Boness
 */
@Component
@Order(4)
public class URLParametersPane extends Composite implements ConsoleTab, RequiresResize {

    private TabLayout tabs;

    private List<ParamsSource> paramsSources;

    public URLParametersPane() {

        LayoutPanel main = new LayoutPanel();

        FlowPanel top = new FlowPanel();
        main.add(top);

        tabs = new TabLayout();
        tabs.setWidth("100%");
        tabs.addStyleName("ParametersTabs");
        main.add(tabs);

        top.setWidth("100%");
        DOM.setStyleAttribute(top.getElement(), "padding", "5px");

        HTML gap = new HTML();
        gap.setSize("10px", "10px");
        top.add(gap);

        main.setWidgetTopHeight(top, 0, Style.Unit.PX, 8, Style.Unit.EM);
        main.setWidgetTopBottom(tabs, 9, Style.Unit.EM, 0, Style.Unit.PX);

        initWidget(main);
        setStyleName("URLDebugPanel");
    }

    public void init(SolrCore solrCore) {
        // do nothing
    }

    public boolean isActive() {
        return true;
    }

    public String getName() {
        return "URL Parameters";
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
        SearchContext context = event.getSearchResult().getSearchContext();
        update(context);
    }

    public void onResize() {
        tabs.onResize();
    }

    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setParamsSources(List<ParamsSource> paramsSources) {
        this.paramsSources = paramsSources;
    }

    //============================================== Helper Methods ====================================================

    private void update(SearchContext context) {

        tabs.clear();
        String firstTabId = null;

        for (ParamsSource source : paramsSources) {
            if (source.isActive()) {
                RequestParams params = new RequestParams();
                Context subContext = context.getContext(source.getContextClass());
                source.addParams(params, subContext);
                addParametersTab(source.getName(), params);
                if (firstTabId == null) {
                    firstTabId = source.getName();
                }
            }
        }
        if (firstTabId != null) {
            tabs.setSelectedTab(firstTabId);
        }
    }

    private void addParametersTab(String name, RequestParams params) {
        FlowPanel v = new FlowPanel();
        DOM.setStyleAttribute(v.getElement(), "margin", "5px");
        int index = 0;
        for (Map.Entry<String, List<String>> entry : params.getParams().entrySet()) {
            String paramName = entry.getKey();
            for (String value : entry.getValue()) {
                Label nameLabel = new Label(paramName);
                nameLabel.setStyleName("NameLabel");
                Label valueLabel = new Label(value);
                Label equalsLabel = new Label("=");
                equalsLabel.setStyleName("EqualsLabel");
                valueLabel.setStyleName("ValueLabel");
                HorizontalPanel h = hBuilder().add(nameLabel).add(equalsLabel).add(valueLabel).getPanel();
                SimplePanel row = new SimplePanel();
                row.setWidget(h);
                row.setWidth("100%");
                row.setStyleName("ParameterRow");
                if (index % 2 != 0) {
                    row.addStyleDependentName("odd");
                }
                v.add(row);
                index++;
            }
        }

        v.setSize("100%", "100%");

        tabs.addTab(name, new ScrollPanel(v));
    }

}
