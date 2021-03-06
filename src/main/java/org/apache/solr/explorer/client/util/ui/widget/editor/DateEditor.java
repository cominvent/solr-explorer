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

package org.apache.solr.explorer.client.util.ui.widget.editor;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DatePicker;
import org.apache.solr.explorer.client.core.image.SolrExplorerImages;
import org.gwtoolbox.widget.client.popup.Popup;

import java.util.Date;

/**
 * @author Uri Boness
 */
public class DateEditor extends Composite implements Editor<Date> {

    private TextBox textBox;
    private DatePicker datePicker;
    private Popup popup;
    private DateTimeFormat format;

    public DateEditor(String pattern) {
        this(pattern, null);
    }

    public DateEditor(String pattern, Date defaultValue) {
        this(DateTimeFormat.getFormat(pattern), defaultValue);
    }

    public DateEditor(DateTimeFormat format) {
        this(format, null);
    }

    public DateEditor(final DateTimeFormat format, Date defaultValue) {
        this.format = format;
        textBox = new TextBox();
        datePicker = new DatePicker();
        popup = new Popup(true);
        popup.setWidget(datePicker);
        datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            public void onValueChange(ValueChangeEvent<Date> event) {
                popup.hide();
                textBox.setValue(format.format(event.getValue()));
            }
        });
        final Image image = SolrExplorerImages.Instance.get().calendar().createImage();
        image.setSize("16px", "16px");
        image.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (textBox.isEnabled()) {
                    popup.setPopupPositionAndShow(new Popup.PositionCallback() {
                        public void setPosition(int offsetWidth, int offsetHeight) {
                            int x = image.getAbsoluteLeft();
                            int y = image.getAbsoluteTop() + image.getOffsetHeight();
                            popup.setPopupPosition(x, y);
                        }
                    });
                }
            }
        });

        HorizontalPanel main = new HorizontalPanel();
        main.add(textBox);
        main.add(image);
        main.setCellWidth(image, "20px");
        main.setCellHorizontalAlignment(image, HorizontalPanel.ALIGN_CENTER);
        main.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        initWidget(main);

        if (defaultValue != null) {
            setValue(defaultValue);
        }
    }

    public Date getValue() {
        return datePicker.getValue();
    }

    public void setValue(Date date) {
        datePicker.setValue(date);
    }

    public Widget getWidget() {
        return this;
    }

    public void setEnabled(boolean enabled) {
        textBox.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return textBox.isEnabled();
    }
}
