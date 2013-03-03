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

package org.apache.solr.explorer.client.plugin.facet.ui.date;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.apache.solr.explorer.client.util.ui.widget.editor.Editor;
import org.apache.solr.explorer.client.plugin.facet.model.context.DateGap;
import org.gwtoolbox.widget.client.form.editor.Option;
import org.gwtoolbox.widget.client.list.DropDownBox;

import java.util.ArrayList;
import java.util.List;

import static org.gwtoolbox.widget.client.panel.LayoutUtils.addGap;

/**
 * @author Uri Boness
 */
public class DateGapEditor extends Composite implements Editor<DateGap> {

    private TextBox numberBox;
    private DropDownBox<DateGap.Unit> unitBox;

    public DateGapEditor() {
        this(null);
    }

    public DateGapEditor(DateGap gap) {
        numberBox = new TextBox();
        numberBox.setWidth("20px");
        unitBox = new DropDownBox<DateGap.Unit>(getAllUnitOptions());

        HorizontalPanel main = new HorizontalPanel();
        main.add(numberBox);
        addGap(main, "3px");
        main.add(unitBox);
        initWidget(main);

        if (gap != null) {
            setValue(gap);
        }
    }

    public DateGap getValue() {
        try {
            return new DateGap(Integer.valueOf(numberBox.getValue()), unitBox.getSelectedValue());
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public void setValue(DateGap dateGap) {
        if (dateGap == null) {
            numberBox.setValue("");
            unitBox.setSelectedItem(DateGap.Unit.HOURS);
        } else {
            numberBox.setValue(String.valueOf(dateGap.getCount()));
            unitBox.setSelectedItem(dateGap.getUnit());
        }
    }

    public Widget getWidget() {
        return this;
    }

    public void setEnabled(boolean enabled) {
        numberBox.setEnabled(enabled);
        unitBox.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return numberBox.isEnabled();
    }


    //================================================ Helper Methods ==================================================

    private static List<Option<DateGap.Unit>> getAllUnitOptions() {
        List<Option<DateGap.Unit>> options = new ArrayList<Option<DateGap.Unit>>();
        for (DateGap.Unit unit : DateGap.Unit.values()) {
            options.add(new Option<DateGap.Unit>(unit.name(), unit));
        }
        return options;
    }

}
