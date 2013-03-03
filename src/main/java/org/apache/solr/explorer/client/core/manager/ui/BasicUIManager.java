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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import org.apache.solr.explorer.client.core.image.SolrExplorerImages;
import org.apache.solr.explorer.client.core.manager.configuration.Message;
import org.apache.solr.explorer.client.util.NoOpCallback;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.ComponentContainerAware;
import org.gwtoolbox.ioc.core.client.ComponentContainer;
import org.gwtoolbox.widget.client.notification.NotificationType;
import org.gwtoolbox.widget.client.panel.LayoutUtils;
import org.gwtoolbox.widget.client.popup.dialog.Dialog;
import org.gwtoolbox.widget.client.popup.dialog.MessageBox;

import static org.gwtoolbox.widget.client.panel.LayoutUtils.addGap;

import java.util.List;

/**
 * A default (and very simple) implementation of the {@link UIManager} interface.
 *
 * @author Uri Boness
 */
@Component(name = "uiManager")
public class BasicUIManager implements UIManager, ComponentContainerAware {

    private ComponentContainer componentContainer;

    /**
     * Constructs a new DefaultUIManager.
     */
    public BasicUIManager() {
    }

    /**
     * Shows error messages using the {@link Window#alert(String)} method.
     */
    public void showErrorMessage(String message) {
        MessageBox.show(NotificationType.ERROR, MessageBox.OptionType.OK, "Error Message", message, null);
    }

    public void showErrorMessage(String message, final AsyncCallback<Void> callback) {
        MessageBox.show(NotificationType.ERROR, MessageBox.OptionType.OK, "Error Message", message, new MessageBox.OptionCallback() {
            public void handle(MessageBox.Button button) {
                callback.onSuccess(null);
            }   
        });
    }

    public void showErrorMessages(String title, List<Message> messages) {
        showErrorMessages(title, messages, NoOpCallback.INSTANCE);
    }

    public void showErrorMessages(String title, List<Message> messages, AsyncCallback<Void> callback) {
        showErrorMessgeDialog(title, messages, callback);
    }

    /**
     * Shows information messages using the {@link Window#alert(String)} method.
     */
    public void showInfoMessage(String message) {
        MessageBox.show(NotificationType.INFO, MessageBox.OptionType.OK, "Error Message", message, null);
    }

    /**
     * Shows confirmation messages using the {@link Window#confirm(String)} method.
     */
    public void showConfirmMessage(String message, final AsyncCallback<Boolean> callback) {
        MessageBox.show(NotificationType.QUESTION, MessageBox.OptionType.OK_CANCEL, "Confirm", message, new MessageBox.OptionCallback() {
            public void handle(MessageBox.Button button) {
                callback.onSuccess(button == MessageBox.Button.OK);
            }
        });
    }

    /**
     * Creates, returns and displays a message indicator on the screen.
     *
     * @param message The initial message the indicator should display.
     * @return The shown progress indicator. Should be used as a handle to the indicator through its message can be
     *         changed or it can be hidden.
     */
    public ProgressIndicator showProgressIndicator(String message) {
        return new DefaultProgressIndicator(message);
    }

    /**
     * {@inheritDoc}
     */
    public ProgressIndicator showProgressIndicator(Widget widget, String message) {
        return new WidgetProgressIndicator(widget, message);
    }

    public ProgressIndicator showProgressIndicator(String widgetId, String message) {
        Widget widget = (Widget) componentContainer.getComponent(widgetId);
        return showProgressIndicator(widget, message);
    }

    public <T> AsyncCallback<T> createIndicatingCallack(String message, AsyncCallback<T> callback) {
        return new IndicatingCallback<T>(callback, showProgressIndicator(message));
    }

    public <T> AsyncCallback<T> createIndicatingCallack(Widget widget, String message, AsyncCallback<T> callback) {
        return new IndicatingCallback<T>(callback, showProgressIndicator(widget, message));
    }

    public <T> AsyncCallback<T> createIndicatingCallack(String widgetId, String message, AsyncCallback<T> callback) {
        return new IndicatingCallback<T>(callback, showProgressIndicator(widgetId, message));
    }

    public void setComponentContainer(ComponentContainer componentContainer) {
        this.componentContainer = componentContainer;
    }

    //============================================== Helper Methods ====================================================

   protected void showMessgeDialog(String title, Image icon, String message) {
        Label messageLabel = new Label(message);
        messageLabel.setStyleName("Message");
        showMessgeDialog(title, icon, messageLabel, null);
    }

    protected void showMessgeDialog(String title, Image icon, Widget widget, final AsyncCallback<Void> callback) {
        final DialogBox dialogBox = new DialogBox(false, true);
        dialogBox.setText(title);

        VerticalPanel content = new VerticalPanel();
        content.setStyleName("MessageDialogContent");
        icon.setStyleName("Icon");
        HorizontalPanel messageRow = new HorizontalPanel();
        messageRow.add(icon);
        messageRow.add(widget);
        messageRow.setCellVerticalAlignment(widget, HorizontalPanel.ALIGN_MIDDLE);
        content.add(messageRow);
        Button closeButton = new Button("Close", new ClickHandler() {
            public void onClick(ClickEvent event) {
                dialogBox.hide();
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }
        });
        LayoutUtils.addGap(content, "5px");
        content.add(closeButton);
        content.setCellHorizontalAlignment(closeButton, VerticalPanel.ALIGN_RIGHT);

        dialogBox.setWidget(content);
        dialogBox.center();
        dialogBox.show();
    }

    protected void showErrorMessgeDialog(String title, List<Message> messages, final AsyncCallback<Void> callback) {
        final Dialog dialogBox = new Dialog(false, true);
        dialogBox.setCaption("Error");

        VerticalPanel content = new VerticalPanel();
        content.setStyleName("MessageDialogContent");

        HorizontalPanel messageRow = new HorizontalPanel();
        Image icon = SolrExplorerImages.Instance.get().errorMessageIcon().createImage();
        icon.setStyleName("Icon");
        messageRow.add(icon);
        Label titleLabel = new Label(title);
        messageRow.add(titleLabel);
        content.add(messageRow);

        addGap(content, "15px");
        final ListBox listBox = new ListBox();
        listBox.setSize("100%", "70px");
        listBox.setVisibleItemCount(6);
        content.add(listBox);
        for (Message message : messages) {
            listBox.addItem(message.getTitle(), message.getBody());
        }

        addGap(content, "15px");
        final TextArea textArea = new TextArea();
        textArea.setSize("100%", "100px");
        textArea.setEnabled(false);
        content.add(textArea);

        listBox.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                int index = listBox.getSelectedIndex();
                String messageBody = index < 0 ? "" : listBox.getValue(index);
                textArea.setText(messageBody);
            }
        });

        addGap(content, "5px");
        Button closeButton = new Button("Close", new ClickHandler() {
            public void onClick(ClickEvent event) {
                dialogBox.hide();
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }
        });
        content.add(closeButton);
        content.setCellHorizontalAlignment(closeButton, VerticalPanel.ALIGN_RIGHT);

        dialogBox.setWidget(content);
        dialogBox.center();
        dialogBox.show();
    }

}

