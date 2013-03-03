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

package org.apache.solr.explorer.client.core.image;

import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.core.client.GWT;

/**
 * @author Uri Boness
 */
public interface SolrExplorerImages extends ImageBundle {

    @Resource("error.png")
    AbstractImagePrototype fieldError();

    @Resource("lightbulb_48.png")
    AbstractImagePrototype infoMessageIcon();

    @Resource("cancel_48.png")
    AbstractImagePrototype errorMessageIcon();

    @Resource("lightbulb_48.png")
    AbstractImagePrototype debugMessageIcon();

    @Resource("asc.gif")
    AbstractImagePrototype sortAscIcon();

    @Resource("desc.gif")
    AbstractImagePrototype sortDescIcon();

    @Resource("sort-hollow.gif")
    AbstractImagePrototype sortHollowIcon();

    @Resource("monitor.png")
    AbstractImagePrototype monitorIcon();

    @Resource("calendar.png")
    AbstractImagePrototype calendar();

    @Resource("information.png")
    AbstractImagePrototype info();

    @Resource("license.png")
    AbstractImagePrototype license();


    //================================================ Accessor ========================================================

    public static class Instance {
        private static SolrExplorerImages instance;

        public static SolrExplorerImages get() {
            if (instance == null) {
                instance = GWT.create(SolrExplorerImages.class);
            }
            return instance;
        }
    }
}
