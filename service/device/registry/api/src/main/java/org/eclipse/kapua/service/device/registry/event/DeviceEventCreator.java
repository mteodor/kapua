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
package org.eclipse.kapua.service.device.registry.event;

import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;

import java.util.Date;

/**
 * Device event creator service definition.
 *
 * @since 1.0
 */
public interface DeviceEventCreator extends KapuaEntityCreator<DeviceEvent> {

    /**
     * Get the device identifier
     *
     * @return
     */
    KapuaId getDeviceId();

    /**
     * Set the device identifier
     *
     * @param deviceId
     */
    void setDeviceId(KapuaId deviceId);

    /**
     * Get the sent on date
     *
     * @return
     */
    Date getSentOn();

    /**
     * Set the sent on date
     *
     * @param sentOn
     */
    void setSentOn(Date sentOn);

    /**
     * Get the received on date
     *
     * @return
     */
    Date getReceivedOn();

    /**
     * Set the received on date
     *
     * @param receivedOn
     */
    void setReceivedOn(Date receivedOn);

    /**
     * Get device position
     *
     * @return
     */
    KapuaPosition getPosition();

    /**
     * Set device position
     *
     * @param position
     */
    void setPosition(KapuaPosition position);

    /**
     * Get resource
     *
     * @return
     */
    String getResource();

    /**
     * Set resource
     *
     * @param resource
     */
    void setResource(String resource);

    /**
     * GHet action
     *
     * @return
     */
    KapuaMethod getAction();

    /**
     * Set action
     *
     * @param action
     */
    void setAction(KapuaMethod action);

    /**
     * Get response code
     *
     * @return
     */
    KapuaResponseCode getResponseCode();

    /**
     * Set response code
     *
     * @param responseCode
     */
    void setResponseCode(KapuaResponseCode responseCode);

    /**
     * Get event message
     *
     * @return
     */
    String getEventMessage();

    /**
     * Set event message
     *
     * @param eventMessage
     */
    void setEventMessage(String eventMessage);
}
