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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.image.SolrExplorerImages;
import org.apache.solr.explorer.client.core.ui.consolepane.ConsolePane;
import org.apache.solr.explorer.client.core.ui.resultspane.ResultsPane;
import org.apache.solr.explorer.client.core.ui.searchpane.SearchPane;
import org.gwtoolbox.ioc.core.client.annotation.*;
import org.gwtoolbox.widget.client.button.ToggleToolButton;
import org.gwtoolbox.widget.client.panel.layout.DockLayout;
import org.gwtoolbox.widget.client.panel.layout.drawer.Drawer;
import org.gwtoolbox.widget.client.panel.layout.drawer.DrawerLayout;

import java.util.List;

/**
 * @author Uri Boness
 */
@Component
@RootPanelBinding
public class MainPane extends Composite {

    private DockLayout main;
    private SearchPane searchPane;
    private StatusBarPane statusBarPane;
    private ResultsPane resultsPane;
    private ConsolePane consolePane;
    private DrawerLayout drawers;

    private List<DrawerSource> drawerSources;

    public MainPane() {
        main = new DockLayout();
        initWidget(main);
        setStyleName("MainPane");
        setVisible(false);
    }

    @Initializer
    public void init() {

        drawers = new DrawerLayout();

        drawers.setContent(resultsPane);
        resultsPane.setSize("100%", "100%");

        final ToggleToolButton debugButton = new ToggleToolButton(SolrExplorerImages.Instance.get().monitorIcon().createImage());
        statusBarPane.setHeight("100%");
        statusBarPane.addButton(debugButton);
        debugButton.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                if (debugButton.isDown()) {
                    drawers.showDrawer("console");
                } else {
                    drawers.hideDrawer(DrawerLayout.Position.BOTTOM);
                }
            }
        });

        consolePane.setSize("100%", "100%");
        consolePane.addCloseHandler(new CloseHandler() {
            public void onClose(CloseEvent closeEvent) {
                debugButton.toggle();
            }
        });

        main.addNorth(searchPane, 100);
        searchPane.setSize("100%", "100%");
        main.addSouth(statusBarPane, 25);
        main.add(drawers);
    }

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        event.registerPostEventCommand(new Command() {
            public void execute() {
//                drawers.clear();

                Drawer consoleDrawer = new Drawer("console", "Console", consolePane, DrawerLayout.Position.BOTTOM).setPreferredSize(300).setButtonless(true);
                drawers.addDrawer(consoleDrawer);

                for (DrawerSource source : drawerSources) {
                    if (source.isActive()) {
                        for (Drawer drawer : source.getDrawers()) {
                            if (drawer.getPosition() == DrawerLayout.Position.BOTTOM) {
                                GWT.log("ERROR!!! Solr Explorer drawers don't support bottom positioning. Drawer '" + drawer.getName() + "' is possitioned at the bottom", null);
                            } else {
                                drawers.addDrawer(drawer);
                            }
                        }
                    }
                }
            }
        });
    }

    //============================================== Setter/Getter =====================================================

    @Inject
    public void setSearchPane(SearchPane searchPane) {
        this.searchPane = searchPane;
    }

    @Inject
    public void setStatusBarPane(StatusBarPane statusBarPane) {
        this.statusBarPane = statusBarPane;
    }

    @Inject
    public void setResultsPane(ResultsPane resultsPane) {
        this.resultsPane = resultsPane;
    }

    @Inject
    public void setConsolePane(ConsolePane consolePane) {
        this.consolePane = consolePane;
    }

    @Inject(by = InjectionType.TYPE)
    public void setDrawerSources(List<DrawerSource> drawerSources) {
        this.drawerSources = drawerSources;
    }
}