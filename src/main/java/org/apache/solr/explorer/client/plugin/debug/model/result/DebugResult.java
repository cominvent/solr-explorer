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
public class DebugResult {

    private String rawQueryString;
    private String queryString;
    private String parsedQueryString;
    private String parsedQueryStringToString;
    private String qParser;
    private Timing timing;
    
    private Map<String, HitExplain> hitDebugInfoByDocumentId = new HashMap<String, HitExplain>();

    public void addHitDebugInfo(String documentId, HitExplain explain) {
        hitDebugInfoByDocumentId.put(documentId, explain);
    }

    public HitExplain getHitDebugInfo(String documentId) {
        return hitDebugInfoByDocumentId.get(documentId);
    }

    public String getRawQueryString() {
        return rawQueryString;
    }

    public void setRawQueryString(String rawQueryString) {
        this.rawQueryString = rawQueryString;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getParsedQueryString() {
        return parsedQueryString;
    }

    public void setParsedQueryString(String parsedQueryString) {
        this.parsedQueryString = parsedQueryString;
    }

    public String getParsedQueryStringToString() {
        return parsedQueryStringToString;
    }

    public void setParsedQueryStringToString(String parsedQueryStringToString) {
        this.parsedQueryStringToString = parsedQueryStringToString;
    }

    public String getQParser() {
        return qParser;
    }

    public void setQParser(String qParser) {
        this.qParser = qParser;
    }

    public Timing getTiming() {
        return timing;
    }

    public void setTiming(Timing timing) {
        this.timing = timing;
    }
}
