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

package org.apache.solr.explorer.client.util.js;

/**
 * @author Uri Boness
 */
public class JavaScriptUtils {

    public native static void loadScript(String src, String callbackName, int timeout, JavaScriptCallback callback)/*-{

         window[callbackName] = function(jsonObj) {
            window[callbackName + "-done"] = true;
            callback.@org.apache.solr.explorer.client.util.js.JavaScriptCallback::onSuccess()();
        }

        var script = document.createElement("script");
        script.setAttribute("src", src);
        script.setAttribute("type", "text/javascript");

        setTimeout(function() {
            var done = window[callbackName + "-done"];

            // cleanup after each JSON request
            window[callbackName] = null;
            window[callbackName + "-done"] = null;

            if (!done) {
                // The request timed out
                callback.@org.apache.solr.explorer.client.util.js.JavaScriptCallback::onTimeout()();
            }

        }, timeout);


        document.body.appendChild(script);
    }-*/;

}
