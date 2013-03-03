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

package org.apache.solr.explorer.client.core.model.context;

import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.ConfiguredProperty;

/**
 * @author Uri Boness
 */
@Component
public class CommonContext implements Context {

    private String version;
    private boolean indent;
    private boolean debugQuery;
    private String writerType = "xml";

    public String getVersion() {
        return version;
    }

    @ConfiguredProperty("core.search.param.version")
    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isIndent() {
        return indent;
    }

    @ConfiguredProperty("core.search.param.indent")
    public void setIndent(boolean indent) {
        this.indent = indent;
    }

    public boolean isDebugQuery() {
        return debugQuery;
    }

    @ConfiguredProperty("core.search.param.debugQuery")
    public void setDebugQuery(boolean debugQuery) {
        this.debugQuery = debugQuery;
    }

    public String getWriterType() {
        return writerType;
    }

}
