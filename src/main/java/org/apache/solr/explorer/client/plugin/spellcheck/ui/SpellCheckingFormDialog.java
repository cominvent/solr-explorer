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

package org.apache.solr.explorer.client.plugin.spellcheck.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import org.apache.solr.explorer.client.util.ui.widget.FormDialog;
import org.apache.solr.explorer.client.util.ui.widget.editor.BooleanEditor;
import org.apache.solr.explorer.client.util.ui.widget.editor.IntegerEditor;
import org.apache.solr.explorer.client.util.ui.widget.editor.TextEditor;
import static org.apache.solr.explorer.client.util.ui.widget.validation.Validators.*;
import org.apache.solr.explorer.client.plugin.spellcheck.model.context.SpellCheckingContext;
import org.gwtoolbox.widget.client.panel.layout.animation.AnimationCallbackAdapter;

/**
 * @author Uri Boness
 */
public class SpellCheckingFormDialog extends FormDialog {

    private SpellCheckingContext spellChecking;

    public SpellCheckingFormDialog() {
        this(new SpellCheckingContext());
    }

    public SpellCheckingFormDialog(SpellCheckingContext spellChecking) {
        super("Spellchecking Form");
        this.spellChecking = spellChecking;

        final BooleanEditor enabledEditor = new BooleanEditor(spellChecking.isEnabled());
        addFieldRow("enabled", "Enabled", enabledEditor);

        final TextEditor dictionaryEditor = new TextEditor(spellChecking.getDictionary(), enabledEditor.getWidget().getValue());
        addFieldRow("dictionary", "Dictionary", dictionaryEditor, notNull(), notBlank());

        final IntegerEditor countEditor = new IntegerEditor(spellChecking.getCount(), enabledEditor.getWidget().getValue());
        addFieldRow("count", "Count", countEditor, notNull(), range(0, 10));

        final BooleanEditor collateEditor = new BooleanEditor(spellChecking.isCollate(), enabledEditor.getWidget().getValue());
        addFieldRow("collate", "Collate", collateEditor);

        final BooleanEditor popularEditor = new BooleanEditor(spellChecking.isOnlyMorePopular(), enabledEditor.getWidget().getValue());
        addFieldRow("popular", "Show Only More Popular", popularEditor);

        enabledEditor.getWidget().addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                boolean enabled = enabledEditor.getWidget().getValue();
                dictionaryEditor.getWidget().setEnabled(enabled);
                countEditor.getWidget().setEnabled(enabled);
                collateEditor.getWidget().setEnabled(enabled);
                popularEditor.getWidget().setEnabled(enabled);
            }
        });
    }

    @Override
    protected boolean shouldValidate(String key) {
        boolean enabled = (Boolean) getValue("enabled");
        if ("dictionary".equals(key) || "count".equals(key) || "collate".equals(key) || "popular".equals(key)) {
            return enabled;
        }
        return true;
    }

    protected final void doSubmit() {
        boolean enabled = getBooleanValue("enabled");
        spellChecking.setEnabled(enabled);
        if (!enabled) {
            hide();
            doSubmit(spellChecking);
            return;
        }
        spellChecking.setDictionary(getStringValue("dictionary"));
        spellChecking.setCount(getIntValue("count"));
        spellChecking.setCollate(getBooleanValue("collate"));
        spellChecking.setOnlyMorePopular(getBooleanValue("popular"));

        hide(new AnimationCallbackAdapter() {
            public void onAnimationComplete() {
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        doSubmit(spellChecking);
                    }
                });                
            }
        });
    }

    protected void doSubmit(SpellCheckingContext spellChecking) {
    }
}