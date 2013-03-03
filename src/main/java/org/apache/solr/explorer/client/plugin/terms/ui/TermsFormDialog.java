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

package org.apache.solr.explorer.client.plugin.terms.ui;

import com.google.gwt.event.dom.client.*;
import org.apache.solr.explorer.client.core.ui.support.FieldNamesSuggestOracle;
import org.apache.solr.explorer.client.core.ui.support.FieldNamesValidator;
import org.apache.solr.explorer.client.util.ui.widget.FormDialog;
import org.apache.solr.explorer.client.util.ui.widget.editor.AutoSuggestTextEditor;
import org.apache.solr.explorer.client.util.ui.widget.editor.BooleanEditor;
import org.apache.solr.explorer.client.util.ui.widget.editor.IntegerEditor;
import org.apache.solr.explorer.client.util.ui.widget.validation.AbstractValidator;
import org.apache.solr.explorer.client.util.ui.widget.validation.ValidationResult;
import org.apache.solr.explorer.client.plugin.terms.model.context.TermsContext;
import org.gwtoolbox.commons.util.client.StringUtils;

import java.util.Arrays;
import java.util.List;

import static org.apache.solr.explorer.client.util.ui.widget.validation.Validators.*;

/**
 * @author Uri Boness
 */
public class TermsFormDialog extends FormDialog {

    private TermsContext termsContext;

    public TermsFormDialog() {
        this(new TermsContext());
    }

    public TermsFormDialog(TermsContext termsContext) {
        super("Spellchecking Form");
        this.termsContext = termsContext;
//        setWidth("300px");
        setResizable(true);

        final BooleanEditor enabledEditor = new BooleanEditor(termsContext.isEnabled());
        addFieldRow("enabled", "Enabled", enabledEditor);

        List<String> fieldNames = termsContext.getFieldNames();
        String defaultValue = StringUtils.collectionToDelimetedString(fieldNames, ", ");
        final AutoSuggestTextEditor fieldNamesEditor =
                new AutoSuggestTextEditor(FieldNamesSuggestOracle.getInstance(), defaultValue, enabledEditor.getValue());
        
        addFieldRow("fieldNames", "Field Names", fieldNamesEditor, notNull(), notBlank(), FieldNamesValidator.getInstance());

        final IntegerEditor limitEditor = new IntegerEditor(termsContext.getLimit(), enabledEditor.getValue());
        addFieldRow("limit", "Limit", limitEditor, notNull());

        final IntegerEditor minCountEditor = new IntegerEditor(termsContext.getMinCount(), enabledEditor.getValue());
        addFieldRow("minCount", "Min. Count", minCountEditor, min(0));

        final IntegerEditor maxCountEditor = new IntegerEditor(termsContext.getMaxCount(), enabledEditor.getValue());
        addFieldRow("maxCount", "Max. Count", maxCountEditor, new AbstractValidator<Integer>() {
            public ValidationResult validate(Integer value) {
                if (value < 0) {
                    return valid();
                }
                return value >= minCountEditor.getValue() ? valid() : error("Value must be greater than Min. Count");
            }
        });

        enabledEditor.getWidget().addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                boolean enabled = enabledEditor.getWidget().getValue();
                fieldNamesEditor.setEnabled(enabled);
                limitEditor.setEnabled(enabled);
                minCountEditor.setEnabled(enabled);
                maxCountEditor.setEnabled(enabled);
            }
        });
    }

    @Override
    protected boolean shouldValidate(String key) {
        boolean enabled = (Boolean) getValue("enabled");
        if ("fieldNames".equals(key) || "limit".equals(key) || "minCount".equals(key) || "maxCount".equals(key)) {
            return enabled;
        }
        return true;
    }

    protected final void doSubmit() {
        boolean enabled = getBooleanValue("enabled");
        termsContext.setEnabled(enabled);
        if (!enabled) {
            hide();
            doSubmit(termsContext);
            return;
        }
        String[] names = getStringValue("fieldNames").split("(,|\\s)");
        List<String> fieldNames = Arrays.asList(names);
        termsContext.setFieldNames(fieldNames);

        Integer limit = getIntValue("limit");
        if (limit == null) {
            limit = 10;
        }
        termsContext.setLimit(limit);

        Integer minCount = getIntValue("minCount");
        if (minCount == null) {
            minCount = 0;
        }
        termsContext.setMinCount(minCount);

        Integer maxCount = getIntValue("maxCount");
        if (maxCount == null) {
            maxCount = -1;
        }
        termsContext.setMaxCount(maxCount);

        hide();
        doSubmit(termsContext);
    }

    protected void doSubmit(TermsContext termsContext) {
    }

}