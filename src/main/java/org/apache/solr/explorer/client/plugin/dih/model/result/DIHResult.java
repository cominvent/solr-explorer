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

package org.apache.solr.explorer.client.plugin.dih.model.result;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Uri Boness
 */
public class DIHResult {

    private String importantMessage;

    private final List<Message> messages = new ArrayList<Message>();

    public DIHResult() {
    }

    public String getImportantMessage() {
        return importantMessage;
    }

    public void setImportantMessage(String importantMessage) {
        this.importantMessage = importantMessage;
    }

    public void addMessage(String messageKey, String messageValue) {
        addMessage(new Message(messageKey, messageValue));
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public class Message {
        private final String key;
        private final String value;

        public Message(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
