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
public class Thumbnail {

    private final Template urlTemplate;
    private final String width;
    private final String height;

    public Thumbnail(String width, String height, Template urlTemplate) {
        this.width = width;
        this.height = height;
        this.urlTemplate = urlTemplate;
    }

    public Template getUrlTemplate() {
        return urlTemplate;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

}
