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

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Uri Boness
 */
public class UnorderedList extends Widget {

    public UnorderedList() {
        setElement(DOM.createElement("UL"));
    }

    public void addListItem(String text) {
        addListItem(text, false);
    }

    public void addListItem(String text, boolean html) {
        Element li = DOM.createElement("LI");
        if (html) {
            li.setInnerHTML(text);
        } else {
            li.setInnerText(text);
        }
        getElement().appendChild(li);
    }

    public void clear() {
        getElement().setInnerHTML("");
    }
}
