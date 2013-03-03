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

package org.apache.solr.explorer.client.plugin.listview.model.config;

/**
 * @author Uri Boness
 */
public class ListViewConfig {

    private final static String UNKNOWN_VALUE = "<unknown>";

    private String titleFieldName;
    private String defaultTitle = UNKNOWN_VALUE;
    private String summaryFieldName;
    private String defaultSummary = UNKNOWN_VALUE;
    private int summaryLength = 400;
    private String urlFieldName;
    private Thumbnail thumbnail;
    private DetailsHtml detailsDetailsHtml;
    private ShowFieldsConfiguration showFieldsConfig;

    public String getTitleFieldName() {
        return titleFieldName;
    }

    public void setTitleFieldName(String titleFieldName) {
        this.titleFieldName = titleFieldName;
    }

    public String getDefaultTitle() {
        return defaultTitle;
    }

    public void setDefaultTitle(String defaultTitle) {
        if (defaultTitle == null) {
            defaultTitle = UNKNOWN_VALUE;
        }
        this.defaultTitle = defaultTitle;
    }

    public String getSummaryFieldName() {
        return summaryFieldName;
    }

    public void setSummaryFieldName(String summaryFieldName) {
        this.summaryFieldName = summaryFieldName;
    }

    public String getDefaultSummary() {
        return defaultSummary;
    }

    public int getSummaryLength() {
        return summaryLength;
    }

    public void setSummaryLength(int summaryLength) {
        this.summaryLength = summaryLength;
    }

    public void setDefaultSummary(String defaultSummary) {
        if (defaultSummary == null) {
            defaultSummary = UNKNOWN_VALUE;
        }
        this.defaultSummary = defaultSummary;
    }

    public String getUrlFieldName() {
        return urlFieldName;
    }

    public void setUrlFieldName(String urlFieldName) {
        this.urlFieldName = urlFieldName;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public DetailsHtml getDetailsHtmlSnippet() {
        return detailsDetailsHtml;
    }

    public void setDetailsHtmlSnippet(DetailsHtml detailsDetailsHtml) {
        this.detailsDetailsHtml = detailsDetailsHtml;
    }

    public void setShowFiledsConfiguration(ShowFieldsConfiguration showFieldsConfig) {
        this.showFieldsConfig = showFieldsConfig;
    }

    public boolean shouldShowField(String fieldName) {
        if (showFieldsConfig == null) {
            return true;
        }
        return showFieldsConfig.shouldShow(fieldName);
    }
}
