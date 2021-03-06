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

package org.apache.solr.explorer.client.core.ui.consolepane.searchcontext;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Widget;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;

/**
 * @author Uri Boness
 */
public abstract class AbstractSearchContextComponent extends Composite implements SearchContextComponent {

    protected final static String UNSPECIFIED = "---";

    protected DisclosurePanel main;

    private boolean active;

    public void init(SolrCore solrCore) {
        setActive(true);
    }

    public boolean isActive() {
        return active;
    }

    public Widget getWidget() {
        return this;
    }

    protected void setActive(boolean active) {
        this.active = active;
    }

    protected AbstractSearchContextComponent(String title) {
        main = new DisclosurePanel(title);
        main.setAnimationEnabled(true);
        initWidget(main);
    }
}
