/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.internal.device.lifecycle;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.message.KapuaMessageFactory;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaLifecycleMessageFactory;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedPayload;

/**
 * {@link KapuaMessageFactory} implementation.
 *
 * @since 1.1.0
 */
@KapuaProvider
public class KapuaLifecycleMessageFactoryImpl implements KapuaLifecycleMessageFactory {
    @Override
    public KapuaAppsMessage newKapuaAppsMessage() {
        return new KapuaAppsMessageImpl();
    }

    @Override
    public KapuaAppsChannel newKapuaAppsChannel() {
        return new KapuaAppsChannelImpl();
    }

    @Override
    public KapuaAppsPayload newKapuaAppsPayload() {
        return new KapuaAppsPayloadImpl();
    }

    @Override
    public KapuaBirthMessage newKapuaBirthMessage() {
        return new KapuaBirthMessageImpl();
    }

    @Override
    public KapuaBirthChannel newKapuaBirthChannel() {
        return new KapuaBirthChannelImpl();
    }

    @Override
    public KapuaBirthPayload newKapuaBirthPayload() {
        return new KapuaBirthPayloadImpl();
    }

    @Override
    public KapuaDisconnectMessage newKapuaDisconnectMessage() {
        return new KapuaDisconnectMessageImpl();
    }

    @Override
    public KapuaDisconnectChannel newKapuaDisconnectChannel() {
        return new KapuaDisconnectChannelImpl();
    }

    @Override
    public KapuaDisconnectPayload newKapuaDisconnectPayload() {
        return new KapuaDisconnectPayloadImpl();
    }

    @Override
    public KapuaMissingMessage newKapuaMissingMessage() {
        return new KapuaMissingMessageImpl();
    }

    @Override
    public KapuaMissingChannel newKapuaMissingChannel() {
        return new KapuaMissingChannelImpl();
    }

    @Override
    public KapuaMissingPayload newKapuaMissingPayload() {
        return new KapuaMissingPayloadImpl();
    }

    @Override
    public KapuaUnmatchedMessage newKapuaUnmatchedMessage() {
        return new KapuaUnmatchedMessageImpl();
    }

    @Override
    public KapuaUnmatchedChannel newKapuaUnmatchedChannel() {
        return new KapuaUnmatchedChannelImpl();
    }

    @Override
    public KapuaUnmatchedPayload newKapuaUnmatchedPayload() {
        return new KapuaUnmatchedPayloadImpl();
    }
}
