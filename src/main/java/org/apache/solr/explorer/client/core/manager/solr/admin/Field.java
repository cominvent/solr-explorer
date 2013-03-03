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

import java.util.Map;
import java.util.HashMap;

/**
 * @author Uri Boness
 */
public class Field {

    private String name;
    private Type type;
    private boolean required;
    private boolean uniqueKey;
    private boolean stored;
    private boolean indexed;
    private boolean omitNorms;
    private boolean tokenized;
    private boolean multiValue;
    private boolean termVectorStored;
    private boolean storeOffsetWithTermVector;
    private boolean storePositionWithTermVector;
    private boolean lazy;
    private boolean binary;
    private boolean compressed;
    private boolean sortMissingFirst;
    private boolean sortMissingLast;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isStored() {
        return stored;
    }

    public void setStored(boolean stored) {
        this.stored = stored;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    public boolean isOmitNorms() {
        return omitNorms;
    }

    public void setOmitNorms(boolean omitNorms) {
        this.omitNorms = omitNorms;
    }

    public boolean isTokenized() {
        return tokenized;
    }

    public void setTokenized(boolean tokenized) {
        this.tokenized = tokenized;
    }

    public boolean isMultiValue() {
        return multiValue;
    }

    public void setMultiValue(boolean multiValue) {
        this.multiValue = multiValue;
    }

    public boolean isTermVectorStored() {
        return termVectorStored;
    }

    public void setTermVectorStored(boolean termVectorStored) {
        this.termVectorStored = termVectorStored;
    }

    public boolean isStoreOffsetWithTermVector() {
        return storeOffsetWithTermVector;
    }

    public void setStoreOffsetWithTermVector(boolean storeOffsetWithTermVector) {
        this.storeOffsetWithTermVector = storeOffsetWithTermVector;
    }

    public boolean isStorePositionWithTermVector() {
        return storePositionWithTermVector;
    }

    public void setStorePositionWithTermVector(boolean storePositionWithTermVector) {
        this.storePositionWithTermVector = storePositionWithTermVector;
    }

    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public boolean isBinary() {
        return binary;
    }

    public void setBinary(boolean binary) {
        this.binary = binary;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public boolean isSortMissingFirst() {
        return sortMissingFirst;
    }

    public void setSortMissingFirst(boolean sortMissingFirst) {
        this.sortMissingFirst = sortMissingFirst;
    }

    public boolean isSortMissingLast() {
        return sortMissingLast;
    }

    public void setSortMissingLast(boolean sortMissingLast) {
        this.sortMissingLast = sortMissingLast;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(boolean uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public void setFlags(String flags) {
        char[] chars = flags.toCharArray();
        setIndexed(chars[0] == 'I');
        setTokenized(chars[1] == 'T');
        setStored(chars[2] == 'S');
        setMultiValue(chars[3] == 'M');
        setTermVectorStored(chars[4] == 'V');
        setStoreOffsetWithTermVector(chars[5] == 'o');
        setStorePositionWithTermVector(chars[6] == 'P');
        setOmitNorms(chars[7] == 'O');
        setLazy(chars[8] == 'L');
        setBinary(chars[9] == 'B');
        setCompressed(chars[10] == 'C');
        setSortMissingFirst(chars[11] == 'f');
        setSortMissingLast(chars[12] == 'l');
    }

    public String[] getPropertiesNames() {
        return new String[] {
                "Indexed",
                "Toknized",
                "Stored",
                "Multi-Valued",
                "Term Vector Stored",
                "Store Offset With Term Vector",
                "Store Position With Term Vector",
                "Omit Norms",
                "Lazy",
                "Binary",
                "Compressed",
                "Sort Missing First",
                "Sort Missing Last"
        };
    }

    public Map<String, Object> getProperties() {
        return new HashMap<String, Object>() {{
            put("Indexed", indexed);
            put("Tokenized", tokenized);
            put("Multi-Valued", multiValue);
            put("Term Vector Stored", termVectorStored);
            put("Store Offset With Term Vector", storeOffsetWithTermVector);
            put("Store Position With Term Vector", storePositionWithTermVector);
            put("Omit Norms", omitNorms);
            put("Lazy", lazy);
            put("Binary", binary);
            put("Compressed", compressed);
            put("Sort Missing First", sortMissingFirst);
            put("Sort Missing Last", sortMissingLast);
        }};
    }
}
