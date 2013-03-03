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

import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.solr.admin.Schema;
import org.apache.solr.explorer.client.util.ui.widget.validation.AbstractValidator;
import org.apache.solr.explorer.client.util.ui.widget.validation.ValidationResult;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;

/**
 * @author Uri Boness
 */
@Component
public class FieldNamesValidator extends AbstractValidator<String> {

    private Schema schema;

    private static FieldNamesValidator instance;

    public FieldNamesValidator() {
        instance = this;
    }

    public static FieldNamesValidator getInstance() {
        return instance;
    }

    public ValidationResult validate(String text) {
        String[] names = text.split(",");
        for (String name : names) {
            name = name.trim();
            if (name.length() == 0) {
                continue;
            }
            if (!schema.hasField(name)) {
                return error("Unknown filed named '" + name + "'");
            }
        }
        return valid();
    }


    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        schema = event.getSolrCore().getSchema();
    }
}
