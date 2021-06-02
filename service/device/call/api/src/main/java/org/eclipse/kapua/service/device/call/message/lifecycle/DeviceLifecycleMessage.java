/*******************************************************************************
 * Copyright (c) 2018, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.message.lifecycle;

import org.eclipse.kapua.service.device.call.message.DeviceMessage;

/**
 * {@link DeviceLifecycleMessage} definition.
 *
 * @param <C> The {@link DeviceLifecycleChannel} type.
 * @param <P> The {@link DeviceLifecyclePayload} type.
 * @since 1.0.0
 */
public interface DeviceLifecycleMessage<C extends DeviceLifecycleChannel, P extends DeviceLifecyclePayload> extends DeviceMessage<C, P> {

}
