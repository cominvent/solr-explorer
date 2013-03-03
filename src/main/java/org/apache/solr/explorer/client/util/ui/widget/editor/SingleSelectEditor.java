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

import com.google.gwt.user.client.ui.Widget;

import java.util.List;

import org.gwtoolbox.widget.client.list.DropDownBox;

/**
 * @author Uri Boness
 */
public class SingleSelectEditor<T> implements Editor<T> {

    private final DropDownBox<T> dropDownBox;

    public SingleSelectEditor(List<Option<T>> options) {
        dropDownBox = new DropDownBox<T>();
        for (Option<T> option : options) {
            dropDownBox.addOption(option.getDisplayName(), option.getValue());
        }
    }

    public T getValue() {
        return dropDownBox.getSelectedValue();
    }

    public void setValue(T t) {
        dropDownBox.setSelectedItem(t);
    }

    public Widget getWidget() {
        return dropDownBox;
    }

    public void setEnabled(boolean enabled) {
        dropDownBox.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return dropDownBox.isEnabled();
    }
}
