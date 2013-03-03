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

/**
 * @author Uri Boness
 */
public abstract class AbstractLogger implements Logger {

    private Level level;

    public boolean isTraceEnabled() {
        return isEnabled(Level.TRACE);
    }

    public void trace(String message) {
        trace(message, null);
    }

    public void trace(String message, Throwable t) {
        log(Level.TRACE, message, t);
    }

    public boolean isDebugEnabled() {
        return isEnabled(Level.DEBUG);
    }

    public void debug(String message) {
        debug(message, null);
    }

    public void debug(String message, Throwable t) {
        log(Level.DEBUG, message, t);
    }

    public boolean isInfoEnabled() {
        return isEnabled(Level.INFO);
    }

    public void info(String message) {
        info(message, null);
    }

    public void info(String message, Throwable t) {
        log(Level.INFO, message, t);
    }

    public boolean isWarnEnabled() {
        return isEnabled(Level.WARN);
    }

    public void warn(String message) {
        warn(message, null);
    }

    public void warn(String message, Throwable t) {
        log(Level.WARN, message, t);
    }

    public boolean isErrorEnabled() {
        return isEnabled(Level.ERROR);
    }

    public void error(String message) {
        error(message, null);
    }

    public void error(String message, Throwable t) {
        log(Level.ERROR, message, t);
    }

    //============================================== Helper Methods ====================================================

    public boolean isEnabled(Level level) {
        return this.level.isIncluded(level);
    }

    public void log(Level level, String message) {
        log(level, message, null);
    }

    public abstract void log(Level level, String message, Throwable t);

    //============================================== Setter/Getter =====================================================

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    //============================================== Inner Classes =====================================================
    }
