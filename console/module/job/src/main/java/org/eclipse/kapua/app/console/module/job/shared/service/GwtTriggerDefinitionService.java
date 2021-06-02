/*******************************************************************************
 * Copyright (c) 2019, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.job.shared.service;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.GwtTriggerProperty;
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.definition.GwtTriggerDefinition;

@RemoteServiceRelativePath("triggerDefinition")
public interface GwtTriggerDefinitionService extends RemoteService {

    ListLoadResult<GwtTriggerDefinition> findAll()
            throws GwtKapuaException;

    GwtTriggerDefinition find(String triggerDefinitionId)
            throws GwtKapuaException;

    // Just to make Gwt serialize GwtTriggerProperty
    GwtTriggerProperty trickGwt();
}
