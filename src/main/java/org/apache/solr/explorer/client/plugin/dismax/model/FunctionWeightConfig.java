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

package org.apache.solr.explorer.client.plugin.dismax.model;

import java.util.Set;

/**
 * @author Uri Boness
 */
public class FunctionWeightConfig {

    private String name;
    private String template;
    private float weight;
    private Set<String> paramNames;

    public FunctionWeightConfig(String name, String template, float weight, Set<String> paramNames) {
        this.name = name;
        this.template = template;
        this.weight = weight;
        this.paramNames = paramNames;
    }

    public FunctionWeightConfig(String template, float weight, Set<String> paramNames) {
        this(template, template, weight, paramNames);
    }

    public String getName() {
        return name;
    }

    public String getTemplate() {
        return template;
    }

    public float getWeight() {
        return weight;
    }

    public Set<String> getParamNames() {
        return paramNames;
    }
}
