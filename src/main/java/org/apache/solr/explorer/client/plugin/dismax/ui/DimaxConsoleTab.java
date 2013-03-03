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

package org.apache.solr.explorer.client.plugin.dismax.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.ui.consolepane.ConsoleTab;
import org.apache.solr.explorer.client.plugin.dismax.model.DismaxConfig;
import org.apache.solr.explorer.client.plugin.dismax.model.FieldWeightConfig;
import org.apache.solr.explorer.client.plugin.dismax.model.FunctionWeightConfig;
import org.apache.solr.explorer.client.util.ValueSource;
import org.gwtoolbox.widget.client.event.custom.EnableEvent;
import org.gwtoolbox.widget.client.event.custom.EnableHandler;

import java.util.Map;

/**
 * @author Uri Boness
 */
public class DimaxConsoleTab extends Composite implements ConsoleTab {

    private boolean active;

    private FlowPanel main;

    private Map<String, FunctionValuesSource> valueSourcesByFunction;
    private Map<String, ValueSource> valueSourceByField;

    public DimaxConsoleTab() {
        main = new FlowPanel();
        initWidget(main);
    }

    public String getName() {
        return "Tuning";
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

    public void init(SolrCore solrCore) {
        active = solrCore.getConfiguration().isActive(DismaxConfig.class);
        DismaxConfig  config = solrCore.getConfiguration().getConfig(DismaxConfig.class);
        resetUI(config);
    }

    public boolean isActive() {
        return active;
    }


    //================================================ Helper Methods ==================================================

    private void resetUI(DismaxConfig config) {
        main.clear();
        valueSourceByField.clear();
        valueSourcesByFunction.clear();
        for (FieldWeightConfig fieldWeight : config.getFieldWeights()) {
            main.add(createFieldWeightRow(fieldWeight));
        }
        for (FunctionWeightConfig functionWeight : config.getFunctionWeights()) {
            main.add(createFunctionWeightRow(functionWeight));
        }
    }

    private Widget createFieldWeightRow(FieldWeightConfig fieldWeight) {
        FlowPanel row = new FlowPanel();

        Label label = new Label(fieldWeight.getName() + ":");
        label.getElement().getStyle().setFloat(Style.Float.LEFT);
        label.getElement().getStyle().setDisplay(Style.Display.INLINE);
        row.add(label);

        TextBox box = new TextBox();
        box.setWidth("30px");
        box.getElement().getStyle().setFloat(Style.Float.LEFT);
        box.getElement().getStyle().setDisplay(Style.Display.INLINE);
        row.add(box);

        valueSourceByField.put(fieldWeight.getFieldName(), new TextBoxValueSource(box));

        return row;
    }

    private Widget createFunctionWeightRow(FunctionWeightConfig functionWeight) {
        return null; // TODO implement
    }


    //================================================= Inner Classes ==================================================

    private static class FunctionValuesSource {

//        public float getWeight() {
//
//        }
//
//        public String getParameterValue(String paramName) {
//
//        }

    }

    private static class TextBoxValueSource implements ValueSource {

        private final TextBox box;

        private TextBoxValueSource(TextBox box) {
            this.box = box;
        }

        public String getValue() {
            return box.getText();
        }
    }
}
