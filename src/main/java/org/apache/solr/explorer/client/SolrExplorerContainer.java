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

import org.gwtoolbox.ioc.core.client.ComponentContainer;
import org.gwtoolbox.ioc.core.client.annotation.*;

/**
 * @author Uri Boness
 */
@Logging(LoggingLevel.FINER)
@ComponentScan(packages = "org.apache.solr.explorer.client.*")
@ResourceBundle("config.properties")
@RootPanelBindingScan
public abstract class SolrExplorerContainer implements ComponentContainer {
}