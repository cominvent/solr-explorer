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

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HTML;
import org.apache.solr.explorer.client.core.model.result.Hit;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.core.manager.solr.admin.Field;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.admin.Schema;
import org.apache.solr.explorer.client.plugin.listview.model.config.ListViewConfig;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import static org.gwtoolbox.widget.client.panel.LayoutUtils.hBuilder;

/**
 * @author Uri Boness
 */
@Component
public class FieldsHitBoxTab implements HitBoxTab {

    private SolrCore solrCore;

    public String getName() {
        return "Fields";
    }

    public void init(SolrCore solrCore) {
        this.solrCore = solrCore;
    }

    public boolean isActive() {
        return true;
    }

    public boolean isEnabled(SearchResult result, Hit hit) {
        return true;
    }

    public Widget createWidget(SearchResult result, Hit hit) {
        ListViewConfig config = solrCore.getConfiguration().getConfig(ListViewConfig.class);
        Schema schema = solrCore.getSchema();
        final VerticalPanel fieldsPanel = new VerticalPanel();
        fieldsPanel.setStyleName("FieldsPanel");
        for (Field field : schema.getFields()) {
            String fieldName = field.getName();
            if (field.isStored() &&
                    !fieldName.equals(config.getTitleFieldName()) &&
                    !fieldName.equals(config.getSummaryFieldName()) &&
                    !fieldName.equals(config.getUrlFieldName()) &&
                    config.shouldShowField(fieldName)) {

                Label nameLabel = new Label(fieldName + ":");
                nameLabel.setStylePrimaryName("NameLabel");
                Object value = hit.get(fieldName);
                String valueHtml = (value != null) ? String.valueOf(value) : "";
                HTML valueLabel = new HTML(valueHtml);
                valueLabel.setStylePrimaryName("ValueLabel");
                fieldsPanel.add(hBuilder().add(nameLabel).addGap("3px").add(valueLabel).getPanel());
            }
        }
        Label nameLabel = new Label("score:");
        nameLabel.setStylePrimaryName("NameLabel");
        Object score = hit.get("score");
        String scoreValue = (score == null) ? "" : String.valueOf(score);
        Label valueLabel = new Label(scoreValue);
        valueLabel.setStylePrimaryName("ValueLabel");
        fieldsPanel.add(hBuilder().add(nameLabel).addGap("3px").add(valueLabel).getPanel());
        fieldsPanel.setWidth("100%");

        return fieldsPanel;
    }
}
