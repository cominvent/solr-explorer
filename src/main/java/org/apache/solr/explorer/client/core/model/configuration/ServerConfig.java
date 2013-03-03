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

package org.apache.solr.explorer.client.core.model.configuration;

/**
 * @author Uri Boness
 */
public class ServerConfig {

    private final String baseUrl;

    private int connectionTimeout;

    private String searchUrl;
    private String lukeUrl;

    public ServerConfig(String baseUrl, int connectionTimeout) {
        this.baseUrl = baseUrl;
        this.connectionTimeout = connectionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUri(String searchUri) {
        searchUrl = baseUrl + searchUri;
    }

    public String getLukeUrl() {
        return lukeUrl;
    }

    public void setLukeUri(String adminUri) {
        lukeUrl = baseUrl + adminUri;
    }

}