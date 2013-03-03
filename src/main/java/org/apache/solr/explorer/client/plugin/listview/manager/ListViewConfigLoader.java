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

package org.apache.solr.explorer.client.plugin.listview.manager;

import org.apache.solr.explorer.client.plugin.listview.model.config.ShowFieldsConfiguration;
import org.apache.solr.explorer.client.plugin.listview.model.config.Thumbnail;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.core.manager.configuration.loader.AbstractConfigLoader;
import org.apache.solr.explorer.client.core.manager.configuration.Verification;
import org.apache.solr.explorer.client.core.manager.configuration.Message;
import org.apache.solr.explorer.client.core.manager.solr.admin.Schema;
import org.apache.solr.explorer.client.plugin.listview.model.config.DetailsHtml;
import org.apache.solr.explorer.client.plugin.listview.model.config.ListViewConfig;
import static org.gwtoolbox.commons.xml.client.DOMUtils.getSingleChild;
import static org.gwtoolbox.commons.xml.client.DOMUtils.getSingleChildValue;
import static org.gwtoolbox.commons.xml.client.DOMUtils.getSingleChildAttribute;
import static org.gwtoolbox.commons.xml.client.DOMUtils.getIntegerAttribute;
import org.gwtoolbox.commons.util.client.template.Template;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.Node;

/**
 * @author Uri Boness
 */
@Component
public class ListViewConfigLoader extends AbstractConfigLoader {

    private final static String ERROR_CATEGORY = "Hit Rendering";

    public boolean load(Element coreElement, SolrCoreConfiguration configuration) {
        Element element = getSingleChild(coreElement, "list-view");
        if (element == null) {
            return false;
        }

        ListViewConfig listViewConfig = new ListViewConfig();

        String titleField = getSingleChildValue(element, "title-field");
        listViewConfig.setTitleFieldName(titleField);
        String defaultTitle = getSingleChildAttribute(element, "title-field", "default");
        listViewConfig.setDefaultTitle(defaultTitle);

        Element summaryFieldElement = getSingleChild(element, "summary-field");
        String summaryField = getSingleChildValue(element, "summary-field");
        listViewConfig.setSummaryFieldName(summaryField);
        String defaultSummary = getSingleChildAttribute(element, "summary-field", "default");
        listViewConfig.setDefaultSummary(defaultSummary);
        int maxLength = getIntegerAttribute(summaryFieldElement, "maxLength", 400);
        listViewConfig.setSummaryLength(maxLength);
        
        String urlField = getSingleChildValue(element, "url-field");
        listViewConfig.setUrlFieldName(urlField);

        Element showFields = getSingleChild(element, "show-fields");
        if (showFields != null) {
            ShowFieldsConfiguration showFieldsConfig = new ShowFieldsConfiguration();
            String includes = getSingleChildValue(showFields, "include");
            if (includes != null) {
                String[] fields = includes.split(",");
                showFieldsConfig.setIncludes(fields);
            }
            String excludes = getSingleChildValue(showFields, "exclude");
            if (excludes != null) {
                String[] fields = excludes.split(",");
                showFieldsConfig.setExcludes(fields);
            }
            listViewConfig.setShowFiledsConfiguration(showFieldsConfig);
        }
        Element thumbnailElement = getSingleChild(element, "thumbnail");
        if (thumbnailElement != null) {
            String width = thumbnailElement.getAttribute("width");
            String height = thumbnailElement.getAttribute("height");
            String templateString = getSingleChildValue(thumbnailElement, "url-template");
            Template template = Template.compile(templateString);
            listViewConfig.setThumbnail(new Thumbnail(width, height, template));
        }

        Element detailsElement = getSingleChild(element, "details-html");
        if (detailsElement != null) {
            String caption = detailsElement.getAttribute("caption");
            Template template = Template.compile(getTextValue(detailsElement));
            DetailsHtml detailsHtml = new DetailsHtml(template, caption);
            listViewConfig.setDetailsHtmlSnippet(detailsHtml);
        }

        configuration.setConfig(ListViewConfig.class, listViewConfig);

        return true;
    }

    @Override
    public void verify(Verification verification, SolrCoreConfiguration configuration, Schema schema) {
        ListViewConfig listViewConfig = configuration.getConfig(ListViewConfig.class);
        String field = listViewConfig.getSummaryFieldName();
        if (!schema.hasField(field)) {
            String title = "Unknown summary field '" + field + "'";
            verification.registerError(new Message(ERROR_CATEGORY, title, title));
        }
        field = listViewConfig.getTitleFieldName();
        if (!schema.hasField(field)) {
            String title = "Unknown title field '" + field + "'";
            verification.registerError(new Message(ERROR_CATEGORY, title, title));
        }
        field = listViewConfig.getUrlFieldName();
        if (field != null && !schema.hasField(field)) {
            String title = "Unknown url field '" + field + "'";
            verification.registerError(new Message(ERROR_CATEGORY, title, title));
        }
    }

    //================================================ Helper Methods ==================================================

    private String getTextValue(Element element) {
        NodeList nodes = element.getChildNodes();
        for (int i=0; i<nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                return node.getNodeValue();
            }
        }
        return "";
    }
}
