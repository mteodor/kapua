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
package org.eclipse.kapua.service.device.management.inventory.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.DeviceManagementDomains;
import org.eclipse.kapua.service.device.management.bundle.message.internal.InventoryRequestChannel;
import org.eclipse.kapua.service.device.management.bundle.message.internal.InventoryRequestMessage;
import org.eclipse.kapua.service.device.management.bundle.message.internal.InventoryRequestPayload;
import org.eclipse.kapua.service.device.management.bundle.message.internal.InventoryResponseMessage;
import org.eclipse.kapua.service.device.management.commons.AbstractDeviceManagementServiceImpl;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementService;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.inventory.DeviceInventoryBundles;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventory;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.packages.DeviceInventoryPackages;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.system.DeviceInventorySystemPackages;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;

import java.util.Date;

/**
 * {@link DeviceInventoryManagementService} implementation.
 *
 * @since 1.5.0
 */
@KapuaProvider
public class DeviceInventoryManagementServiceImpl extends AbstractDeviceManagementServiceImpl implements DeviceInventoryManagementService {

    private static final String SCOPE_ID = "scopeId";
    private static final String DEVICE_ID = "deviceId";

    @Override
    public DeviceInventory getInventory(KapuaId scopeId, KapuaId deviceId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        InventoryRequestChannel inventoryRequestChannel = new InventoryRequestChannel();
        inventoryRequestChannel.setAppName(DeviceInventoryAppProperties.APP_NAME);
        inventoryRequestChannel.setVersion(DeviceInventoryAppProperties.APP_VERSION);
        inventoryRequestChannel.setMethod(KapuaMethod.READ);
        inventoryRequestChannel.setResource("inventory");

        InventoryRequestPayload inventoryRequestPayload = new InventoryRequestPayload();

        InventoryRequestMessage inventoryRequestMessage = new InventoryRequestMessage();
        inventoryRequestMessage.setScopeId(scopeId);
        inventoryRequestMessage.setDeviceId(deviceId);
        inventoryRequestMessage.setCapturedOn(new Date());
        inventoryRequestMessage.setPayload(inventoryRequestPayload);
        inventoryRequestMessage.setChannel(inventoryRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, InventoryResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(inventoryRequestMessage, timeout);
        InventoryResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, inventoryRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getDeviceInventory());
    }

    @Override
    public DeviceInventoryBundles getBundles(KapuaId scopeId, KapuaId deviceId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        InventoryRequestChannel inventoryRequestChannel = new InventoryRequestChannel();
        inventoryRequestChannel.setAppName(DeviceInventoryAppProperties.APP_NAME);
        inventoryRequestChannel.setVersion(DeviceInventoryAppProperties.APP_VERSION);
        inventoryRequestChannel.setMethod(KapuaMethod.READ);
        inventoryRequestChannel.setResource("bundles");

        InventoryRequestPayload inventoryRequestPayload = new InventoryRequestPayload();

        InventoryRequestMessage inventoryRequestMessage = new InventoryRequestMessage();
        inventoryRequestMessage.setScopeId(scopeId);
        inventoryRequestMessage.setDeviceId(deviceId);
        inventoryRequestMessage.setCapturedOn(new Date());
        inventoryRequestMessage.setPayload(inventoryRequestPayload);
        inventoryRequestMessage.setChannel(inventoryRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, InventoryResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(inventoryRequestMessage, timeout);
        InventoryResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, inventoryRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getDeviceInventoryBundles());
    }

    @Override
    public DeviceInventorySystemPackages getSystemPackages(KapuaId scopeId, KapuaId deviceId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        InventoryRequestChannel inventoryRequestChannel = new InventoryRequestChannel();
        inventoryRequestChannel.setAppName(DeviceInventoryAppProperties.APP_NAME);
        inventoryRequestChannel.setVersion(DeviceInventoryAppProperties.APP_VERSION);
        inventoryRequestChannel.setMethod(KapuaMethod.READ);
        inventoryRequestChannel.setResource("systemPackages");

        InventoryRequestPayload inventoryRequestPayload = new InventoryRequestPayload();

        InventoryRequestMessage inventoryRequestMessage = new InventoryRequestMessage();
        inventoryRequestMessage.setScopeId(scopeId);
        inventoryRequestMessage.setDeviceId(deviceId);
        inventoryRequestMessage.setCapturedOn(new Date());
        inventoryRequestMessage.setPayload(inventoryRequestPayload);
        inventoryRequestMessage.setChannel(inventoryRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, InventoryResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(inventoryRequestMessage, timeout);
        InventoryResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, inventoryRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getDeviceInventorySystemPackages());
    }

    @Override
    public DeviceInventoryPackages getDeploymentPackages(KapuaId scopeId, KapuaId deviceId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        InventoryRequestChannel inventoryRequestChannel = new InventoryRequestChannel();
        inventoryRequestChannel.setAppName(DeviceInventoryAppProperties.APP_NAME);
        inventoryRequestChannel.setVersion(DeviceInventoryAppProperties.APP_VERSION);
        inventoryRequestChannel.setMethod(KapuaMethod.READ);
        inventoryRequestChannel.setResource("deploymentPackages");

        InventoryRequestPayload inventoryRequestPayload = new InventoryRequestPayload();

        InventoryRequestMessage inventoryRequestMessage = new InventoryRequestMessage();
        inventoryRequestMessage.setScopeId(scopeId);
        inventoryRequestMessage.setDeviceId(deviceId);
        inventoryRequestMessage.setCapturedOn(new Date());
        inventoryRequestMessage.setPayload(inventoryRequestPayload);
        inventoryRequestMessage.setChannel(inventoryRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, InventoryResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(inventoryRequestMessage, timeout);
        InventoryResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, inventoryRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getDeviceInventoryPackages());
    }
}
