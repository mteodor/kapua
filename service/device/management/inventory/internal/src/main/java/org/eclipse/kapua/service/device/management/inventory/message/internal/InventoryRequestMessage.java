/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.bundle.message.internal;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventory;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;

/**
 * {@link DeviceInventory} {@link KapuaRequestMessage} implementation.
 *
 * @since 1.5.0
 */
public class InventoryRequestMessage extends KapuaMessageImpl<InventoryRequestChannel, InventoryRequestPayload>
        implements KapuaRequestMessage<InventoryRequestChannel, InventoryRequestPayload> {

    private static final long serialVersionUID = 3593350285989405174L;

    @Override
    public Class<InventoryRequestMessage> getRequestClass() {
        return InventoryRequestMessage.class;
    }

    @Override
    public Class<InventoryResponseMessage> getResponseClass() {
        return InventoryResponseMessage.class;
    }

}
