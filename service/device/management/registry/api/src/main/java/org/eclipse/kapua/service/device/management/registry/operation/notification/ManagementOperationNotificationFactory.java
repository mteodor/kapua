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
package org.eclipse.kapua.service.device.management.registry.operation.notification;

import org.eclipse.kapua.model.KapuaEntityFactory;

/**
 * {@link ManagementOperationNotificationFactory} definition.
 *
 * @see org.eclipse.kapua.model.KapuaEntityFactory
 * @since 1.0.0
 */
public interface ManagementOperationNotificationFactory extends KapuaEntityFactory<ManagementOperationNotification, ManagementOperationNotificationCreator, ManagementOperationNotificationQuery, ManagementOperationNotificationListResult> {

}