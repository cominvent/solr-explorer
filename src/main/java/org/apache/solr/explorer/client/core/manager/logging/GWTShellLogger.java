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

package org.apache.solr.explorer.client.core.manager.logging;

import com.google.gwt.core.client.GWT;
import org.gwtoolbox.ioc.core.client.annotation.ConfiguredProperty;

/**
 * @author Uri Boness
 */
//@Component(name = "logger")
public class GWTShellLogger extends AbstractLogger {

    public void log(Level level, String message, Throwable t) {
        GWT.log("[" + level.name() + "] " + message, t);
    }

    @ConfiguredProperty("core.logging.default.level")
    public void setLogLevelAsString(String levelAsString) {
        Level level = Level.valueOf(levelAsString.toUpperCase());
        setLevel(level);
    }

}
