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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Uri Boness
 */
public class DismaxConfig {

    private List<FieldWeightConfig> fieldWeights = new ArrayList<FieldWeightConfig>();

    private List<FunctionWeightConfig> functionWeights = new ArrayList<FunctionWeightConfig>();

    private String mm;

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public List<FieldWeightConfig> getFieldWeights() {
        return fieldWeights;
    }

    public void addFieldWeight(FieldWeightConfig fieldWeight) {
        fieldWeights.add(fieldWeight);
    }

    public List<FunctionWeightConfig> getFunctionWeights() {
        return functionWeights;
    }

    public void addFunctionWeight(FunctionWeightConfig functionWeight) {
        functionWeights.add(functionWeight);
    }
}
