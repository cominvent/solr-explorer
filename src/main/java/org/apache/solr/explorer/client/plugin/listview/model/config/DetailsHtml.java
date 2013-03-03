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

package org.apache.solr.explorer.client.plugin.listview.model.config;

import org.gwtoolbox.commons.util.client.template.Template;

/**
 * @author Uri Boness
 */
public class DetailsHtml {

    private Template template;
    private String caption;

    public DetailsHtml(Template template, String caption) {
        this.template = template;
        this.caption = caption;
    }

    public Template getTemplate() {
        return template;
    }

    public String getCaption() {
        return caption;
    }
}
