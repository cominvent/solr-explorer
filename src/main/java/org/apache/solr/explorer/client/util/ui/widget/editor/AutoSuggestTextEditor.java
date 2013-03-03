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

import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * @author Uri Boness
 */
public class AutoSuggestTextEditor implements Editor<String> {
    
    private SuggestBox box;

    public AutoSuggestTextEditor(SuggestOracle oracle) {
        this(oracle, "", true);
    }

    public AutoSuggestTextEditor(SuggestOracle oracle, String defaultText) {
        this(oracle, defaultText, true);
    }

    public AutoSuggestTextEditor(SuggestOracle oracle, boolean enabled) {
        this(oracle, "", enabled);
    }

    public AutoSuggestTextEditor(SuggestOracle oracle, String defaultText, boolean enabled) {
        box = new SuggestBox(oracle);
        box.setPopupStyleName("AutoCompletionPopup");
        box.setAnimationEnabled(true);
        box.setText(defaultText == null ? "" : defaultText);
        box.getTextBox().setEnabled(enabled);
    }

    public String getValue() {
        return box.getText();
    }

    public void setValue(String text) {
        box.setText(text);
    }

    public SuggestBox getWidget() {
        return box;
    }

    public void setEnabled(boolean enabled) {
        box.getTextBox().setEnabled(enabled);
    }

    public boolean isEnabled() {
        return box.getTextBox().isEnabled();
    }
}