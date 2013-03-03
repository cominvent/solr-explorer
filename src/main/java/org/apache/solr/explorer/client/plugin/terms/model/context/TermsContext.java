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

package org.apache.solr.explorer.client.plugin.terms.model.context;

import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.model.context.Context;
import org.apache.solr.explorer.client.plugin.terms.model.config.TermsConfig;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Uri Boness
 */
@Component
public class TermsContext implements Context {

    public enum Sort { COUNT, INDEX }

    // required
    private boolean enabled;
    private List<String> fieldNames = new ArrayList<String>();

    private String prefix;

    private String lowerBound;
    private boolean lowerBoundIncluded = false;
    private String upperBound;
    private boolean upperBoundIncluded = false;

    private int minCount = 0;
    private int maxCount = -1;
    private int limit = 10;

    private boolean raw = false;
    private Sort sort = Sort.COUNT;

    public TermsContext() {
    }

    public TermsContext(TermsContext other) {
        init(other);
    }

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        TermsConfig config = event.getSolrCore().getConfiguration().getConfig(TermsConfig.class);
        if (config == null) {
            setEnabled(false);
            return;
        }
        init(config.getDefaultContext());
    }

    public void init(TermsContext other) {
        this.enabled = other.enabled;
        this.fieldNames = new ArrayList<String>(other.fieldNames);
        this.prefix = other.prefix;
        this.lowerBound = other.lowerBound;
        this.lowerBoundIncluded = other.lowerBoundIncluded;
        this.upperBound = other.upperBound;
        this.upperBoundIncluded = other.upperBoundIncluded;
        this.minCount = other.minCount;
        this.maxCount = other.maxCount;
        this.limit = other.limit;
        this.raw = other.raw;
        this.sort = other.sort;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void addFieldName(String fieldName) {
        fieldNames.add(fieldName);
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(String lowerBound) {
        this.lowerBound = lowerBound;
    }

    public boolean isLowerBoundIncluded() {
        return lowerBoundIncluded;
    }

    public void setLowerBoundIncluded(boolean lowerBoundIncluded) {
        this.lowerBoundIncluded = lowerBoundIncluded;
    }

    public String getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(String upperBound) {
        this.upperBound = upperBound;
    }

    public boolean isUpperBoundIncluded() {
        return upperBoundIncluded;
    }

    public void setUpperBoundIncluded(boolean upperBoundIncluded) {
        this.upperBoundIncluded = upperBoundIncluded;
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isRaw() {
        return raw;
    }

    public void setRaw(boolean raw) {
        this.raw = raw;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }
}
