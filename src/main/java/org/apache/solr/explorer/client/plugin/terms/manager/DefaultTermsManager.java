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

package org.apache.solr.explorer.client.plugin.terms.manager;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Element;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.RequestManager;
import org.apache.solr.explorer.client.core.manager.RequestParams;
import org.apache.solr.explorer.client.core.manager.XmlResponse;
import org.apache.solr.explorer.client.core.manager.solr.search.urlwriter.CommonParamsSource;
import org.apache.solr.explorer.client.core.model.context.CommonContext;
import org.apache.solr.explorer.client.plugin.terms.model.config.TermsConfig;
import org.apache.solr.explorer.client.plugin.terms.model.context.TermsContext;
import org.apache.solr.explorer.client.plugin.terms.model.result.TermEntry;
import org.apache.solr.explorer.client.plugin.terms.model.result.TermResult;
import org.apache.solr.explorer.client.plugin.terms.model.result.TermsResult;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Inject;

import static org.apache.solr.explorer.client.util.SolrDomUtils.getLstChild;
import static org.gwtoolbox.commons.xml.client.DOMUtils.children;
import static org.gwtoolbox.commons.xml.client.DOMUtils.getIntValue;

/**
 * @author Uri Boness
 */
@Component(name = "termsManager")
public class DefaultTermsManager implements TermsManager {

    private TermsConfig termsConfig;
    private TermsContext termsContext;

    private CommonParamsSource commonUrlWriter;
    private CommonContext commonContext;

    private RequestManager requestManager;

    public void getTerms(AsyncCallback<TermsResult> result) {
        requestManager.send(termsConfig.getUrl(), buildParams(), result, new RequestManager.XmlResponseParser<TermsResult>() {
            public TermsResult parse(XmlResponse xmlResponse) throws Exception {
                TermsResult result = new TermsResult();
                Element termsElement = getLstChild(xmlResponse.getDocument().getDocumentElement(), "terms");
                for (Element fieldElement : children(termsElement)) {
                    String fieldName = fieldElement.getAttribute("name");
                    TermResult termResult = new TermResult(fieldName);
                    for (Element termElement : children(fieldElement)) {
                        String term = termElement.getAttribute("name");
                        int count = getIntValue(termElement);
                        termResult.addEntry(new TermEntry(term, count));
                    }
                    result.addTermResult(termResult);
                }
                return result;
            }
        });
    }

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        termsConfig = event.getSolrCore().getConfiguration().getConfig(TermsConfig.class);
    }
    

    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setTermsContext(TermsContext termsContext) {
        this.termsContext = termsContext;
    }

    @Inject
    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Inject
    public void setCommonUrlWriter(CommonParamsSource commonUrlWriter) {
        this.commonUrlWriter = commonUrlWriter;
    }

    @Inject
    public void setCommonContext(CommonContext commonContext) {
        this.commonContext = commonContext;
    }

    //================================================ Helper Methods ==================================================

    private RequestParams buildParams() {
        RequestParams params = new RequestParams()
                .addParameter("terms", true);

        for (String fieldName : termsContext.getFieldNames()) {
            params.addParameter("terms.fl", fieldName);
        }

        commonUrlWriter.addParams(params, commonContext);
        
        String prefix = termsContext.getPrefix();
        if (prefix != null) {
            params.setParameter("terms.prefix", prefix);
        }

        String lowerBound = termsContext.getLowerBound();
        if (lowerBound != null) {
            params.setParameter("terms.lower", lowerBound)
                .setParameter("terms.lower.incl", termsContext.isLowerBoundIncluded());
        }

        String upperBound = termsContext.getUpperBound();
        if (upperBound != null) {
            params.setParameter("terms.upper", upperBound)
                .addParameter("terms.upper.incl", termsContext.isUpperBoundIncluded());
        }

        params.setParameter("terms.mincount", termsContext.getMinCount())
                .addParameter("terms.maxcount", termsContext.getMaxCount())
                .addParameter("limit", termsContext.getLimit())
                .addParameter("raw", termsContext.isRaw())
                .addParameter("sort", termsContext.getSort().name().toLowerCase());

        return params;
    }

}
