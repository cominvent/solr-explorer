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

package org.apache.solr.explorer.client.core.ui.support;

import com.google.gwt.user.client.ui.SuggestOracle;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.solr.admin.Field;
import org.apache.solr.explorer.client.core.manager.solr.admin.Schema;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Uri Boness
 */
@Component
public class FieldNamesSuggestOracle extends SuggestOracle {

    private Schema schema;

    private static FieldNamesSuggestOracle instance;

    public FieldNamesSuggestOracle() {
        instance = this;
    }

    public static FieldNamesSuggestOracle getInstance() {
        return instance;
    }

    @Override
    public void requestSuggestions(Request request, Callback callback) {
        String text = request.getQuery();
        Set<String> alreadyDefined = parse(text);
        List<Suggestion> suggestions = new ArrayList<Suggestion>();
        for (Field field : schema.getFields()) {
            if (!alreadyDefined.contains(field.getName())) {
                suggestions.add(new NamesSuggestion(text, field.getName()));
            }
        }
        callback.onSuggestionsReady(request, new Response(suggestions));
    }

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        this.schema = event.getSolrCore().getSchema();
    }

    private Set<String> parse(String text) {
        Set<String> result = new HashSet<String>();
        String[] names = text.split(",");
        for (String name : names) {
            name = name.trim();
            if (name.length() != 0) {
                result.add(name);
            }
        }
        return result;
    }


    //================================================= Inner Classes ==================================================

    private class NamesSuggestion implements Suggestion {

        private final String name;
        private final String replacementString;

        private NamesSuggestion(String text, String name) {
            this.name = name;
            int index = text.lastIndexOf(",");
            if (index < 0) {
                replacementString = name;
            } else {
                text = text.substring(0, index + 1);
                replacementString = text + name;
            }
        }

        public String getDisplayString() {
            return name;
        }

        public String getReplacementString() {
            return replacementString;
        }
    }
}
