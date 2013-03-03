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

package org.apache.solr.explorer.client.util.ui.widget;

import java.util.Collection;
import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;

/**
 * @author Uri Boness
 */
public class RadioSelect<T> extends Composite {

    private final HorizontalPanel main;
    private final String name;

    private final Collection<Listener<T>> listeners = new ArrayList<Listener<T>>();

    public RadioSelect(String name) {
        this.main = new HorizontalPanel();
        this.name = name;
        initWidget(main);
    }

    public void addOption(String label, final T value) {
        RadioButton button = new RadioButton(name, label);
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                notifySelection(value);
            }
        });
        main.add(button);
    }

    public void addListener(Listener<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener<T> listener) {
        listeners.remove(listener);
    }

    public void removeAllListeners() {
        listeners.clear();
    }

    //============================================== Helper Methods ====================================================

    private void notifySelection(T value) {
        for (Listener<T> listener : listeners) {
            listener.valueSelected(value);
        }
    }


    //============================================== Inner Classes =====================================================

    public interface Listener<T> {

        void valueSelected(T value);

    }
}
