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

package org.apache.solr.explorer.client.util.collection;

import org.gwtoolbox.commons.conversion.client.TextConverterManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Uri Boness
 */
public class Properties extends HashMap<String, String> {

    public final static Properties EMPTY = new Properties() {
        @Override
        public void put(String name, Object value) {
            throw new RuntimeException("Cannot modify the EMPTY Properties instance");
        }

        @Override
        public String put(String key, String value) {
            throw new RuntimeException("Cannot modify the EMPTY Properties instance");
        }

        @Override
        public void putAll(Map<? extends String, ? extends String> m) {
            throw new RuntimeException("Cannot modify the EMPTY Properties instance");
        }
    };

    public Properties() {
    }

    public Properties(Properties properties) {
        putAll(properties);
    }

    public void put(String name, Object value) {
        put(name, String.valueOf(value));
    }

    public Boolean getBoolean(String name) {
        String value = get(name);
        if (value == null) {
            return null;
        }
        return TextConverterManager.getGlobalConverter().toValue(Boolean.class, value);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        Boolean value = getBoolean(name);
        return value != null ? value : defaultValue;
    }

    public Integer getInt(String name) {
        String value = get(name);
        if (value == null) {
            return null;
        }
        return TextConverterManager.getGlobalConverter().toValue(Integer.class, value);
    }

    public int getInt(String name, int defaultValue) {
        Integer value = getInt(name);
        return value != null ? value : defaultValue;
    }

    public Long getLong(String name) {
        String value = get(name);
        if (value == null) {
            return null;
        }
        return TextConverterManager.getGlobalConverter().toValue(Long.class, value);
    }

    public long getLong(String name, long defaultValue) {
        Long value = getLong(name);
        return value != null ? value : defaultValue;
    }

    public Double getDouble(String name) {
        String value = get(name);
        if (value == null) {
            return null;
        }
        return TextConverterManager.getGlobalConverter().toValue(Double.class, value);
    }

    public double getDouble(String name, double defaultValue) {
        Double value = getDouble(name);
        return value != null ? value : defaultValue;
    }

    public Float getFloat(String name) {
        String value = get(name);
        if (value == null) {
            return null;
        }
        return TextConverterManager.getGlobalConverter().toValue(Float.class, value);
    }

    public float getFloat(String name, float defaultValue) {
        Float value = getFloat(name);
        return value != null ? value : defaultValue;
    }

}
