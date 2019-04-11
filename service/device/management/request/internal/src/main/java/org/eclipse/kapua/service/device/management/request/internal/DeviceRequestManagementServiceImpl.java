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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.request.internal;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.DeviceManagementDomains;
import org.eclipse.kapua.service.device.management.commons.AbstractDeviceManagementServiceImpl;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.management.request.DeviceRequestManagementService;
import org.eclipse.kapua.service.device.management.request.GenericRequestFactory;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestChannel;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestPayload;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;

import java.util.Date;

/**
 * {@link DeviceRequestManagementService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class DeviceRequestManagementServiceImpl extends AbstractDeviceManagementServiceImpl implements DeviceRequestManagementService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final GenericRequestFactory FACTORY = LOCATOR.getFactory(GenericRequestFactory.class);

    @Override
    public GenericResponseMessage exec(GenericRequestMessage requestInput, Long timeout) throws KapuaException {
        return exec(requestInput.getScopeId(), requestInput.getDeviceId(), requestInput, timeout);
    }

    @Override
    public GenericResponseMessage exec(KapuaId scopeId, KapuaId deviceId, GenericRequestMessage requestInput, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(requestInput, "requestInput");

        //
        // Check Access
        Actions action;
        switch (requestInput.getChannel().getMethod()) {
            case EXECUTE:
                action = Actions.execute;
                break;
            case READ:
            case OPTIONS:
                action = Actions.read;
                break;
            case CREATE:
            case WRITE:
                action = Actions.write;
                break;
            case DELETE:
                action = Actions.delete;
                break;
            default:
                throw new KapuaRuntimeException(KapuaErrorCodes.OPERATION_NOT_SUPPORTED);
        }
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, action, requestInput.getScopeId()));

        //
        // Prepare the request
        GenericRequestChannel genericRequestChannel = FACTORY.newRequestChannel();
        genericRequestChannel.setAppName(requestInput.getChannel().getAppName());
        genericRequestChannel.setVersion(requestInput.getChannel().getVersion());
        genericRequestChannel.setMethod(requestInput.getChannel().getMethod());
        genericRequestChannel.setResources(requestInput.getChannel().getResources());

        GenericRequestPayload genericRequestPayload = FACTORY.newRequestPayload();
        genericRequestPayload.setMetrics(requestInput.getPayload().getMetrics());
        genericRequestPayload.setBody(requestInput.getPayload().getBody());

        GenericRequestMessage genericRequestMessage = FACTORY.newRequestMessage();
        genericRequestMessage.setScopeId(requestInput.getScopeId());
        genericRequestMessage.setDeviceId(requestInput.getDeviceId());
        genericRequestMessage.setCapturedOn(new Date());
        genericRequestMessage.setChannel(genericRequestChannel);
        genericRequestMessage.setPayload(genericRequestPayload);
        genericRequestMessage.setPosition(requestInput.getPosition());

        //
        // Do exec
        DeviceCallExecutor<?, ?, ?, GenericResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(genericRequestMessage, timeout);
        GenericResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, genericRequestMessage, responseMessage);

        return responseMessage;
    }
}
