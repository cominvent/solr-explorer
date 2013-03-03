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

package org.apache.solr.explorer.client.plugin.facet.ui;

import org.apache.solr.explorer.client.core.manager.solr.admin.Field;
import org.apache.solr.explorer.client.core.manager.solr.admin.SolrCore;
import org.apache.solr.explorer.client.core.ui.AbstractDrawerSource;
import org.apache.solr.explorer.client.plugin.facet.model.config.FacetsConfig;
import org.apache.solr.explorer.client.plugin.facet.ui.date.DateFacetPane;
import org.apache.solr.explorer.client.plugin.facet.ui.field.FieldFacetPane;
import org.apache.solr.explorer.client.plugin.facet.ui.query.QueryFacetPane;
import org.gwtoolbox.ioc.core.client.ComponentContainer;
import org.gwtoolbox.ioc.core.client.ComponentContainerAware;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.widget.client.panel.layout.drawer.Drawer;
import org.gwtoolbox.widget.client.panel.layout.drawer.DrawerLayout;

import java.util.*;

/**
 * @author Uri Boness
 */
@Component
public class FacetDrawerSource extends AbstractDrawerSource implements ComponentContainerAware {

    private ComponentContainer container;

    private boolean dateEnabled;
    private boolean fieldEnabled;
    private boolean queryEnabled;

    private Drawer dateFacetDrawer;
    private Drawer fieldFacetDrawer;
    private Drawer queryFacetDrawer;

    @Override
    public void init(SolrCore solrCore) {
        boolean active = solrCore.getConfiguration().isActive(FacetsConfig.class);
        setActive(active);
        if (!active) {
            return;
        }
        dateEnabled = resolveDateFacetEnabled(solrCore);
        fieldEnabled = true;
        queryEnabled = true;
    }

    public Collection<Drawer> getDrawers() {
        List<Drawer> drawers = new ArrayList<Drawer>();
        if (fieldEnabled) {
            drawers.add(getFieldFacetDrawer());
        }
        if (queryEnabled) {
            drawers.add(getQueryFacetDrawer());
        }
        if (dateEnabled) {
            drawers.add(getDateFacetDrawer());
        }
        return drawers;
    }

    public void setComponentContainer(ComponentContainer container) {
        this.container = container;
    }

    //================================================ Helper Methods ==================================================

    private boolean resolveDateFacetEnabled(SolrCore solrCore) {
        for (Field field : solrCore.getSchema().getFields()) {
            if (field.getType().isDateField()) {
                return true;
            }
        }
        return false;
    }

    private Drawer getDateFacetDrawer() {
        if (dateFacetDrawer == null) {
            DateFacetPane pane = (DateFacetPane) container.getComponent("dateFacetPane");
            dateFacetDrawer = new Drawer("dateFacets", "Date Facets", pane, DrawerLayout.Position.LEFT).setDecorated(true);
        }
        return dateFacetDrawer;
    }

    private Drawer getFieldFacetDrawer() {
        if (fieldFacetDrawer == null) {
            FieldFacetPane pane = (FieldFacetPane) container.getComponent("fieldFacetPane");
            fieldFacetDrawer = new Drawer("fieldFacets", "Field Facets", pane, DrawerLayout.Position.LEFT).setDecorated(true);
        }
        return fieldFacetDrawer;
    }

    private Drawer getQueryFacetDrawer() {
        if (queryFacetDrawer == null) {
            QueryFacetPane pane = (QueryFacetPane) container.getComponent("queryFacetPane");
            queryFacetDrawer = new Drawer("queryFacets", "Query Facets", pane, DrawerLayout.Position.LEFT).setDecorated(true);
        }
        return queryFacetDrawer;
    }
}
