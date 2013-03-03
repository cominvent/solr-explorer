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

package org.apache.solr.explorer.client.core.ui.searchpane;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.apache.solr.explorer.client.core.image.SolrExplorerImages;
import org.gwtoolbox.widget.client.button.SimpleLinkButton;
import org.gwtoolbox.widget.client.panel.layout.tab.TabLayout;
import org.gwtoolbox.widget.client.panel.layout.tab.TabSpec;
import org.gwtoolbox.widget.client.popup.dialog.Dialog;

/**
 * @author Uri Boness
 */
public class AboutDialog extends Dialog {

    private TabLayout tabs;

    public AboutDialog() {
        super(false, true);
        setCaption("About");
        setStyleName("Dialog");
        addStyleName("AboutDialog");
        setWidthPx(500);
        setHeightPx(300);
        setGlassEnabled(true);

        VerticalPanel main = new VerticalPanel();
        main.setWidth("100%");
        main.setHeight("100%");
        add(main);

        tabs = new TabLayout();
        main.add(tabs);
        tabs.setHeight("300px");

        FlowPanel infoTab = new FlowPanel();
        infoTab.setStyleName("AboutTab");
        
        Label header = new Label("Explorer for Apache Solr");
        header.setStyleName("InfoTab-Header");
        infoTab.add(header);

        Label version = new Label("Version: 0.9");
        version.setStyleName("InfoTab-Version");
        infoTab.add(version);
        HTML infoText = new HTML("Explorer for Apache Solr is developed by SearchWorkings - A company striving to " +
                "make the best of open source search technologies accessible to everyone. Our focus mainly revolves around the " +
                "Apache Lucene project. We strongly believe that with the right tools around Lucene, open source search " +
                "can bring unpresidented value to search solutions of all kinds. For more information, please visit our" +
                "global community website at <a href='http://www.searchworkings.org' target='_blank'>http://www.searchworkings.org</a>");
        infoText.setStyleName("InfoTab-Text");
        infoTab.add(infoText);
        tabs.addTab(new TabSpec("info", "Info.", SolrExplorerImages.Instance.get().info().createImage(), infoTab, false));

        FlowPanel licenseTab = new FlowPanel();
        licenseTab.setStyleName("AboutTab");

        HTML licenseTextTop = new HTML("Copyright 2011 SearchWorking.org<p/>" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");<br/>" +
                "you may not use this file except in compliance with the License.<br/>" +
                "You may obtain a copy of the License at<p/>");
        licenseTextTop.setStyleName("LicenseTab-Text");
        licenseTab.add(licenseTextTop);

        SimpleLinkButton apacheLink = new SimpleLinkButton("http://www.apache.org/licenses/LICENSE-2.0", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Window.open("http://www.apache.org/licenses/LICENSE-2.0", "_blank", "");
            }
        });
        apacheLink.getElement().getStyle().setPaddingLeft(50, Style.Unit.PX);
        licenseTab.add(apacheLink);

        HTML licenseTextBottom = new HTML("<p/>Unless required by applicable law or agreed to in writing, software<br/>" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,<br/>" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.<br/>" +
                "See the License for the specific language governing permissions and<br/>" +
                "limitations under the License.");
        licenseTextBottom.setStyleName("LicenseTab-Text");
        licenseTab.add(licenseTextBottom);

        tabs.addTab(new TabSpec("license", "License", SolrExplorerImages.Instance.get().license().createImage(), licenseTab, false));

        tabs.setSelectedTab("info");
    }

    public String toString() {
        throw new RuntimeException("You forgot to define the toString for this class");
    }

    @Override
    public void show() {
        super.show();
        tabs.onResize();
    }

}
