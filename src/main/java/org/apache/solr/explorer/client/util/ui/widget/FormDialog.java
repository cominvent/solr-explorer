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

package org.apache.solr.explorer.client.util.ui.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import org.apache.solr.explorer.client.core.image.SolrExplorerImages;
import org.apache.solr.explorer.client.util.ui.widget.editor.Editor;
import org.apache.solr.explorer.client.util.ui.widget.validation.ValidationResult;
import org.apache.solr.explorer.client.util.ui.widget.validation.Validator;
import static org.gwtoolbox.widget.client.panel.LayoutUtils.addGap;

import org.gwtoolbox.widget.client.button.SimpleButton;
import org.gwtoolbox.widget.client.panel.LayoutUtils;
import org.gwtoolbox.widget.client.popup.dialog.Dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Uri Boness
 */
public abstract class FormDialog extends Dialog {

    private final static int LABEL_COLUMN = 0;

    private final static int FIELD_COLUMN = 1;

    private final static int ERROR_IMAGE_COLUMN = 2;

    private FlexTable grid;

    private Map<String, FieldRow> fieldRowByKey;

    private List<FieldRow> rows;

    public FormDialog(String title) {
        this(title, false);
    }

    public FormDialog(String title, boolean autoHide) {
        this(title, autoHide, !autoHide);
    }

    public FormDialog(String title, boolean autoHide, boolean modal) {
        super(autoHide, modal);
        setCaption(title);
        setClosable(true);
        setAnimationEnabled(true);

        grid = new FlexTable();
        grid.setStyleName("form-table");
        fieldRowByKey = new HashMap<String, FieldRow>();
        rows = new ArrayList<FieldRow>();

        HorizontalPanel buttonsPanel = new HorizontalPanel();
        if (!autoHide) {
            buttonsPanel.add(new SimpleButton("Cancel", new ClickHandler() {
                public void onClick(ClickEvent event) {
                    doCancel();
                }
            }));
            addGap(buttonsPanel, "3px");
        }
        buttonsPanel.add(new SimpleButton("Save", new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (validate()) {
                    doSubmit();
                }
            }
        }));

        VerticalPanel main = LayoutUtils.vBuilder().addGap("5px").add(grid).addGap("8px").add(buttonsPanel).addGap("5px").getPanel();
        main.setCellHorizontalAlignment(buttonsPanel, VerticalPanel.ALIGN_CENTER);

        setWidget(main);
    }

    protected <T> void addFieldRow(String key, String label, Editor<T> editor, Validator<T>... validators) {
        int row = grid.getRowCount();
        grid.insertRow(grid.getRowCount());
        grid.setWidget(row, LABEL_COLUMN, new Label(label));
        grid.setWidget(row, FIELD_COLUMN, editor.getWidget());
        Image image = SolrExplorerImages.Instance.get().fieldError().createImage();
        image.setVisible(false);
        grid.setWidget(row, ERROR_IMAGE_COLUMN, image);

        grid.getCellFormatter().setStyleName(row, LABEL_COLUMN, "label-cell");
        grid.getCellFormatter().setStyleName(row, FIELD_COLUMN, "field-cell");
        grid.getCellFormatter().setStyleName(row, ERROR_IMAGE_COLUMN, "image-cell");

        FieldRow fieldRow = new FieldRow(key, editor, image, validators);
        rows.add(fieldRow);
        fieldRowByKey.put(key, fieldRow);
    }

    protected void addWidgetRow(String label, Widget widget) {
        int row = grid.getRowCount();
        grid.insertRow(grid.getRowCount());
        grid.setWidget(row, LABEL_COLUMN, new Label(label));
        grid.setWidget(row, FIELD_COLUMN, widget);

        grid.getCellFormatter().setStyleName(row, LABEL_COLUMN, "label-cell");
        grid.getCellFormatter().setStyleName(row, FIELD_COLUMN, "field-cell");
    }

    protected boolean validate() {
        boolean valid = true;
        for (FieldRow row : rows) {
            if (shouldValidate(row.getKey())) {
                valid = row.validate() && valid;
            }
        }
        return valid;
    }

    protected void doCancel() {
        hide();
    }

    @Override
    public void show() {
        super.show();
        for (FieldRow row : rows) {
            Widget widget = row.getEditor().getWidget();
            if (widget instanceof Focusable) {
                ((Focusable)widget).setFocus(true);
                break;
            }
        }
    }

    protected abstract void doSubmit();

    protected boolean shouldValidate(String key) {
        return true;
    }

    protected Object getValue(String key) {
        return fieldRowByKey.get(key).getEditor().getValue();
    }

    protected String getStringValue(String key) {
        return (String) getValue(key);
    }

    protected Integer getIntValue(String key) {
        return (Integer) getValue(key);
    }

    protected long getLongValue(String key) {
        return (Long) getValue(key);
    }

    protected boolean getBooleanValue(String key) {
        return (Boolean) getValue(key);
    }

    protected void setValue(String key, Object value) {
        fieldRowByKey.get(key).getEditor().setValue(value);
    }

    @Override
    protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
            if (validate()) {
                doSubmit();
            }
            event.cancel();
        }
    }


    //============================================== Inner Classes =====================================================

    protected class FieldRow<T> {

        private final String key;

        private final Editor<T> editor;

        private final Image errorImage;

        private final Validator<T>[] validators;

        public FieldRow(String key, Editor<T> editor, Image errorImage, Validator<T>... validators) {
            this.key = key;
            this.editor = editor;
            this.errorImage = errorImage;
            this.validators = validators;
        }

        public boolean validate() {
            ValidationResult result = doValidate(editor.getValue());
            if (result == null) {
                errorImage.setVisible(false);
                return true;
            }
            errorImage.setTitle(result.getErrorMessages()[0]);
            errorImage.setVisible(true);
            return false;
        }

        protected ValidationResult doValidate(Object value) {
            for (Validator validator : validators) {
                ValidationResult result = validator.validate(value);
                if (!result.isValid()) {
                    return result;
                }
            }
            return null;
        }

        public String getKey() {
            return key;
        }

        public Editor<T> getEditor() {
            return editor;
        }
    }
}
