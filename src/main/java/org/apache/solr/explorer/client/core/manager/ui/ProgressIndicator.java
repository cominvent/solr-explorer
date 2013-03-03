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

package org.apache.solr.explorer.client.core.manager.ui;

/**
 * A progress indicator is used to indicate to the user (in visual manner) that an on going process is progressing.
 *
 * @author Uri Boness
 */
public interface ProgressIndicator {

    /**
     * Sets the message to be displayed to the user.
     *
     * @param message The message to be displayed.
     */
    void setMessage(String message);

    /**
     * Hides this indicator.
     */
    void hide();

}
