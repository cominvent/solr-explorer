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

package org.apache.solr.explorer.client.core.ui.consolepane;

import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.ResizeComposite;
import org.apache.solr.explorer.client.core.event.SolrCoreChangedEvent;
import org.apache.solr.explorer.client.core.image.SolrExplorerImages;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.EventHandler;
import org.gwtoolbox.ioc.core.client.annotation.Inject;
import org.gwtoolbox.ioc.core.client.annotation.InjectionType;
import org.gwtoolbox.widget.client.event.custom.EnableEvent;
import org.gwtoolbox.widget.client.event.custom.EnableHandler;
import org.gwtoolbox.widget.client.panel.LayoutUtils;
import org.gwtoolbox.widget.client.panel.layout.PanelLayout;
import org.gwtoolbox.widget.client.panel.layout.tab.TabLayout;

import java.util.List;

/**
 * @author Uri Boness
 */
@Component
public class ConsolePane extends ResizeComposite {

    private TabLayout tabs;
    private PanelLayout main;

    // injected
    private List<ConsoleTab> consoleTabs;

    public ConsolePane() {
        main = new PanelLayout("Console", SolrExplorerImages.Instance.get().monitorIcon().createImage());
        tabs = new TabLayout();
        main.setContent(tabs);
        LayoutUtils.fitParent(tabs);
        initWidget(main);
        addStyleName("ConsolePane");
    }

    @EventHandler
    public void handleSolrCoreChanged(SolrCoreChangedEvent event) {
        event.registerPostEventCommand(new Command() {
            public void execute() {
                tabs.clear();
                for (ConsoleTab tab : consoleTabs) {
                    if (tab.isActive()) {
                        final ConsoleTab theTab = tab;
                        tabs.addTab(tab.getName(), tab.getContent());
                        tabs.setTabEnabled(tab.getName(), tab.isEnabled());
                        tab.addEnableHandler(new EnableHandler() {
                            public void onEnableChanged(EnableEvent event) {
                                tabs.setTabEnabled(theTab.getName(), event.isEnabled());
                            }
                        });
                    }
                }
                if (tabs.getTabCount() > 0) {
                    tabs.setSelectedTab(consoleTabs.get(0).getName());
                }
            }
        });
    }

    public HandlerRegistration addCloseHandler(final CloseHandler handler) {
        main.setClosable(true);
        return main.addCloseHandler(handler);
    }

    public TabLayout getTabs() {
        return tabs;
    }

    //============================================ Setter/Getter Methods ===============================================

    @Inject(by = InjectionType.TYPE)
    public void setDebugPaneTabs(List<ConsoleTab> consoleTabs) {
        this.consoleTabs = consoleTabs;
    }
}
