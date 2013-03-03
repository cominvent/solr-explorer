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

package org.apache.solr.explorer.client.plugin.facet.model.context;

/**
 * A field facet definition.
 *
 * @author Uri Boness
 */
public class FieldFacetDefinition extends FacetDefinition {

    public enum Sort { INDEX, COUNT }

    private String fieldName;

    private Sort sort;

    private Integer limit;

    private int minCount = 0;

    private Boolean missing;

    private boolean mutuallyExclusive = true;

    private boolean showAsCloud;

    /**
     * Default empty constructor.
     */
    public FieldFacetDefinition() {
        this(null);
    }

    /**
     * Constructs a new FieldFacetDefinition without a name.
     *
     * @param fieldName The field name of the facet.
     */
    public FieldFacetDefinition(String fieldName) {
        this(fieldName, fieldName);
    }

    /**
     * Constructs a new FieldFacetDefinition with a given field name.
     *
     * @param name The name of the facet
     * @param fieldName The name of the field.
     */
    public FieldFacetDefinition(String name, String fieldName) {
        super(name);
        this.fieldName = fieldName;
    }

    /**
     * Returns the name of the field of this facet.
     *
     * @return The name of the field of this facet.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Sets the field name of this facet.
     *
     * @param fieldName The field name of this facet.
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Returns whether the sort is defined. If not, the default sort should be used.
     *
     * @return whether the sort is defined.
     */
    public boolean hasSort() {
        return sort != null;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    /**
     * Returns the maximum number of facet entries that should be returned. Negative number indicates that there should
     * be no limit. May return <code>null</code> indicating the limit was not set.
     *
     * @see #hasLimit()
     * @return The maximum number of facet entries that should be returned. Negative number indicates that there should
     *         be no limit. <code>null</code> indicates the limit was not set.
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Sets the maximum number of facet entries that should be returned. A netagive number indicates there should be no
     * limit.
     *
     * @see #getLimit()
     * @param limit The maximum number of facet entries that should be returned.
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * Returns whether the limit of returned entries is set.
     *
     * @return Whether the limit of returned entries is set.
     */
    public boolean hasLimit() {
        return limit != null;
    }

    /**
     * Returns the minimum count that should be associated with the returned entries. When set to X, all entries with
     * counts that are less than X will be filtered out and will not be returned.
     *
     * @return The minimum count that should be associated with the returned entries.
     */
    public int getMinCount() {
        return minCount;
    }

    /**
     * Sets the minimum count that should be associated with the returned entries.
     *
     * @see #getMinCount()
     * @param minCount The minimum count that should be associated with the returned entries.
     */
    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    /**
     * Returns whether the minimum was set.
     *
     * @see #getMinCount()
     * @see #setMinCount(int)
     * @return Whether the minimum was set.
     */
    public boolean hasMinCount() {
        return minCount >= 0;
    }

    /**
     * Returns whether the missing entry should be returned. The missing entry represents the count of all documents for
     * which this facet does not apply (that is, document which hold no value for the associated field).
     *
     * @return whether the missing entry should be returned.
     */
    public boolean isMissing() {
        return missing;
    }

    /**
     * Sets whether the missing entry should be returned.
     *
     * @see #isMissing()
     * @param missing Indicates whether the missing entry should be returned.
     */
    public void setMissing(Boolean missing) {
        this.missing = missing;
    }

    /**
     * Returns whether the missing entry should be returned.
     *
     * @return Whether the missing entry should be returned.
     */
    public boolean hasMissing() {
        return missing != null;
    }

    /**
     * Returns whether this facet is mutually exclusive, that is whether one can filter on the facet multiple times.
     *
     * @return Whether this facet is mutually exclusive, that is whether one can filter on the facet multiple times.
     */
    public boolean isMutuallyExclusive() {
        return mutuallyExclusive;
    }

    /**
     * Sets whether this facet is mutually exclusive, that is whether one can filter on the facet multiple times.
     *
     * @param mutuallyExclusive Indicates whether this facet is mutually exclusive, that is whether one can filter on
     *        the facet multiple times.
     */
    public void setMutuallyExclusive(boolean mutuallyExclusive) {
        this.mutuallyExclusive = mutuallyExclusive;
    }

    /**
     * Returns whether this facet should be displayed as a cloud.
     *
     * @return Whether this facet should be displayed as a cloud.
     */
    public boolean isShowAsCloud() {
        return showAsCloud;
    }

    /**
     * Sets whether this facet should be displayed as a cloud. The size of the facet values will be determined by their
     * associated counts.
     *
     * @param showAsCloud Sets whether this facet should be displayed as a cloud.
     */
    public void setShowAsCloud(boolean showAsCloud) {
        this.showAsCloud = showAsCloud;
    }
}
