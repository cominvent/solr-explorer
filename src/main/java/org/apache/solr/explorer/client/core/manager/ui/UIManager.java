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


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

import org.apache.solr.explorer.client.core.manager.configuration.Message;

/**
 * Manager all application wide UI aspect.
 *
 * @author Uri Boness
 */
public interface UIManager {

    /**
     * Shows error messages.
     *
     * @param message The error message to be shown.
     */
    void showErrorMessage(String message);

    void showErrorMessage(String message, AsyncCallback<Void> callback);

    void showErrorMessages(String title, List<Message> messages);

    void showErrorMessages(String title, List<Message> messages, AsyncCallback<Void> callback);

    /**
     * Shows information massage.
     *
     * @param message The information message to be shown.
     */
    void showInfoMessage(String message);

    /**
     * Shows a confirmation dialog.
     *
     * @param message The message to be displayed on the dialog.
     * @param callback The callback to handle the user input.
     */
    void showConfirmMessage(String message, AsyncCallback<Boolean> callback);

    /**
     * Shows a {@link ProgressIndicator progress indicator} on screen.
     *
     * @param message The initial message to be displayed by the progress indicator.
     * @return The shown progress indicator.
     */
    ProgressIndicator showProgressIndicator(String message);

    /**
     * Shows a progress indicator for the given widget.
     *
     * @param widget The widget that should display the progress indicator
     * @param message The message to be displayed by the indicator.
     * @return The shown progress indicator.
     */
    ProgressIndicator showProgressIndicator(Widget widget, String message);

    /**
     * Shows a progress indicator for the given widget.
     *
     * @param widgetId The id of the widget that should display the progress indicator (the id as defined in the container)
     * @param message The message to be displayed by the indicator.
     * @return The shown progress indicator.
     */
    ProgressIndicator showProgressIndicator(String widgetId, String message);

    <T> AsyncCallback<T> createIndicatingCallack(String message, AsyncCallback<T> callback);
    <T> AsyncCallback<T> createIndicatingCallack(Widget widget, String message, AsyncCallback<T> callback);
    <T> AsyncCallback<T> createIndicatingCallack(String widgetId, String message, AsyncCallback<T> callback);

}

