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
 * KapuaIllegalAccessException is thrown when an access check does not pass.
 *
 * @since 1.0
 *
 */
public class KapuaIllegalAccessException extends KapuaException {

    private static final long serialVersionUID = 7415560563036738488L;

    /**
     * Constructor
     *
     * @param operationName
     */
    public KapuaIllegalAccessException(String operationName) {
        super(KapuaErrorCodes.ILLEGAL_ACCESS, operationName);
    }
}
