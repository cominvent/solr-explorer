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

import com.google.gwt.xml.client.Element;
import org.gwtoolbox.commons.xml.client.DOMUtils;

import java.util.Date;

import static org.gwtoolbox.commons.xml.client.DOMUtils.getTextValue;

/**
 * /name
 * /name1/name2
 * /name1/name2[attribute="value"]
 *
 * @author Uri Boness
 */
public class SolrDomUtils {

    public static Element getLstChild(Element element, String name) {
        return getChild(element, "lst", name);
    }

    public static String getStringChild(Element element, String name) {
        Element child = getChild(element, "str", name);
        return child == null ? null : DOMUtils.getTextValue(child);
    }

    public static String getStringChild(Element element, String name, String defaultValue) {
        String value = getStringChild(element, name);
        return value == null ? defaultValue : value;
    }

    public static Integer getIntChild(Element element, String name) {
        Element child = getChild(element, "int", name);
        return child == null ? null : DOMUtils.getIntValue(child);
    }

    public static int getIntChild(Element element, String name, int defaultValue) {
        Integer value = getIntChild(element, name);
        return value == null ? defaultValue : value;
    }

    public static Long getLongChild(Element element, String name) {
        Element child = getChild(element, "long", name);
        return child == null ? null : DOMUtils.getLongValue(child);
    }

    public static long getLongChild(Element element, String name, long defaultValue) {
        Long value = getLongChild(element, name);
        return value == null ? defaultValue : value;
    }


    public static Double getDoubleChild(Element element, String name) {
        Element child = getChild(element, "double", name);
        return child == null ? null : DOMUtils.getDoubleValue(child);
    }

    public static Double getDoubleChild(Element element, String name, double defaultValue) {
        Double value = getDoubleChild(element, name);
        return value == null ? defaultValue : value;
    }

    public static Boolean getBooleanChild(Element element, String name) {
        Element child = getChild(element, "bool", name);
        return child == null ? null : DOMUtils.getBooleanValue(child);
    }

    public static Boolean getBooleanChild(Element element, String name, boolean defaultValue) {
        Boolean value = getBooleanChild(element, name);
        return value == null ? defaultValue : value;
    }

    public static Date getDateChild(Element element, String name) {
        return getDateChild(element, name, null);
    }

    public static Date getDateChild(Element element, String name, Date defaultValue) {
        Element child  = getChild(element, "date", name);
        return child == null ? null : SolrDateUtils.parse(getTextValue(child));
    }

    public static Element getChild(Element element, String tagName, String name) {
        for (Element child : DOMUtils.children(element, tagName)) {
            if (name.equals(child.getAttribute("name"))) {
                return child;
            }
        }
        return null;
    }

}
