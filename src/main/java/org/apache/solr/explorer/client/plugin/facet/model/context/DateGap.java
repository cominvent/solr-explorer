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

package org.apache.solr.explorer.client.plugin.facet.model.context;

import com.google.gwt.i18n.client.DateTimeFormat;
import org.apache.solr.explorer.client.util.SolrDateUtils;

import java.util.Date;

/**
 * @author Uri Boness
 */
public class DateGap {

    public enum Unit {
        SECONDS("dd-MM-yyyy HH:mm:ss"),
        MINUTES("dd-MM-yyyy HH:mm"),
        HOURS("dd-MM-yyyy HH:mm"),
        DAYS("dd-MM-yyyy"),
        MONTHS("MMMM yyyy"),
        YEARS("yyyy");

        private final DateTimeFormat dateFormat;

        Unit(String pattern) {
            this.dateFormat = DateTimeFormat.getFormat(pattern);
        }

        public String fixFormat(String solrDateString) {
            Date date = SolrDateUtils.parse(solrDateString);
            return dateFormat.format(date);
        }
    }

    public enum Sign {
        PLUS,
        MINUS
    }

    private final int count;
    private final Unit unit;

    public DateGap(int count, Unit unit) {
        this.count = count;
        this.unit = unit;
    }

    public int getCount() {
        return count;
    }

    public Unit getUnit() {
        return unit;
    }

}
