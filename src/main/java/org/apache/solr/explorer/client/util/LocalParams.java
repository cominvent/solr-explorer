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

package org.apache.solr.explorer.client.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Uri Boness
 */
public class LocalParams implements ValueSource {

    private final static String TYPE_PARAM = "type";

    private final String value;
    private final Map<String, String> params = new HashMap<String, String>();

    public LocalParams(String value) {
        this.value = value;
    }

    public LocalParams set(String name, String value) {
        params.put(name, value);
        return this;
    }

    public LocalParams setType(String type) {
        return set(TYPE_PARAM, type);
    }

    public String getValue() {
        if (params.isEmpty()) {
            return value;
        }
        StringBuilder builder = new StringBuilder("{!");
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                builder.append(" ");
            }
            builder.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }
        builder.append("}").append(value);
        return builder.toString();
    }
}
