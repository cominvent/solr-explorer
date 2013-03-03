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

import com.google.gwt.xml.client.Element;
import org.apache.solr.explorer.client.core.manager.configuration.Message;
import org.apache.solr.explorer.client.core.manager.configuration.Verification;
import org.apache.solr.explorer.client.core.manager.configuration.loader.AbstractConfigLoader;
import org.apache.solr.explorer.client.core.manager.logging.Logger;
import org.apache.solr.explorer.client.core.manager.solr.admin.Schema;
import org.apache.solr.explorer.client.core.model.configuration.ServerConfig;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.plugin.terms.model.config.TermsConfig;
import org.apache.solr.explorer.client.plugin.terms.model.context.TermsContext;
import org.gwtoolbox.commons.util.client.StringUtils;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Inject;

import java.util.List;

import static org.gwtoolbox.commons.xml.client.DOMUtils.*;

/**
 * @author Uri Boness
 */
@Component
public class TermsConfigLoader extends AbstractConfigLoader {

    private Logger logger;

    public boolean load(Element coreElement, SolrCoreConfiguration configuration) {
        if (logger.isInfoEnabled()) {
            logger.info("Checking for terms support configuration");
        }

        Element element = getSingleChild(coreElement, "terms");
        if (element == null) {
            return false;
        }

        TermsConfig config = new TermsConfig();
        configuration.setConfig(TermsConfig.class, config);

        if (logger.isInfoEnabled()) {
            logger.info("Found terms support configuration and loading...");
        }
        boolean enabled = getBooleanAttribute(element, "enabled", true);
        config.setEnabled(enabled);

        String uri = element.getAttribute("uri");
        if (uri == null) {
            logger.error("Invalid configuration!!!! &lt;terms&gt; element must have a \"uri\" attribute... disabling terms support");
            config.setEnabled(false);
            return false;
        }

        config.setUri(uri);

        TermsContext context = parseContext(element);
        if (context == null) {
            config.setEnabled(false);
            return true;
        }

        config.setDefaultContext(context);
        configuration.setConfig(TermsConfig.class, config);
        return true;
    }

    @Override
    public void postProcess(SolrCoreConfiguration configuration) {
        TermsConfig config = configuration.getConfig(TermsConfig.class);
        if (config == null) {
            return;
        }
        ServerConfig serverConfig = configuration.getConfig(ServerConfig.class);
        config.setUrl(serverConfig.getBaseUrl() + config.getUri());
    }

    @Override
    public void verify(Verification verification, SolrCoreConfiguration configuration, Schema schema) {
        TermsConfig config = configuration.getConfig(TermsConfig.class);
        if (config == null) {
            return;
        }
        List<String> fieldNames = config.getDefaultContext().getFieldNames();
        for (String fieldName : fieldNames) {
            if (!schema.hasField(fieldName)) {
               verification.registerError(new Message("terms", "Unknown field name", "Field '" + fieldName +
                        "' could not be found for solr core '" + configuration.getCoreName() + "'"));
            }
        }
    }

    //============================================ Setter/Getter Methods ===============================================

    @Inject
    public void setLogger(Logger logger) {
        this.logger = logger;
    }


    //================================================ Helper Methods ==================================================

    private TermsContext parseContext(Element element) {
        TermsContext context = new TermsContext();
        String fieldNamesValue = getSingleChildValue(element, "field-names");
        if (fieldNamesValue == null) {
            logger.info("checking element '" + element.getTagName() + "'");
            logger.error("Bad configuration!!!! &lt;field-names&gt; element is missing under the &lt;terms&gt; element");
            return null;
        }
        String[] fieldNames = StringUtils.delimitedListToStringArray(fieldNamesValue, ", ");
        for (String fieldName : fieldNames) {
            context.addFieldName(fieldName);
        }

        Element lowerBound = getSingleChild(element, "lower-bound");
        if (lowerBound != null) {
            context.setLowerBound(getTextValue(lowerBound, null));
            context.setLowerBoundIncluded(getBooleanAttribute(lowerBound, "include", context.isLowerBoundIncluded()));
        }
        Element upperBound = getSingleChild(element, "upper-bound");
        if (upperBound != null) {
            context.setUpperBound(getTextValue(upperBound, null));
            context.setUpperBoundIncluded(getBooleanAttribute(upperBound, "include", context.isUpperBoundIncluded()));
        }
        context.setMinCount(getSingleChildIntegerValue(element, "min-count", context.getMinCount()));
        context.setMaxCount(getSingleChildIntegerValue(element, "max-count", context.getMaxCount()));
        context.setLimit(getSingleChildIntegerValue(element, "limit", context.getLimit()));

        context.setRaw(getSingleChildBooleanValue(element, "raw", context.isRaw()));
        String sortName = getSingleChildValue(element, "sort", context.getSort().name());
        context.setSort(TermsContext.Sort.valueOf(sortName.toUpperCase()));

        return context;
    }


}
