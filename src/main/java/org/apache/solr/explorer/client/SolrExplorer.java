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

package org.apache.solr.explorer.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.apache.solr.explorer.client.core.event.ConfigurationLoadedEvent;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.manager.configuration.ConfigurationManager;
import org.apache.solr.explorer.client.core.manager.configuration.Verification;
import org.apache.solr.explorer.client.core.manager.solr.admin.Schema;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrAdminManager;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.manager.ui.UIManager;
import org.apache.solr.explorer.client.core.model.configuration.Configuration;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.util.Callback;
import org.gwtoolbox.ioc.core.client.ComponentContainer;
import org.gwtoolbox.ioc.core.client.support.AbstractComponentContainerEntryPoint;
import org.gwtoolbox.widget.client.button.SimpleButton;
import org.gwtoolbox.widget.client.popup.Popup;
import org.gwtoolbox.widget.client.popup.dialog.Dialog;

/**
 * @author Uri Boness
 */
public class SolrExplorer extends AbstractComponentContainerEntryPoint {

    protected ComponentContainer createComponentContainer() {
        return GWT.create(SolrExplorerContainer.class);
    }

    @Override
    protected void afterInitialization() {

        // we first need to load the configuration. Only after the configuration is loaded properly, we can load the
        // admin manager (this due to the dependency of the amdin manager in some of the configurations).
        loadConfiguration(new Callback<Configuration>() {
            public void onSuccess(Configuration configuration) {

                setSplashMessage("");

                // notifying the configuration was loaded.
                ConfigurationLoadedEvent event = new ConfigurationLoadedEvent(SolrExplorer.this, configuration);
                getComponentContainer().getApplicationEventMulticaster().notifyEvent(event);

                showCoreSelectionDialog(configuration);
            }
        });
    }

    protected void showCoreSelectionDialog(final Configuration configuration) {
        showCoreSelectionDialog(configuration, new Callback<SolrCoreConfiguration>() {
            public void onSuccess(final SolrCoreConfiguration solrCoreConfiguration) {
                handleSolrCore(solrCoreConfiguration, configuration);
            }
        });
    }

    protected void handleSolrCore(final SolrCoreConfiguration solrCoreConfiguration, final Configuration configuration) {
        loadSolrCore(solrCoreConfiguration, new AsyncCallback<SolrCore>() {

                    public void onFailure(Throwable caught) {
                        GWT.log("Could not load solr core ('" + solrCoreConfiguration.getCoreName() + "'): ", caught);
                        UIManager uiManager = (UIManager) getComponentContainer().getComponent("uiManager");
                        uiManager.showErrorMessage("Could not load solr core ('" + solrCoreConfiguration.getCoreName() + "'): " + caught.getMessage(), new Callback<Void>() {
                            public void onSuccess(Void result) {
                                showCoreSelectionDialog(configuration);
                            }
                        });
                    }

                    public void onSuccess(SolrCore core) {
                        Verification verification = verifyConfiguration(solrCoreConfiguration, core.getSchema());
                        if (verification.hasErrors()) {
                            UIManager uiManager = (UIManager) getComponentContainer().getComponent("uiManager");
                            String title = "Invalid configuration for solr core '" + solrCoreConfiguration.getCoreName() + "'";
                            uiManager.showErrorMessages(title, verification.getErrors(), new Callback<Void>() {
                                public void onSuccess(Void result) {
                                    showCoreSelectionDialog(configuration);
                                }
                            });
                            return;
                        }

                        // on any exception that will be thrown by notifying the event (should never happen, but just in case)
                        try {

                            SolrCoreChangedEvent event = new SolrCoreChangedEvent(SolrExplorer.this, core);
                            getComponentContainer().getApplicationEventMulticaster().notifyEvent(event);
                            hideSplash();
                            Widget mainPane = (Widget) getComponentContainer().getComponent("mainPane");
                            mainPane.setVisible(true);

                        } catch (Throwable t) {
                            onFailure(t);
                        }
                    }
                });
    }

    protected void loadConfiguration(final AsyncCallback<Configuration> callback) {
        ConfigurationManager confManager = (ConfigurationManager) componentContainer.getComponent("configurationManager");
        setSplashMessage("Loading configuration...");
        confManager.load(callback);
    }

    protected void loadSolrCore(SolrCoreConfiguration configuration, AsyncCallback<SolrCore> callback) {
        setSplashMessage("Loading solr core '" + configuration.getCoreName() + "'...");
        SolrAdminManager solrAdminManager = (SolrAdminManager) getComponentContainer().getComponent("solrAdminManager");
        solrAdminManager.loadSolrCore(configuration, callback);
    }

    protected void showCoreSelectionDialog(final Configuration configuration, final AsyncCallback<SolrCoreConfiguration> callback) {
        final Dialog dialog = new Dialog(false, true);
        dialog.setStyleName("Dialog");
        dialog.setCaption("Solr Cores");
        VerticalPanel main = new VerticalPanel();
        main.setWidth("100%");
        dialog.setWidthPx(300);

        Label label = new Label("Please select the solr core to connect to");
        main.add(label);
        main.setCellHeight(label, "25px");

        final ListBox listBox = new ListBox(false);
        for (String coreName : configuration.getCoreNames()) {
            listBox.addItem(coreName, coreName);
        }
        listBox.setVisibleItemCount(5);
        listBox.setSelectedIndex(-1);
        listBox.setWidth("90%");
        listBox.setHeight("100%");
        main.add(listBox);
        main.setCellHorizontalAlignment(listBox, VerticalPanel.ALIGN_CENTER);
        main.setCellHeight(listBox, "120px");

        final SimpleButton okButton = new SimpleButton("OK", new ClickHandler() {
            public void onClick(ClickEvent event) {
                dialog.hide();
                int index = listBox.getSelectedIndex();
                String coreName = listBox.getValue(index);
                callback.onSuccess(configuration.getCoreConfiguration(coreName));
            }
        });
        okButton.setEnabled(false);
        main.add(okButton);
        main.setCellHorizontalAlignment(okButton, VerticalPanel.ALIGN_CENTER);
        main.setCellHeight(okButton, "25px");

        listBox.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                GWT.log("Enabling button = " + (listBox.getSelectedIndex() >= 0), null);                
                okButton.setEnabled(listBox.getSelectedIndex() >= 0);
            }
        });

        dialog.setWidget(main);

        dialog.center();

        final HandlerRegistration windowResizeHandlerRegistration = Window.addResizeHandler(new ResizeHandler() {
            public void onResize(ResizeEvent event) {
                dialog.center();
            }
        });

        dialog.addCloseHandler(new CloseHandler<Popup>() {
            public void onClose(CloseEvent<Popup> closeEvent) {
                windowResizeHandlerRegistration.removeHandler();
            }
        });

    }

    private Verification verifyConfiguration(SolrCoreConfiguration solrCoreConfig, Schema schema) {
        ConfigurationManager confManager = (ConfigurationManager) componentContainer.getComponent("configurationManager");
        Verification verification = new Verification();
        setSplashMessage("Verifying configuration for solr core '" + solrCoreConfig.getCoreName() + "'...");
        confManager.verifySolrCoreConfiguration(verification, solrCoreConfig, schema);
        return verification;
    }

    private void setSplashMessage(String message) {
        Element splashText = DOM.getElementById("splashText");
        splashText.setInnerText(message);
    }

    private void hideSplash() {
        Element splash = DOM.getElementById("splash");
        UIObject.setVisible(splash, false);
    }

    //================================================= Inner Classes ==================================================

    private class VerificationResult {

        private String message;

        private VerificationResult() {
            this(null);
        }

        private VerificationResult(String message) {
            this.message = message;
        }

        public boolean isSuccessful() {
            return message == null;
        }

        public String getMessage() {
            return message;
        }
    }
}
