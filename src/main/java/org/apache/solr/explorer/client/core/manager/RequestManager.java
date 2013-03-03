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

package org.apache.solr.explorer.client.core.manager;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Uri Boness
 */
public interface RequestManager {

    Request send(String url, RequestParams params, AsyncCallback<XmlResponse> callback);

    Request send(String url, RequestParams params, int timeout, AsyncCallback<XmlResponse> callback);

    <T> Request send(String url, RequestParams params, AsyncCallback<T> callback, XmlResponseParser<T> responseParser);

    <T> Request send(String url, RequestParams params, int timeout, AsyncCallback<T> callback, XmlResponseParser<T> responseParser);

    Request loadTextResource(String name, AsyncCallback<String> callback);

    <T> Request loadTextResource(String name, AsyncCallback<T> callback, TextParser<T> parser);


    //================================================= Inner Classes ==================================================

    public static interface XmlResponseParser<T> {

        T parse(XmlResponse response) throws Exception;
    }

    public static interface TextParser<T> {

        T parse(String text) throws Exception ;

    }


}
