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

package org.apache.solr.explorer.client.plugin.debug.manager;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.solr.search.handler.SearchResultProcessor;
import org.apache.solr.explorer.client.core.model.context.SearchContext;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.core.plugin.PluginBase;
import org.apache.solr.explorer.client.plugin.debug.model.config.DebugConfig;
import org.apache.solr.explorer.client.plugin.debug.model.result.DebugResult;
import org.apache.solr.explorer.client.plugin.debug.model.result.HitExplain;
import org.apache.solr.explorer.client.plugin.debug.model.result.Timing;
import org.gwtoolbox.ioc.core.client.annotation.Component;

import static org.apache.solr.explorer.client.util.SolrDomUtils.*;
import static org.gwtoolbox.commons.xml.client.DOMUtils.*;

/**
 * @author Uri Boness
 */
@Component
public class DebugResultProcessor extends PluginBase implements SearchResultProcessor {

    public void init(SolrCore solrCore) {
        setActive(solrCore.getConfiguration().isActive(DebugConfig.class));
    }

    public void process(SearchContext context, Document document, SearchResult result) {
        Element element = getLstChild(document.getDocumentElement(), "debug");
        if (element == null) {
            return;
        }

        DebugResult debugResult = new DebugResult();

        String value = getStringChild(element, "rawquerystring");
        debugResult.setRawQueryString(value);
        value = getStringChild(element, "querystring");
        debugResult.setQueryString(value);
        value = getStringChild(element, "parsedquery");
        debugResult.setParsedQueryString(value);
        value = getStringChild(element, "parsedquery_toString");
        debugResult.setParsedQueryStringToString(value);
        value = getStringChild(element, "QParser");
        debugResult.setQParser(value);

        Element explain = getLstChild(element, "explain");
        for (Element child : children(explain)) {
            String key = child.getAttribute("name");
            String info = getTextValue(child);
            debugResult.addHitDebugInfo(key, new HitExplain(info));
        }

        Timing timing = new Timing();
        Element timingElement = getLstChild(element, "timing");
        timing.setTime(getDoubleChild(timingElement, "time"));
        timing.setPreparePhase(parseTimingPhase(timingElement, "prepare"));
        timing.setProcessPhase(parseTimingPhase(timingElement, "process"));
        debugResult.setTiming(timing);

        result.setPart(DebugResult.class, debugResult);
    }

    public void postProcess(SearchContext context, SearchResult result) {
    }


    //================================================ Helper Methods ==================================================

    private Timing.Phase parseTimingPhase(Element element, String phaseName) {
        Timing.Phase phase = new Timing.Phase();
        Element phaseElement = getLstChild(element, phaseName);
        for (Element child : children(phaseElement)) {
            String name = child.getAttribute("name");
            if ("time".equals(name)) {
                double time = getDoubleValue(child);
                phase.setTime(time);
                continue;
            }
            double time = getDoubleChild(child, "time");
            phase.addTime(name, time);
        }
        return phase;
    }
}
