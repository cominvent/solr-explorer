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

package org.apache.solr.explorer.client.core.manager.solr.search.urlwriter;

import org.apache.solr.explorer.client.core.manager.RequestParams;
import org.apache.solr.explorer.client.core.plugin.PluginBase;
import org.apache.solr.explorer.client.core.model.context.Context;
import org.apache.solr.explorer.client.core.manager.solr.ParamsSource;
import org.apache.solr.explorer.client.util.collection.Properties;

/**
 * @author Uri Boness
 */
public abstract class AbstractParamsSource<T extends Context> extends PluginBase implements ParamsSource<T> {

    private final String name;

    public AbstractParamsSource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addParams(RequestParams params, T context) {
        addParams(params, context, Properties.EMPTY);
    }

    //================================================ Helper Methods ==================================================

    protected static String escapeLucenQuery(String value) {
        return value.replace("\\", "\\\\")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace("&&", "\\&&")
                .replace("||", "\\||")
                .replace("!", "\\!")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("^", "\\^")
                .replace("~", "\\~")
                .replace("?", "\\?")
                .replace(":", "\\:");
    }

}
