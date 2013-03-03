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

package org.apache.solr.explorer.client.core.ui.consolepane.log;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import org.apache.solr.explorer.client.core.manager.logging.AbstractLogger;
import org.apache.solr.explorer.client.core.manager.logging.Logger;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.ui.consolepane.ConsoleTab;
import org.gwtoolbox.commons.util.client.template.MapModel;
import org.gwtoolbox.commons.util.client.template.Template;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.ConfiguredProperty;
import org.gwtoolbox.ioc.core.client.annotation.Initializer;
import org.gwtoolbox.ioc.core.client.annotation.Order;
import org.gwtoolbox.widget.client.button.SimpleButton;
import org.gwtoolbox.widget.client.event.custom.EnableEvent;
import org.gwtoolbox.widget.client.event.custom.EnableHandler;
import org.gwtoolbox.widget.client.list.DropDownBox;

import static org.gwtoolbox.widget.client.panel.LayoutUtils.addGap;

/**
 * @author Uri Boness
 */
@Component
@Order(1)
public class LogPane extends ResizeComposite implements ConsoleTab {

    private final static int MAX_ROLL_ENTRIES = 100;

    private final static Template logTemplate = Template.compile("<span style='color:${color}'>[${level}]:</span>&nbsp;${message}");

    private FlowPanel logEntries;
    private ScrollPanel logEntriesScrollPanel;
    private DropDownBox<AbstractLogger.Level> levelDropDown;

    private final Logger logger;

    public LogPane() {

        logger = new AbstractLogger() {
            public void log(Level level, String message, Throwable t) {
                LogPane.this.log(level, message, t);
            }
        };

        HorizontalPanel toolbar = new HorizontalPanel();
        toolbar.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        toolbar.add(new Label("Level:"));
        addGap(toolbar, "3px");
        levelDropDown = new DropDownBox<AbstractLogger.Level>();
        for (AbstractLogger.Level level : AbstractLogger.Level.values()) {
            levelDropDown.addOption(level.name(), level);
        }
        levelDropDown.addSelectionHandler(new SelectionHandler<AbstractLogger.Level>() {
            public void onSelection(SelectionEvent<Logger.Level> event) {
                logger.setLevel(event.getSelectedItem());
            }
        });
        toolbar.add(levelDropDown);
        addGap(toolbar, "10px");
        SimpleButton clearButton = new SimpleButton("Clear", new ClickHandler() {
            public void onClick(ClickEvent event) {
                doClear();
            }
        });
        toolbar.add(clearButton);

        logEntries = new FlowPanel();
        logEntries.setStylePrimaryName("LogEntries");

        logEntriesScrollPanel = new ScrollPanel(logEntries);

        DOM.setStyleAttribute(logEntriesScrollPanel.getElement(), "border", "2px inset gray");

        LayoutPanel main = new LayoutPanel();
        main.add(toolbar);
        main.setWidgetLeftRight(toolbar, 10, Style.Unit.PX, 10, Style.Unit.PX);
        main.setWidgetTopHeight(toolbar, 10, Style.Unit.PX, 25, Style.Unit.PX);

        main.add(logEntriesScrollPanel);
        main.setWidgetLeftRight(logEntriesScrollPanel, 10, Style.Unit.PX, 10, Style.Unit.PX);
        main.setWidgetTopBottom(logEntriesScrollPanel, 45, Style.Unit.PX, 10, Style.Unit.PX);
        
        initWidget(main);
        setStylePrimaryName("LogPane");
    }

    @Initializer
    public void init() {
        levelDropDown.setSelectedItem(logger.getLevel());
    }

    // The log is cleared for every core
    public void init(SolrCore solrCore) {
        doClear();
    }

    public boolean isActive() {
        return true;
    }

    public String getName() {
        return "Log";
    }

    public Widget getContent() {
        return this;
    }

    public boolean isEnabled() {
        return true;
    }

    public HandlerRegistration addEnableHandler(EnableHandler handler) {
        return addHandler(handler, EnableEvent.getType());
    }

    //============================================ Setter/Getter Methods ===============================================

    @ConfiguredProperty("core.logging.default.level")
    public void setLogLevelAsString(String level) {
        logger.setLevel(Logger.Level.valueOf(level.toUpperCase()));
    }


    //================================================ Helper Methods ==================================================

    Logger getLogger() {
        return logger;
    }

    private void log(final AbstractLogger.Level level, final String message, Throwable t) {
        MapModel model = new MapModel() {{
            set("color", resolveColor(level));
            set("level", level.name());
            set("message", message);
        }};
        String htmlText = logTemplate.render(model);
        HTML html = new HTML(htmlText, true);
        logEntries.add(html);
        if (logEntries.getWidgetCount() > MAX_ROLL_ENTRIES) {
            logEntries.remove(0);
        }
        logEntriesScrollPanel.scrollToBottom();
    }

    private void doClear() {
        logEntries.clear();
    }

    private String resolveColor(AbstractLogger.Level level) {
        switch (level) {
            case TRACE:
                return "green";
            case DEBUG:
                return "blue";
            case INFO:
                return "black";
            case WARN:
                return "purple";
            case ERROR:
                return "red";
        }
        return "black";
    }
}
