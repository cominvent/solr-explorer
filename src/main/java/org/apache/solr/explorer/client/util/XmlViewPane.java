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

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.xml.client.*;
import org.gwtoolbox.commons.xml.client.DOMUtils;

import static org.gwtoolbox.commons.xml.client.DOMUtils.getTextValue;

/**
 * @author Uri Boness
 */
public class XmlViewPane extends Composite {

    private final FlowPanel main;

    public XmlViewPane() {
        main = new FlowPanel();
        initWidget(main);
        setStylePrimaryName("XmlViewPane");
    }

    public void setXmlText(String xml) {
        setDocument(XMLParser.parse(xml));
    }

    public void setDocument(Document document) {
        render(document.getDocumentElement());
    }

    //================================================ Helper Methods ==================================================

    private void render(Element element) {
        main.clear();
        Widget content = createElementWidget(element);
        main.add(content);
    }

    private Widget createElementWidget(Element element) {

        DisclosurePanel panel = new DisclosurePanel();
        panel.setAnimationEnabled(true);
        panel.setOpen(true);

        StringBuilder stringBuilder = new StringBuilder("&lt;<span class='tagName'>").append(element.getTagName()).append("</span>");
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Attr attr = (Attr) attributes.item(i);
            String attrName = attr.getName();
            String value = attr.getValue();
            stringBuilder.append(" <span class='attrName'>").append(attrName).append("=</span>")
                    .append("<span class='attrValue'>\"").append(value).append("\"</span>");
        }

        if (!element.hasChildNodes()) {
            stringBuilder.append("/&gt;");
            return new HTML(stringBuilder.toString());
        }

        stringBuilder.append("&gt;");

        String textValue = getTextValue(element, null);
        if (textValue != null && textValue.trim().length() > 0) {
            stringBuilder.append(textValue).append("&lt;/<span class='tagName'>").append(element.getTagName()).append("</span>&gt;");
            return new HTML(stringBuilder.toString());
        }

        HTML header = new HTML(stringBuilder.toString());
        panel.setHeader(header);

        FlowPanel content = new FlowPanel();
        content.getElement().getStyle().setPaddingLeft(30, Style.Unit.PX);
        panel.setContent(content);

        for (Element child : DOMUtils.children(element)) {
            Widget childPane = createElementWidget(child);
            content.add(childPane);
        }

        HTML footer = new HTML(new StringBuilder("&lt;/<span class='tagName'>").append(element.getTagName()).append("</span>&gt;").toString());
        footer.getElement().getStyle().setMarginLeft(-34, Style.Unit.PX);
        content.add(footer);

        return panel;
    }
}
