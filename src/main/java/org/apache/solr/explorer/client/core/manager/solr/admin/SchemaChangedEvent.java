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

package org.apache.solr.explorer.client.core.manager.solr.admin;

import org.gwtoolbox.ioc.core.client.event.AbstractApplicationEvent;

/**
 * @author Uri Boness
 */
public class SchemaChangedEvent extends AbstractApplicationEvent {

    private final Schema schema;

    public SchemaChangedEvent(Object source, Schema schema) {
        super(source);
        this.schema = schema;
    }

    public String getDescription() {
        return "";
    }

    public Schema getSchema() {
        return schema;
    }

}
