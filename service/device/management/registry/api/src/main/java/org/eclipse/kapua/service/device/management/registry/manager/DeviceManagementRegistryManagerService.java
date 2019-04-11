/*******************************************************************************
 * Copyright (c) 2018, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.registry.manager;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.message.notification.OperationStatus;
import org.eclipse.kapua.service.device.management.registry.manager.exception.ManagementOperationNotificationInvalidStatusException;
import org.eclipse.kapua.service.device.management.registry.manager.exception.ManagementOperationNotificationProcessingException;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationAttributes;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationProperty;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryService;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationAttributes;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public interface DeviceManagementRegistryManagerService extends KapuaService {

    Logger LOG = LoggerFactory.getLogger(DeviceManagementRegistryManagerService.class);

    KapuaLocator LOCATOR = KapuaLocator.getInstance();

    DeviceManagementOperationRegistryService DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE = LOCATOR.getService(DeviceManagementOperationRegistryService.class);
    DeviceManagementOperationFactory DEVICE_MANAGEMENT_OPERATION_FACTORY = LOCATOR.getFactory(DeviceManagementOperationFactory.class);

    ManagementOperationNotificationService MANAGEMENT_OPERATION_NOTIFICATION_REGISTRY_SERVICE = LOCATOR.getService(ManagementOperationNotificationService.class);
    ManagementOperationNotificationFactory MANAGEMENT_OPERATION_NOTIFICATION_FACTORY = LOCATOR.getFactory(ManagementOperationNotificationFactory.class);


    default void processOperationNotification(KapuaId scopeId, KapuaId operationId, Date updateOn, String resource, OperationStatus status, Integer progress) throws ManagementOperationNotificationProcessingException {

        try {
            switch (status) {
                case RUNNING: {
                    processRunningNotification(scopeId, operationId, updateOn, resource, progress);
                }
                break;
                case FAILED: {
                    processFailedNotification(scopeId, operationId, updateOn, resource);
                }
                break;
                case COMPLETED: {
                    processCompletedNotification(scopeId, operationId, updateOn, resource);
                }
                break;
                default: {
                    throw new ManagementOperationNotificationInvalidStatusException(scopeId, operationId, status, updateOn, progress);
                }
            }
        } catch (KapuaException ke) {
            throw new ManagementOperationNotificationProcessingException(ke, scopeId, operationId, status, updateOn, progress);
        }
    }


    default void processRunningNotification(KapuaId scopeId, KapuaId operationId, Date updateOn, String resource, Integer progress) throws KapuaException {
        addManagementNotification(scopeId, operationId, updateOn, OperationStatus.RUNNING, resource, progress);
    }

    default void processFailedNotification(KapuaId scopeId, KapuaId operationId, Date updateOn, String resource) throws KapuaException {
        closeDeviceManagementOperation(scopeId, operationId, updateOn, OperationStatus.FAILED);
    }

    default void processCompletedNotification(KapuaId scopeId, KapuaId operationId, Date updateOn, String resource) throws KapuaException {

        DeviceManagementOperation deviceManagementOperation = getDeviceManagementOperation(scopeId, operationId);

        //
        // UGLY 'DEPLOY-V2'-related part
        //
        boolean isLastNotification = true;
        for (DeviceManagementOperationProperty ip : deviceManagementOperation.getInputProperties()) {
            if (ip.getName().equals("kapua.package.download.install")) {
                if (resource.equals("download")) {
                    isLastNotification = !Boolean.parseBoolean(ip.getPropertyValue());
                }
                break;
            }
        }

        if (isLastNotification) {
            closeDeviceManagementOperation(scopeId, operationId, updateOn, OperationStatus.COMPLETED);
        } else {
            addManagementNotification(scopeId, operationId, updateOn, OperationStatus.COMPLETED, resource, 100);
        }
    }

    default void addManagementNotification(KapuaId scopeId, KapuaId operationId, Date updateOn, OperationStatus operationStatus, String resource, Integer progress) throws KapuaException {
        DeviceManagementOperation deviceManagementOperation = getDeviceManagementOperation(scopeId, operationId);

        ManagementOperationNotificationCreator managementOperationNotificationCreator = MANAGEMENT_OPERATION_NOTIFICATION_FACTORY.newCreator(scopeId);
        managementOperationNotificationCreator.setOperationId(deviceManagementOperation.getId());
        managementOperationNotificationCreator.setSentOn(updateOn);
        managementOperationNotificationCreator.setStatus(operationStatus);
        managementOperationNotificationCreator.setResource(resource);
        managementOperationNotificationCreator.setProgress(progress);

        MANAGEMENT_OPERATION_NOTIFICATION_REGISTRY_SERVICE.create(managementOperationNotificationCreator);
    }

    default void closeDeviceManagementOperation(KapuaId scopeId, KapuaId operationId, Date updateOn, OperationStatus finalStatus) throws KapuaException {

        DeviceManagementOperation deviceManagementOperation = null;

        short attempts = 0;
        short limit = 3;
        boolean failed = false;
        do {
            try {
                deviceManagementOperation = getDeviceManagementOperation(scopeId, operationId);
                deviceManagementOperation.setEndedOn(updateOn);
                deviceManagementOperation.setStatus(finalStatus);

                DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE.update(deviceManagementOperation);

                LOG.info("Update DeviceManagementOperation {} with status {}...  SUCCEEDED!", operationId, finalStatus);
                break;
            } catch (Exception e) {
                failed = true;
                attempts++;

                if (attempts >= limit) {
                    throw e;
                } else {
                    LOG.warn("Update DeviceManagementOperation {} with status {}...  FAILED! Retrying...", operationId, finalStatus);
                }
            }
        } while (failed);

        ManagementOperationNotificationQuery query = MANAGEMENT_OPERATION_NOTIFICATION_FACTORY.newQuery(scopeId);
        query.setPredicate(query.attributePredicate(ManagementOperationNotificationAttributes.OPERATION_ID, deviceManagementOperation.getId()));

        ManagementOperationNotificationListResult notifications = MANAGEMENT_OPERATION_NOTIFICATION_REGISTRY_SERVICE.query(query);

        for (ManagementOperationNotification mon : notifications.getItems()) {
            MANAGEMENT_OPERATION_NOTIFICATION_REGISTRY_SERVICE.delete(mon.getScopeId(), mon.getId());
        }
    }

    default DeviceManagementOperation getDeviceManagementOperation(KapuaId scopeId, KapuaId operationId) throws KapuaException {
        DeviceManagementOperationQuery query = DEVICE_MANAGEMENT_OPERATION_FACTORY.newQuery(scopeId);
        query.setPredicate(query.attributePredicate(DeviceManagementOperationAttributes.OPERATION_ID, operationId));

        DeviceManagementOperationListResult operations = DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE.query(query);
        DeviceManagementOperation deviceManagementOperation = operations.getFirstItem();

        if (deviceManagementOperation == null) {
            throw new KapuaEntityNotFoundException(DeviceManagementOperation.TYPE, operationId);
        }

        return deviceManagementOperation;
    }
}
