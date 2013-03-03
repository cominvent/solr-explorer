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

package org.apache.solr.explorer.client.plugin.listview.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.model.configuration.SolrCoreConfiguration;
import org.apache.solr.explorer.client.core.model.result.Hit;
import org.apache.solr.explorer.client.core.model.result.SearchResult;
import org.apache.solr.explorer.client.plugin.listview.model.config.ListViewConfig;
import org.apache.solr.explorer.client.plugin.listview.model.config.Thumbnail;
import org.gwtoolbox.commons.util.client.template.MapModel;
import org.gwtoolbox.commons.util.client.template.Template;
import org.gwtoolbox.widget.client.button.SimpleLinkButton;
import org.gwtoolbox.widget.client.support.setter.ValueSetter;

import java.util.List;

import static org.gwtoolbox.widget.client.panel.LayoutUtils.addGap;

/**
 * @author Uri Boness
 */
public class HitBox extends Composite {

    private int shownTabIndex;

    public HitBox(SearchResult result, final Hit hit, SolrCore solrCore, List<HitBoxTab> tabs, List<HitBoxLink> links) {

        SolrCoreConfiguration configuration = solrCore.getConfiguration();
        ListViewConfig renderingConfig = configuration.getConfig(ListViewConfig.class);

        VerticalPanel content = new VerticalPanel();

        String titleField = renderingConfig.getTitleFieldName();
        String title = (String) hit.get(titleField);
        if (title == null) {
            title = renderingConfig.getDefaultTitle();
        }
        Label titleLabel = new HTML(title);
        titleLabel.setStyleName("HitTitle");
        content.add(titleLabel);

        String summaryField = renderingConfig.getSummaryFieldName();
        String summary = (String) hit.get(summaryField);
        if (summary == null) {
            summary = renderingConfig.getDefaultSummary();
        }
        FlowPanel summaryPane = new FlowPanel();
        Thumbnail thumbnail = renderingConfig.getThumbnail();
        if (thumbnail != null) {
            Template urlTemplate = thumbnail.getUrlTemplate();
            final String thumbnailUrl = urlTemplate.render(new MapModel(hit));
            final Image image = new Image();
            image.setVisible(false);
            image.setWidth(thumbnail.getWidth());
            image.setHeight(thumbnail.getHeight());
            image.setStylePrimaryName("HitThumbnail");
            image.addLoadHandler(new LoadHandler() {
                public void onLoad(LoadEvent event) {
                    image.setVisible(true);
                }
            });
            image.addErrorHandler(new ErrorHandler() {
                public void onError(ErrorEvent event) {
                    image.removeFromParent();
                }
            });

            summaryPane.add(image);

            // the url of the image needs to be set in a delay so that IE will pick up the load events!!! 
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    image.setUrl(thumbnailUrl);
                }
            });
            
        }
        CollapsableLabel descriptionLabel = new CollapsableLabel(summary, true, renderingConfig.getSummaryLength());
        descriptionLabel.setStyleName("HitDescription");
        summaryPane.add(descriptionLabel);
        content.add(summaryPane);

        String urlField = renderingConfig.getUrlFieldName();
        String url = (String) hit.get(urlField);
        if (url == null || url.length() == 0) {
            url = "<no URL>";
        }
        final String finalUrl = URL.encode(url);
        final String finalTitle = title;
        Label urlLabel = new Label(url);
        urlLabel.setStyleName("HitUrl");
        urlLabel.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                GWT.log("final url: " + finalUrl, null);
                if (finalUrl.startsWith("http://") || finalUrl.startsWith("https://")) {
                    Window.open(finalUrl, finalTitle, null);
                } else {
                    Window.open("http://" + finalUrl, finalTitle, null);
                }
            }
        });

        HorizontalPanel toolbar = new HorizontalPanel();
        toolbar.setStyleName("Toolbar");
        toolbar.add(urlLabel);
        content.add(toolbar);

        addGap(toolbar, "2px");
        toolbar.add(new Label("-"));
        addGap(toolbar, "2px");
        toolbar.add(new Label("-"));
        addGap(toolbar, "2px");

        final DeckPanel tabContentHolder = new DeckPanel();
//        tabContentHolder.setVisible(false);
        tabContentHolder.setAnimationEnabled(true);

        final SimplePanel emptyPanel = new SimplePanel();
        tabContentHolder.add(emptyPanel);
        tabContentHolder.showWidget(0);

        for (HitBoxTab tab : tabs) {

            if (!tab.isActive() || !tab.isEnabled(result, hit)) {
                continue;
            }

            final Widget tabContent = tab.createWidget(result, hit);
            tabContentHolder.add(tabContent);
            final int index = tabContentHolder.getWidgetIndex(tabContent);

            final SimpleLinkButton link = new SimpleLinkButton(tab.getName());
            link.setStyleName("ToolbarLink");
            link.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    if (shownTabIndex == index) {
                        shownTabIndex = 0;
                        tabContentHolder.showWidget(0);
                    } else {
                        tabContentHolder.showWidget(index);
                        shownTabIndex = index;
                    }
//                    if (button.isDown()) {
//                        tabContentHolder.setVisible(true);
//                        
//                    } else {
//                        tabContentHolder.setVisible(false);
//                    }
                }
            });

            addGap(toolbar, "5px");
            toolbar.add(link);
        }

        content.add(tabContentHolder);
//        tabContentHolder.setVisible(false);

        for (HitBoxLink link : links) {
            if (!link.isActive() || !link.isEnabled(hit)) {
                continue;
            }
            SimpleLinkButton button = new SimpleLinkButton(link.getName());
            button.setStyleName("ToolbarLink");
            button.addClickHandler(link.createHandler(hit));
            addGap(toolbar, "5px");
            toolbar.add(button);
        }

        initWidget(content);
        setStyleName("HitBox");
    }


    //================================================= Inner Classes ==================================================

    private class CollapsableLabel extends Composite {

        private String text;
        private int collapsedTextLength;
        private ValueSetter<String> textSetter;

        private SimpleLinkButton link;

        private boolean collapsed;

        private CollapsableLabel(final String text, final boolean html, int collapsedTextLength) {

            final Label label = html ? new HTML() : new Label();
            label.setStyleName("CollapseLabelText");
            textSetter = new ValueSetter<String>() {
                public void set(String text) {
                    if (html) {
                        ((HTML) label).setHTML(text);
                    } else {
                        label.setText(text);
                    }
                }
            };

            link = new SimpleLinkButton("", new ClickHandler() {
                public void onClick(ClickEvent event) {
                    if (collapsed) {
                        expand();
                    } else {
                        collapse();
                    }
                }
            });
            link.setStyleName("CollapseLabelLink");

            FlowPanel main = new FlowPanel();
            main.add(label);
            main.add(link);
            initWidget(main);
            setText(text, collapsedTextLength);            
        }

        public void setText(String text, int collapsedTextLength) {
            this.text = text;
            this.collapsedTextLength = collapsedTextLength;
            if (text.length() <= collapsedTextLength) {
                link.setVisible(false);
                textSetter.set(text);
            } else {
                collapse();
            }
        }

        public void collapse() {
            link.setText("[+]");
            link.setTitle("Show full text");
            String collapsedText = text.substring(0, collapsedTextLength);
            textSetter.set(collapsedText);
            this.collapsed = true;
        }

        public void expand() {
            link.setText("[-]");
            link.setTitle("Collapse");
            textSetter.set(text);
            this.collapsed = false;
        }
    }
}
