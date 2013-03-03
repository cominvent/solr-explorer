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

package org.apache.solr.explorer.client.core.event;

import org.gwtoolbox.ioc.core.client.event.AbstractApplicationEvent;
import org.apache.solr.explorer.client.core.model.configuration.Configuration;

/**
 * @author Uri Boness
 */
public class ConfigurationLoadedEvent extends AbstractApplicationEvent {

    private final Configuration configuration;

    public ConfigurationLoadedEvent(Object source, Configuration configuration) {
        super(source);
        this.configuration = configuration;
    }

    public String getDescription() {
        return "";
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
