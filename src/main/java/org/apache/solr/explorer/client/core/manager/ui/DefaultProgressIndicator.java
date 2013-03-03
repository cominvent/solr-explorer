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

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.Window;

/**
 * A simple implementation of a {@link ProgressIndicator} that shows a message on the middle-top part of the screen.
 *
 * @author Uri Boness
 */
public class DefaultProgressIndicator extends PopupPanel implements ProgressIndicator {

    private Label messageLabel;

    /**
     * Constructs a new MessageProgressIndicator with a default message.
     */
    public DefaultProgressIndicator() {
		this("Loading...");
	}

    /**
     * Constructs a new MessageProgressIndicator with a given initial message to be displayed.
     *
     * @param message The initial message to be displayed.
     */
    public DefaultProgressIndicator(String message) {
		super(false, true);
		messageLabel = new Label(message);
		messageLabel.setStyleName("Label");
		setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                int x = Window.getClientWidth()/2 - offsetWidth/2;
                setPopupPosition(x, 0);
            }
        });
        setWidget(messageLabel);
        setStyleName("DefaultProgressIndicator");
	}

    /**
     * Sets the message this indicator should display. Can be set while the indicator is already displayed.
     *
     * @param message The message this indicator should display.
     */
    public void setMessage(String message) {
		messageLabel.setText(message);
	}

}
