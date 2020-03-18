/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura.exception;

import org.eclipse.kapua.KapuaException;

/**
 * {@link KuraDeviceCallException} base class.
 *
 * @since 1.0.0
 */
public class KuraDeviceCallException extends KapuaException {

    private static final long serialVersionUID = -6207605695086240243L;

    private static final String KAPUA_ERROR_MESSAGES = "device-call-service-error-messages";

    /**
     * Constructor.
     *
     * @param code The {@link KuraDeviceCallErrorCodes} associated with the {@link KuraDeviceCallException}
     */
    public KuraDeviceCallException(KuraDeviceCallErrorCodes code) {
        super(code);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link KuraDeviceCallErrorCodes} associated with the {@link KuraDeviceCallException}.
     * @param arguments The arguments associated with the {@link KuraDeviceCallException}.
     */
    public KuraDeviceCallException(KuraDeviceCallErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link KuraDeviceCallErrorCodes} associated with the {@link KuraDeviceCallException}.
     * @param cause     The original {@link Throwable}.
     * @param arguments The arguments associated with the {@link KuraDeviceCallException}.
     * @since 1.0.0
     */
    public KuraDeviceCallException(KuraDeviceCallErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
