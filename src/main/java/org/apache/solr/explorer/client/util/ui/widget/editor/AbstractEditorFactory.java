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

/**
 * @author Uri Boness
 */
public abstract class AbstractEditorFactory<T> implements EditorFactory<T> {

    public Editor<T> create(boolean enabled) {
        Editor editor = create();
        editor.setEnabled(enabled);
        return editor;
    }

    public Editor<T> create(T defaultValue) {
        Editor editor = create();
        editor.setValue(defaultValue);
        return editor;
    }

    public Editor create(T defaultValue, boolean enabled) {
        Editor editor = create(defaultValue);
        editor.setEnabled(enabled);
        return editor;
    }
}
