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

package org.apache.solr.explorer.client.plugin.facet.ui.query;

import org.apache.solr.explorer.client.util.ui.widget.editor.TextEditor;
import static org.apache.solr.explorer.client.util.ui.widget.validation.Validators.*;
import org.apache.solr.explorer.client.util.ui.widget.FormDialog;
import org.apache.solr.explorer.client.plugin.facet.model.context.QueryFacetDefinition;

/**
 * @author Uri Boness
 */
public abstract class QueryFacetDialog extends FormDialog {

    private final QueryFacetDefinition definition;

    public QueryFacetDialog() {
        this(new QueryFacetDefinition());
    }

    protected QueryFacetDialog(QueryFacetDefinition definition) {
        this(definition, false);
    }

    protected QueryFacetDialog(QueryFacetDefinition definition, boolean autoHide) {
        this(definition, autoHide, !autoHide);
    }

    protected QueryFacetDialog(boolean autoHide) {
        this(new QueryFacetDefinition(), autoHide);
    }

    protected QueryFacetDialog(boolean autoHide, boolean modal) {
        this(new QueryFacetDefinition(), autoHide, modal);
    }

    protected QueryFacetDialog(QueryFacetDefinition definition, boolean autoHide, boolean modal) {
        super("Query Facet Dialog", autoHide, modal);
        this.definition = definition;
        addFieldRow("name", "Name", new TextEditor(definition.getName(), definition.getName() == null), notBlank());
        addFieldRow("query", "Query", new TextEditor(definition.getQuery()), notBlank());
//        setWidth("325px");
    }

    protected final void doSubmit() {
        String name = (String) getValue("name");
        String query = (String) getValue("query");

        definition.setName(name);
        definition.setQuery(query);

        doSubmit(definition);
        hide();
    }

    protected abstract void doSubmit(QueryFacetDefinition queryFacetDefinition);
}