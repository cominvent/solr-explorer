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
import com.google.gwt.user.client.ui.Widget;
import org.gwtoolbox.widget.client.panel.CenterPanel;

/**
 * A simple implementation of a {@link nl.jteam.solr.explorer.client.manager.ui.ProgressIndicator} that shows a message on the middle-top part of the screen.
 *
 * @author Uri Boness
 */
public class WidgetProgressIndicator extends PopupPanel implements ProgressIndicator {

    private Label messageLabel;

    /**
     * Constructs a new MessageProgressIndicator with a default message.
     *
     * @param widget The widget this indicator is for.
     */
    public WidgetProgressIndicator(Widget widget) {
		this(widget, "Loading...");
	}

    /**
     * Constructs a new MessageProgressIndicator with a given initial message to be displayed.
     *
     * @param widget The widget this indicator is for.
     * @param message The initial message to be displayed.
     */
    public WidgetProgressIndicator(final Widget widget, String message) {
		super(false, true);
		messageLabel = new Label(message);
		messageLabel.setStyleName("Label");
		setPopupPositionAndShow(new PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                int left = widget.getAbsoluteLeft();
                int top = widget.getAbsoluteTop();
                setPopupPosition(left, top);
            }
        });

        CenterPanel centerPanel = new CenterPanel(messageLabel);
        centerPanel.setSize(widget.getOffsetWidth() + "px", widget.getOffsetHeight() + "px");
        setWidget(centerPanel);
        setStyleName("WidgetProgressIndicator");
        addStyleName("transparent-50");
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