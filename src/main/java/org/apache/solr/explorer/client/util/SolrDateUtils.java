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

import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.*;

/**
 * @author Uri Boness
 */
public class SolrDateUtils {

    private final static DateTimeFormat[] DATE_FORMATS = new DateTimeFormat[]{
            DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'"),
            DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    };

    public static Date parse(String solrDateString) {
        for (DateTimeFormat format : DATE_FORMATS) {
            try {
                return format.parse(solrDateString);
            } catch (IllegalArgumentException iae) {
                // do nothing
            }
        }
        throw new IllegalArgumentException("Could not parse '" + solrDateString + "' as date");
    }

    public static String format(Date date) {
        return DATE_FORMATS[0].format(date);
    }

}
