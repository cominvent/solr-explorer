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

package org.apache.solr.explorer.client.util.ui.widget.editor;

import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Uri Boness
 */
public class IntegerEditor implements Editor<Integer> {

    public final static EditorFactory<Integer> FACTORY = new AbstractEditorFactory<Integer>() {
        public IntegerEditor create() {
            return new IntegerEditor();
        }
    };

    private TextBox textBox;

    public IntegerEditor() {
        this(0);
    }

    public IntegerEditor(boolean enabled) {
        this(0, enabled);
    }

    public IntegerEditor(Integer defaultValue) {
        this(defaultValue, true);
    }

    public IntegerEditor(Integer defaultValue, boolean enabled) {
        textBox = new TextBox();
        if (defaultValue != null) {
            textBox.setText(String.valueOf(defaultValue));
        }
        setEnabled(enabled);
    }

    public Integer getValue() {
        String stringValue = textBox.getText();
        if ("".equals(stringValue)) {
            return null;
        }
        return Integer.valueOf(stringValue);
    }

    public void setValue(Integer integer) {
        textBox.setText(integer == null ? "" : integer.toString());
    }

    public TextBox getWidget() {
        return textBox;
    }

    public void setEnabled(boolean enabled) {
        textBox.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return textBox.isEnabled();
    }
}
