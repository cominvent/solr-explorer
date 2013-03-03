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

package org.apache.solr.explorer.client.plugin.highlight.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import org.apache.solr.explorer.client.plugin.highlight.model.context.HighlightingContext;
import org.apache.solr.explorer.client.util.ui.widget.FormDialog;
import org.apache.solr.explorer.client.util.ui.widget.editor.BooleanEditor;
import org.apache.solr.explorer.client.util.ui.widget.editor.TextEditor;
import org.gwtoolbox.widget.client.panel.layout.animation.AnimationCallbackAdapter;

import static org.apache.solr.explorer.client.util.ui.widget.validation.Validators.notBlank;

/**
 * @author Uri Boness
 */
public abstract class HighlightingFormDialog extends FormDialog {

    private HighlightingContext highlighting;

    public HighlightingFormDialog() {
        this(new HighlightingContext());
    }

    public HighlightingFormDialog(HighlightingContext highlighting) {
        super("Highlighting Form");
        this.highlighting = highlighting;

        final BooleanEditor enabledEditor = new BooleanEditor(highlighting.isEnabled());
        addFieldRow("enabled", "Enabled", enabledEditor);

        final TextEditor fieldsEditor = new TextEditor(highlighting.getFields(), highlighting.isEnabled());
        addFieldRow("fields", "Fields", fieldsEditor, notBlank());

        final BooleanEditor multiTermEditor = new BooleanEditor(highlighting.isHighlightMultiTerm(), highlighting.isEnabled());
        addFieldRow("multiTerm", "Multi Term", multiTermEditor);

        final TextEditor prefixEditor = new TextEditor(highlighting.getWrappingPrefix(), highlighting.isEnabled());
        addFieldRow("prefix", "Prefix", prefixEditor, notBlank());

        final TextEditor suffixEditor = new TextEditor(highlighting.getWrappingSuffix(), highlighting.isEnabled());
        addFieldRow("suffix", "Suffix", suffixEditor, notBlank());

        enabledEditor.getWidget().addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                boolean enabled = enabledEditor.getWidget().getValue();
                fieldsEditor.setEnabled(enabled);
                multiTermEditor.setEnabled(enabled);
                prefixEditor.setEnabled(enabled);
                suffixEditor.setEnabled(enabled);
            }
        });
    }

    @Override
    protected boolean shouldValidate(String key) {
        boolean enabled = getBooleanValue("enabled");
        if ("fields".equals(key) || "prefix".equals(key) || "suffix".equals(key) || "multiTerm".equals(key)) {
            return enabled;
        }
        return true;
    }

    protected final void doSubmit() {
        boolean enabled = getBooleanValue("enabled");
        highlighting.setEnabled(enabled);
        if (!enabled) {
            hide();
            doSubmit(highlighting);
            return;
        }
        highlighting.setFields(getStringValue("fields"));
        highlighting.setHighlightMultiTerm(getBooleanValue("multiTerm"));
        highlighting.setWrappingPrefix(getStringValue("prefix"));
        highlighting.setWrappingSuffix(getStringValue("suffix"));

        hide(new AnimationCallbackAdapter() {
            public void onAnimationComplete() {
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        doSubmit(highlighting);
                    }
                });
            }
        });
    }

    protected abstract void doSubmit(HighlightingContext highlighting);
}
