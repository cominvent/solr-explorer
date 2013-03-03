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

package org.apache.solr.explorer.client.core.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.event.ApplicationEvent;

/**
 * The status bar of the application that will be used for user feedbacks.
 *
 * @author Uri Boness
 */
@Component
public class StatusBarPane extends Composite {

    private Label messageLabel;

    private FlowPanel main;
    private FlowPanel buttons;

    public StatusBarPane() {
        messageLabel = new Label();
        messageLabel.addStyleName("floating-left");

        main = new FlowPanel();
        main.add(messageLabel);

        buttons = new FlowPanel();
        buttons.addStyleName("Buttons");
        main.add(buttons);

        initWidget(main);
        setStyleName("StatusBarPane");
    }

    protected void initUI() {
    }

    @EventHandler
    public void handle(ApplicationEvent event) {
        setMessage(event.getDescription());
    }

    public void addButton(Widget button) {
        buttons.add(button);
//        main.add(button);
    }

    //============================================== Setter/Getter =====================================================

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

}
