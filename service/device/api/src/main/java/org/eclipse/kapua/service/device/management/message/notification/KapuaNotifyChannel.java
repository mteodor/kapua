/*******************************************************************************
 * Copyright (c) 2011, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.message.notification;

import org.eclipse.kapua.service.device.management.message.KapuaAppChannel;

/**
 * Kapua notify message channel object definition.
 *
 * @since 1.0
 */
public interface KapuaNotifyChannel extends KapuaAppChannel {

    /**
     * Get the request resources
     *
     * @return
     * @since 1.2.0
     */
    String[] getResources();

    /**
     * Set the request resources
     *
     * @param resources
     * @since 1.2.0
     */
    void setResources(String[] resources);
}
