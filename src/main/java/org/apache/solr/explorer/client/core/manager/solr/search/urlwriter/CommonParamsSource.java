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

package org.apache.solr.explorer.client.core.manager.solr.search.urlwriter;

import org.apache.solr.explorer.client.core.manager.RequestParams;
import org.apache.solr.explorer.client.core.model.context.CommonContext;
import org.apache.solr.explorer.client.util.collection.Properties;
import org.gwtoolbox.ioc.core.client.annotation.Component;

/**
 * @author Uri Boness
 */
@Component
public class CommonParamsSource extends AbstractParamsSource<CommonContext> {

    private static final String EXPLICIT = "explicit";

    public CommonParamsSource() {
        super("Common");
    }

    public Class<CommonContext> getContextClass() {
        return CommonContext.class;
    }

    public void addParams(RequestParams params, CommonContext context, Properties hints) {
        params.setParameter("version", context.getVersion());
        params.setParameter("wt", context.getWriterType());
        params.setParameter("indent", false);
        params.setParameter("echoParams", EXPLICIT);
    }
}
