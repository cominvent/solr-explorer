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

package org.apache.solr.explorer.client.core.ui.resultspane;

import org.apache.solr.explorer.client.util.ui.widget.FormDialog;
import org.apache.solr.explorer.client.util.ui.widget.editor.DefaultOption;
import org.apache.solr.explorer.client.util.ui.widget.editor.Option;
import org.apache.solr.explorer.client.util.ui.widget.editor.SingleSelectEditor;
import org.apache.solr.explorer.client.util.ui.widget.editor.TextEditor;
import static org.apache.solr.explorer.client.util.ui.widget.validation.Validators.notBlank;
import static org.apache.solr.explorer.client.util.ui.widget.validation.Validators.notNull;
import org.apache.solr.explorer.client.core.manager.solr.admin.Schema;
import org.apache.solr.explorer.client.core.manager.solr.admin.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Uri Boness
 */
public abstract class SortFormDialog extends FormDialog {

    public SortFormDialog(Schema schema, HeaderBar.SortToolBar sortToolbar) {
        super("Add Sort Form", true);
        setWidthPx(300);
        setResizable(true);

        List<Option<String>> options = new ArrayList<Option<String>>();
        for (final Field field : schema.getFields()) {
            if (field.isMultiValue() || !field.isIndexed() || sortToolbar.hasSortField(field.getName())) {
                continue;
            }
            options.add(new DefaultOption<String>(field.getName(), field.getName()));
        }
        if (!sortToolbar.hasSortField("score")) {
            options.add(new DefaultOption<String>("score", "score"));
        }

        addFieldRow("name", "Name", new TextEditor(), notNull(), notBlank());
        addFieldRow("fieldName", "Field Name", new SingleSelectEditor<String>(options));
    }

    protected void doSubmit() {
        String name = getStringValue("name");
        String fieldName = getStringValue("fieldName");
        doSubmit(name, fieldName);
        hide();
    }

    protected abstract void doSubmit(String name, String fieldName);
}
