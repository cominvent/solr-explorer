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

package org.apache.solr.explorer.client.core.manager.solr.admin;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import org.apache.solr.explorer.client.core.manager.RequestManager;
import org.apache.solr.explorer.client.core.manager.RequestParams;
import org.apache.solr.explorer.client.core.manager.XmlResponse;
import org.apache.solr.explorer.client.core.manager.solr.search.urlwriter.CommonParamsSource;
import org.apache.solr.explorer.client.core.model.configuration.ServerConfig;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.core.model.context.CommonContext;
import org.apache.solr.explorer.client.util.Callback;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Inject;

import static org.apache.solr.explorer.client.util.SolrDomUtils.*;
import static org.gwtoolbox.commons.xml.client.DOMUtils.children;

/**
 * @author Uri Boness
 */
@Component(name = "solrAdminManager")
public class DefaultSolrAdminManager implements SolrAdminManager {

    private RequestManager requestManager;

    private CommonParamsSource commonUrlWriter;
    private CommonContext commonContext;

    public void loadSolrCore(final SolrCoreConfiguration coreConfig, final AsyncCallback<SolrCore> callback) {
        loadInfo(coreConfig, new Callback<SolrCoreInfo>() {
            public void onSuccess(SolrCoreInfo result) {
                SolrCore core = new SolrCore(coreConfig, result.getSchema(), result.getIndexInfo());
                callback.onSuccess(core);
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }

    //============================================ Setter/Getter Methods ===============================================

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

    private void loadInfo(SolrCoreConfiguration config, final AsyncCallback<SolrCoreInfo> callback) {
        ServerConfig serverConfig = config.getConfig(ServerConfig.class);
        RequestParams params = new RequestParams();
        commonUrlWriter.addParams(params, commonContext);
        params.setParameter("show", "schema");
        requestManager.send(serverConfig.getLukeUrl(), params, callback, new RequestManager.XmlResponseParser<SolrCoreInfo>() {
            public SolrCoreInfo parse(XmlResponse xmlResponse) throws Exception {
                return parseInfo(xmlResponse.getDocument());
            }
        });
    }

    private SolrCoreInfo parseInfo(Document document) throws Exception {
        Element element = document.getDocumentElement();

        SolrCoreInfo info = new SolrCoreInfo();

        // loading index info.
        Element indexElement = getLstChild(element, "index");
        if (indexElement != null) {
            IndexInfo indexInfo = new IndexInfo();
            indexInfo.setNumberOfDocuments(getIntChild(indexElement, "numDocs"));
            indexInfo.setDirectory(getStringChild(indexElement, "directory"));
            indexInfo.setHasDeletions(getBooleanChild(indexElement, "hasDeletions"));
            indexInfo.setIndexVersion(getLongChild(indexElement, "version"));
            indexInfo.setLastModified(getDateChild(indexElement, "lastModified"));
            indexInfo.setNumberOfTerms(getIntChild(indexElement, "numTerms"));
            indexInfo.setOptimized(getBooleanChild(indexElement, "optimized"));
            info.setIndexInfo(indexInfo);
        }

        // loading schema
        Element schemaElement = getLstChild(element, "schema");
        if (schemaElement == null) {
            throw new RuntimeException("Failed to load admin manager");
        }

        Schema schema = new Schema();
        schema.setUniqueKeyField(getStringChild(schemaElement, "uniqueKeyField"));
        schema.setDefaultSearchField(getStringChild(schemaElement, "defaultSearchField"));

        Element typesElement = getLstChild(schemaElement, "types");
        for (Element typeElement : children(typesElement)) {
            String typeName = typeElement.getAttribute("name");
            Type type = new Type();
            type.setName(typeName);
            type.setClassName(getStringChild(typeElement, "className"));
            type.setTokenized(getBooleanChild(typeElement, "tokenized"));
            schema.addTypes(type);
        }

        Element fieldsElement = getLstChild(schemaElement, "fields");
        for (Element fieldElement : children(fieldsElement)) {
            String fieldName = fieldElement.getAttribute("name");
            Field field = new Field();
            field.setName(fieldName);
            String typeName = getStringChild(fieldElement, "type");
            field.setType(schema.getType(typeName));
            String flags = getStringChild(fieldElement, "flags");
            field.setFlags(flags);
            boolean required = getBooleanChild(fieldElement, "required", false);
            field.setRequired(required);
            boolean uniqueKey = getBooleanChild(fieldElement, "uniqueKey", false);
            field.setUniqueKey(uniqueKey);
            schema.addFields(field);
        }
        info.setSchema(schema);

        return info;
    }
}
