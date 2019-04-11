/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

public class GwtGroup extends GwtUpdatableEntityModel {

    private static final long serialVersionUID = 1L;

    public String getGroupName() {
        return get("groupName");
    }

    public void setGroupName(String name) {
        set("groupName", name);
        set("value", name);
    }

    public String getGroupDescription() {
        return get("description");
    }

    public String getUnescapedDescription() {
        return (String) getUnescaped("description");
    }

    public void setGroupDescription(String description) {
        set("description", description);
        set("value", description);
    }

    @Override
    public String toString() {
        return getGroupName();
    }
}
