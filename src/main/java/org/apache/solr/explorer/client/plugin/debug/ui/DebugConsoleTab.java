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

package org.apache.solr.explorer.client.plugin.debug.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import org.apache.solr.explorer.client.core.event.SearchResultUpdatedEvent;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.core.ui.consolepane.ConsoleTab;
import org.apache.solr.explorer.client.plugin.debug.model.config.DebugConfig;
import org.apache.solr.explorer.client.plugin.debug.model.context.DebugContext;
import org.apache.solr.explorer.client.plugin.debug.model.result.DebugResult;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.ioc.core.client.annotation.Order;
import org.gwtoolbox.widget.client.event.custom.EnableEvent;
import org.gwtoolbox.widget.client.event.custom.EnableHandler;
import org.gwtoolbox.widget.client.table.basic.BasicTable;

import java.util.Map;

/**
 * @author Uri Boness
 */
@Component
@Order(10)
public class DebugConsoleTab extends Composite implements ConsoleTab {

    private boolean active;

    private DebugContext debugContext;

    private Label rawQueryStringLabel;
    private Label queryStringLabel;
    private Label parsedQueryStringLabel;
    private Label parsedQueryStringToStringLabel;
    private Label qParserLabel;

    private HTML timingRootLabel;
    private TreeItem prepareItem;
    private TreeItem processItem;

    public DebugConsoleTab() {
        FlowPanel main = new FlowPanel();

        BasicTable table = new BasicTable();
        table.setHeaderHTML(0, "Name");
        table.setHeaderHTML(1, "Value");
        table.setText(0, 0, "Raw Query");
        table.setWidet(0, 1, rawQueryStringLabel = new Label());
        table.setText(1, 0, "Query");
        table.setWidet(1, 1, queryStringLabel = new Label());
        table.setText(2, 0, "Parsed Query");
        table.setWidet(2, 1, parsedQueryStringLabel = new Label());
        table.setText(3, 0, "Parsed Query (ToString)");
        table.setWidet(3, 1, parsedQueryStringToStringLabel = new Label());
        table.setText(4, 0, "Query Parser");
        table.setWidet(4, 1, qParserLabel = new Label());

        table.getColumnFormatter().setWidth(0, "200px");
        table.getCellFormatter().setStyleName(0, 0, "PropertiesTableNameCell");
        table.getCellFormatter().setStyleName(1, 0, "PropertiesTableNameCell");
        table.getCellFormatter().setStyleName(2, 0, "PropertiesTableNameCell");
        table.getCellFormatter().setStyleName(3, 0, "PropertiesTableNameCell");
        table.getCellFormatter().setStyleName(4, 0, "PropertiesTableNameCell");
        main.add(table);


        HTML gap = new HTML();
        gap.setSize("10px", "10px");
        main.add(gap);

        timingRootLabel = new HTML();
        TreeItem root = new TreeItem(timingRootLabel);

        prepareItem = new TreeItem("Prepare");
        root.addItem(prepareItem);

        processItem = new TreeItem("Process");
        root.addItem(processItem);

        Tree tree = new Tree();
        tree.setAnimationEnabled(true);
        tree.addItem(root);
        main.add(tree);

        gap = new HTML();
        gap.setSize("10px", "10px");
        main.add(gap);

        ScrollPanel sp = new ScrollPanel(main);
        sp.setSize("100%", "100%");

        initWidget(sp);
    }

    @EventHandler
    public void handleSearchResultUpdated(SearchResultUpdatedEvent event) {
        if (!active) {
            return;
        }

        SearchResult result = event.getSearchResult();
        DebugResult debugResult = result.getPart(DebugResult.class);

        if (debugResult == null) {
            EnableEvent.fire(this, false);
            return;
        }

        EnableEvent.fire(this, true);

        String value = debugResult.getRawQueryString();
        rawQueryStringLabel.setText(value == null ? "" : value);
        value = debugResult.getQueryString();
        queryStringLabel.setText(value == null ? "" : value);
        value = debugResult.getParsedQueryString();
        parsedQueryStringLabel.setText(value == null ? "" : value);
        value = debugResult.getParsedQueryStringToString();
        parsedQueryStringToStringLabel.setText(value == null ? "" : value);
        value = debugResult.getQParser();
        qParserLabel.setText(value == null ? "" : value);

        timingRootLabel.setHTML("<b>Timing (" + debugResult.getTiming().getTime() + ")</b>");

        prepareItem.setHTML("Prepare (" + debugResult.getTiming().getPreparePhase().getTime() + ")");
        prepareItem.removeItems();
        for (Map.Entry<String, Double> entry : debugResult.getTiming().getPreparePhase().getTimeByComponent().entrySet()) {
            prepareItem.addItem(entry.getKey() + ": " + String.valueOf(entry.getValue()));
        }

        processItem.setHTML("Process (" + debugResult.getTiming().getProcessPhase().getTime() + ")");
        processItem.removeItems();
        for (Map.Entry<String, Double> entry : debugResult.getTiming().getProcessPhase().getTimeByComponent().entrySet()) {
            processItem.addItem(entry.getKey() + ": " + String.valueOf(entry.getValue()));
        }
    }

    public void init(SolrCore solrCore) {
        active = solrCore.getConfiguration().isActive(DebugConfig.class);
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return "Debug";
    }

    public Widget getContent() {
        return this;
    }

    public boolean isEnabled() {
        return debugContext.isDebugQuery();
    }

    public HandlerRegistration addEnableHandler(EnableHandler handler) {
        return addHandler(handler, EnableEvent.getType());
    }


    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setDebugContext(DebugContext debugContext) {
        this.debugContext = debugContext;
    }
}
