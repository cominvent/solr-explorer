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

package org.apache.solr.explorer.client.plugin.dih.ui;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import static org.gwtoolbox.widget.client.panel.LayoutUtils.hBuilder;
import org.apache.solr.explorer.client.plugin.dih.manager.DIHManager;
import org.apache.solr.explorer.client.plugin.dih.model.result.DIHResult;
import org.apache.solr.explorer.client.util.Callback;

/**
 * @author Uri Boness
 */
public class DIHDialog extends DialogBox {

    private Timer statusTimer;
    private VerticalPanel main;

    private DIHManager dihManager;

    public DIHDialog(final DIHManager dihManager) {
        super(true, false);
        this.dihManager = dihManager;

        main = new VerticalPanel();
        setText("Data Import Result");

        statusTimer = new Timer() {
            public void run() {
                dihManager.getStatus(new Callback<DIHResult>() {
                    public void onSuccess(DIHResult result) {
                        reset(result);
                    }
                });
            }
        };

        addCloseHandler(new CloseHandler<PopupPanel>() {
            public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                statusTimer.cancel();
            }
        });

        setWidget(main);
    }

    public void fullImport() {
        dihManager.executeFullImport(new Callback<DIHResult>() {
            public void onSuccess(DIHResult result) {
                reset(result);
            }
        });
        pollStatus();
    }

    public void deltaImport() {
        dihManager.executeDeltaImport(new Callback<DIHResult>() {
            public void onSuccess(DIHResult result) {
                reset(result);
                pollStatus();
            }
        });
    }

    public void pollStatus() {
        statusTimer.cancel();
        statusTimer.scheduleRepeating(1000);
    }

    public void reloadConfig() {
        dihManager.reloadConfig(new Callback<DIHResult>() {
            public void onSuccess(DIHResult result) {
                reset(result);
            }
        });
    }

    private void reset(DIHResult result) {
        main.clear();
        String importantMessage = result.getImportantMessage();
        if (importantMessage != null && importantMessage.length() != 0 && !"\"\"".equals(importantMessage)) {
            main.add(hBuilder().add(new HTML("<b>" + importantMessage + "</b>")).getPanel());
        }
        for (DIHResult.Message message : result.getMessages()) {
            main.add(hBuilder().add(new HTML("<b>" + message.getKey() + ":</b>")).addGap("3px").add(new Label(message.getValue())).getPanel());
        }
        center();
    }

}
