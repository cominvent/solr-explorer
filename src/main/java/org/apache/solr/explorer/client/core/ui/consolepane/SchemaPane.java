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

import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import org.apache.solr.explorer.client.core.manager.solr.admin.Field;
import org.apache.solr.explorer.client.core.manager.solr.admin.Schema;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.admin.Type;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Order;
import org.gwtoolbox.widget.client.event.custom.EnableEvent;
import org.gwtoolbox.widget.client.event.custom.EnableHandler;
import org.gwtoolbox.widget.client.panel.layout.SplitLayout;
import org.gwtoolbox.widget.client.table.basic.BasicTable;

import java.util.Map;

/**
 * @author Uri Boness
 */
@Component
@Order(2)
public class SchemaPane extends ResizeComposite implements ConsoleTab {

    private Tree tree;

    private BasicTable typeInfoTable;
    private BasicTable fieldInfoTable;
    private SimplePanel infoPane;

    private TreeItem currentOpenItem;

    private boolean enabled;

    public SchemaPane() {

        SplitLayout main = new SplitLayout();

        tree = new Tree();
        tree.setAnimationEnabled(true);
        tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
            public void onSelection(SelectionEvent<TreeItem> event) {
                TreeItem item = event.getSelectedItem();
                Object userObject = item.getUserObject();
                if (userObject instanceof Field) {
                    handleFieldClicked((Field) userObject);
                    return;
                }

                if (userObject instanceof Type) {
                    handleTypeClicked((Type) userObject);
                    return;
                }

                // otherwise we just clean the infopane
                infoPane.clear();
            }
        });
        tree.addOpenHandler(new OpenHandler<TreeItem>() {
            public void onOpen(OpenEvent<TreeItem> event) {
                TreeItem item = event.getTarget();
                if (item.getState() && currentOpenItem != item) {
                    if (currentOpenItem != null) {
                        currentOpenItem.setState(false, false);
                    }
                    currentOpenItem = item;
                }
            }
        });

        typeInfoTable = new BasicTable();
        typeInfoTable.setHeaderHTML(0, "Name");
        typeInfoTable.setHeaderHTML(1, "Value");
        typeInfoTable.setWidth("400px");
        typeInfoTable.getColumnFormatter().setWidth(0, "100px");

        fieldInfoTable = new BasicTable();
        fieldInfoTable.setHeaderHTML(0, "Name");
        fieldInfoTable.setHeaderHTML(1, "Value");
        fieldInfoTable.setWidth("400px");
        fieldInfoTable.getColumnFormatter().setWidth(0, "200px");

        main.addWest(new ScrollPanel(tree), 300);
        infoPane = new SimplePanel();
        infoPane.setSize("100%", "100%");
        main.add(new ScrollPanel(infoPane));

        initWidget(main);
        addStyleName("SchemaPane");
    }

    public void init(SolrCore solrCore) {
        Schema schema = solrCore.getSchema();
        update(schema);
        enabled = true;
        EnableEvent.fire(this, true);
    }

    public boolean isActive() {
        return true;
    }

    public String getName() {
        return "Schema";
    }

    public Widget getContent() {
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public HandlerRegistration addEnableHandler(EnableHandler handler) {
        return addHandler(handler, EnableEvent.getType());
    }

    //================================================ Helper Methods ==================================================

    private void handleTypeClicked(Type type) {
        typeInfoTable.removeAllRows();
        Map<String, Object> props = type.getProperties();
        int i = 0;
        for (String name : type.getPropertiesNames()) {
            typeInfoTable.setText(i, 0, name);
            typeInfoTable.getCellFormatter().setStyleName(i, 0, "PropertiesTableNameCell");
            typeInfoTable.setText(i, 1, String.valueOf(props.get(name)));
            i++;
        }
        infoPane.setWidget(new ScrollPanel(typeInfoTable));
    }

    private void handleFieldClicked(Field field) {
        fieldInfoTable.removeAllRows();
        Map<String, Object> props = field.getProperties();
        int i = 0;
        for (String name : field.getPropertiesNames()) {
            fieldInfoTable.setText(i, 0, name);
            fieldInfoTable.getCellFormatter().setStyleName(i, 0, "PropertiesTableNameCell");
            fieldInfoTable.setText(i, 1, String.valueOf(props.get(name)));
            i++;
        }
        infoPane.setWidget(new ScrollPanel(fieldInfoTable));
    }

    private void update(Schema schema) {
        infoPane.clear();
        tree.removeItems();

        TreeItem defaultSearchFieldItem = new TreeItem("<b>Default Search Field:</b>&nbsp;" + schema.getDefaultSearchField());
        tree.addItem(defaultSearchFieldItem);

        TreeItem uniqueKeyFieldItem = new TreeItem("<b>Unique Key Field:</b>&nbsp;" + schema.getUniqueKeyField());
        tree.addItem(uniqueKeyFieldItem);

        TreeItem typesItem = new TreeItem("<b>Types</b>");
        for (Type type : schema.getTypes()) {
            TreeItem typeItem = new TreeItem(type.getName());
            typeItem.setUserObject(type);
            typesItem.addItem(typeItem);
        }
        tree.addItem(typesItem);

        TreeItem fieldsItem = new TreeItem("<b>Fields</b>");
        for (Field field : schema.getFields()) {
            TreeItem fieldItem = new TreeItem(field.getName());
            fieldItem.setUserObject(field);
            fieldsItem.addItem(fieldItem);
        }
        tree.addItem(fieldsItem);

    }


}
