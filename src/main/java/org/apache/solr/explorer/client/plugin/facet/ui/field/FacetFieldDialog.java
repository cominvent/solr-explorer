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

package org.apache.solr.explorer.client.plugin.facet.ui.field;

import org.apache.solr.explorer.client.core.manager.solr.admin.Schema;
import org.apache.solr.explorer.client.core.manager.solr.admin.Field;
import org.apache.solr.explorer.client.util.ui.widget.FormDialog;

import static org.apache.solr.explorer.client.util.ui.widget.validation.Validators.*;
import org.apache.solr.explorer.client.plugin.facet.model.context.FieldFacetDefinition;
import org.apache.solr.explorer.client.util.ui.widget.editor.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Uri Boness
 */
public abstract class FacetFieldDialog extends FormDialog {

    private final FieldFacetDefinition definition;

    public FacetFieldDialog(Schema schema) {
        this(schema, new FieldFacetDefinition());
    }

    protected FacetFieldDialog(Schema schema, boolean autoHide) {
        this(schema, new FieldFacetDefinition(), autoHide);
    }

    protected FacetFieldDialog(Schema schema, boolean autoHide, boolean modal) {
        this(schema, new FieldFacetDefinition(), autoHide, modal);
    }

    public FacetFieldDialog(Schema schema, FieldFacetDefinition definition) {
        this(schema, definition, false);
    }

    public FacetFieldDialog(Schema schema, FieldFacetDefinition definition, boolean autoHide) {
        this(schema, definition, autoHide, !autoHide);
    }

    public FacetFieldDialog(
            final Schema schema,
            FieldFacetDefinition definition,
            boolean autoHide,
            boolean modal) {

        super("Facet Field Dialog", autoHide, modal);

        this.definition = definition;

        List<Option<String>> options = new ArrayList<Option<String>>();
        Collection<Field> fields = schema.getFields();
        for (final Field field : fields) {
            options.add(new DefaultOption<String>(field.getName(), field.getName()));
        }
        SingleSelectEditor<String> fieldEditor = new SingleSelectEditor<String>(options);
        if (definition.getFieldName() != null) {
            fieldEditor.setValue(definition.getFieldName());
        }
        addFieldRow("name", "Name", new TextEditor(definition.getName(), definition.getName() == null), notNull(), notBlank());

        addFieldRow("field", "Field", fieldEditor);

        addFieldRow("mincount", "Min Count", new IntegerEditor(definition.getMinCount()), notNull(), range(0, Integer.MAX_VALUE));
        addFieldRow("limit", "Max Entries", new IntegerEditor(definition.getLimit()), range(0, Integer.MAX_VALUE));
        addFieldRow("missing", "Show Missing", new BooleanEditor(definition.hasMissing() && definition.isMissing()));
        List<Option<FieldFacetDefinition.Sort>> sortOptions = new ArrayList<Option<FieldFacetDefinition.Sort>>();
        for (FieldFacetDefinition.Sort sort : FieldFacetDefinition.Sort.values()) {
            sortOptions.add(new DefaultOption<FieldFacetDefinition.Sort>(sort.name(), sort));
        }
        SingleSelectEditor<FieldFacetDefinition.Sort> sortEditor = new SingleSelectEditor<FieldFacetDefinition.Sort>(sortOptions);
        if (definition.hasSort()) {
            sortEditor.setValue(definition.getSort());
        }
        addFieldRow("sort", "Sort", sortEditor);
    }

    protected final void doSubmit() {
        String name = (String) getValue("name");
        String fieldName = (String) getValue("field");
        Integer minCount = (Integer) getValue("mincount");
        Integer limit = (Integer) getValue("limit");
        Boolean missing = (Boolean) getValue("missing");
        FieldFacetDefinition.Sort sort = (FieldFacetDefinition.Sort) getValue("sort");

        definition.setName(name);
        definition.setFieldName(fieldName);
        definition.setMissing(missing);
        definition.setMinCount(minCount);
        definition.setSort(sort);
        definition.setLimit(limit);
        
        doSubmit(definition);
        hide();
    }

    protected abstract void doSubmit(FieldFacetDefinition definition);
}
