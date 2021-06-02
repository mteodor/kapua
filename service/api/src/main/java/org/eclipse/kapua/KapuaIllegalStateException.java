/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua;

/**
 * KapuaIllegalStateException is thrown when the the user session is inconsistent.
 *
 * @since 1.0
 *
 */
public class KapuaIllegalStateException extends KapuaRuntimeException {

    private static final long serialVersionUID = -912672615903975466L;

    /**
     * Constructor
     *
     * @param message
     */
    public KapuaIllegalStateException(String message) {
        super(KapuaErrorCodes.ILLEGAL_STATE, null, message);
    }

    /**
     * Constructor
     *
     * @param message
     * @param t
     */
    public KapuaIllegalStateException(String message, Throwable t) {
        super(KapuaErrorCodes.ILLEGAL_STATE, t, message);
    }

}
