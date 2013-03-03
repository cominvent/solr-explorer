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

package org.apache.solr.explorer.client.plugin.facet.ui.date;

import org.apache.solr.explorer.client.core.manager.solr.admin.Field;
import org.apache.solr.explorer.client.core.manager.solr.admin.Schema;
import org.apache.solr.explorer.client.plugin.facet.model.context.DateFacetDefinition;
import org.apache.solr.explorer.client.plugin.facet.model.context.DateGap;
import org.apache.solr.explorer.client.util.SolrDateUtils;
import org.apache.solr.explorer.client.util.ui.widget.FormDialog;
import org.apache.solr.explorer.client.util.ui.widget.editor.DefaultOption;
import org.apache.solr.explorer.client.util.ui.widget.editor.Option;
import org.apache.solr.explorer.client.util.ui.widget.editor.SingleSelectEditor;
import org.apache.solr.explorer.client.util.ui.widget.editor.TextEditor;
import org.apache.solr.explorer.client.util.ui.widget.validation.AbstractValidator;
import org.apache.solr.explorer.client.util.ui.widget.validation.ValidationResult;
import org.apache.solr.explorer.client.util.ui.widget.validation.Validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.solr.explorer.client.util.ui.widget.validation.Validators.notBlank;
import static org.apache.solr.explorer.client.util.ui.widget.validation.Validators.notNull;

/**
 * @author Uri Boness
 */
public abstract class DateFacetDialog extends FormDialog {

    private final static Validator<String> solrDateValidator = new SolrDateValidator();

    private final DateFacetDefinition definition;

    public DateFacetDialog(Schema schema) {
        this(schema, new DateFacetDefinition());
    }

    protected DateFacetDialog(Schema schema, boolean autoHide) {
        this(schema, new DateFacetDefinition(), autoHide);
    }

    protected DateFacetDialog(Schema schema, boolean autoHide, boolean modal) {
        this(schema, new DateFacetDefinition(), autoHide, modal);
    }

    public DateFacetDialog(Schema schema, DateFacetDefinition definition) {
        this(schema, definition, false);
    }

    public DateFacetDialog(Schema schema, DateFacetDefinition definition, boolean autoHide) {
        this(schema, definition, autoHide, !autoHide);
    }

    public DateFacetDialog(final Schema schema, DateFacetDefinition definition, boolean autoHide, boolean modal) {

        super("Facet Field Dialog", autoHide, modal);

        this.definition = definition;

        List<Option<String>> options = new ArrayList<Option<String>>();
        Collection<Field> fields = schema.getFields();
        for (final Field field : fields) {
            if (field.getType().isDateField()) {
                options.add(new DefaultOption<String>(field.getName(), field.getName()));
            }
        }
        SingleSelectEditor<String> fieldEditor = new SingleSelectEditor<String>(options);
        if (definition.getFieldName() != null) {
            fieldEditor.setValue(definition.getFieldName());
        }
        addFieldRow("name", "Name", new TextEditor(definition.getName(), definition.getName() == null), notNull(), notBlank());
        addFieldRow("field", "Field", fieldEditor);
        addFieldRow("start", "Start Date", new SolrDateEditor(definition.getStart()), notNull(), notBlank(), solrDateValidator);
        addFieldRow("end", "End Date", new SolrDateEditor(definition.getEnd()), notNull(), notBlank(), solrDateValidator);
        addFieldRow("gap", "Gap", new DateGapEditor(definition.getGap()), notNull());
    }

    protected final void doSubmit() {
        String name = getStringValue("name");
        String fieldName = getStringValue("field");
        String startDate = getStringValue("start");
        String endDate = getStringValue("end");
        DateGap gap = (DateGap) getValue("gap");

        definition.setName(name);
        definition.setFieldName(fieldName);
        definition.setStart(startDate);
        definition.setEnd(endDate);
        definition.setGap(gap);

        doSubmit(definition);
        hide();
    }

    protected abstract void doSubmit(DateFacetDefinition definition);



    //================================================= Inner Classes ==================================================

    private static class SolrDateValidator extends AbstractValidator<String> {

        public ValidationResult validate(String text) {
            try {
                SolrDateUtils.parse(text);
                return valid();
            } catch (IllegalArgumentException iae) {
                if (text.startsWith("NOW")) {
                    return valid();
                }
                return error("Invalid date expression or date format");
            }
        }
    }
}