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

import java.util.Date;

/**
 * @author Uri Boness
 */
public class IndexInfo {

    private long indexVersion;
    private boolean optimized;
    private boolean hasDeletions;
    private String directory;
    private Date lastModified;
    private long numberOfDocuments;
    private long numberOfTerms;

    public long getIndexVersion() {
        return indexVersion;
    }

    void setIndexVersion(long indexVersion) {
        this.indexVersion = indexVersion;
    }

    public boolean isOptimized() {
        return optimized;
    }

    void setOptimized(boolean optimized) {
        this.optimized = optimized;
    }

    public boolean isHasDeletions() {
        return hasDeletions;
    }

    void setHasDeletions(boolean hasDeletions) {
        this.hasDeletions = hasDeletions;
    }

    public String getDirectory() {
        return directory;
    }

    void setDirectory(String directory) {
        this.directory = directory;
    }

    public Date getLastModified() {
        return lastModified;
    }

    void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public long getNumberOfDocuments() {
        return numberOfDocuments;
    }

    void setNumberOfDocuments(long numberOfDocuments) {
        this.numberOfDocuments = numberOfDocuments;
    }

    public long getNumberOfTerms() {
        return numberOfTerms;
    }

    void setNumberOfTerms(long numberOfTerms) {
        this.numberOfTerms = numberOfTerms;
    }
}
