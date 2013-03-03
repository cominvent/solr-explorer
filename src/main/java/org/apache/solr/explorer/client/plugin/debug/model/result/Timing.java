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

package org.apache.solr.explorer.client.plugin.debug.model.result;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Uri Boness
 */
public class Timing {

    private double time;
    private Phase preparePhase;
    private Phase processPhase;

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Phase getPreparePhase() {
        return preparePhase;
    }

    public void setPreparePhase(Phase preparePhase) {
        this.preparePhase = preparePhase;
    }

    public Phase getProcessPhase() {
        return processPhase;
    }

    public void setProcessPhase(Phase processPhase) {
        this.processPhase = processPhase;
    }

    //================================================= Inner Classes ==================================================

    public static class Phase {

        private double time;
        private Map<String, Double> timeByComponent = new HashMap<String, Double>();

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }

        public void addTime(String component, double time) {
            timeByComponent.put(component, time);
        }

        public Map<String, Double> getTimeByComponent() {
            return timeByComponent;
        }
    }

}
