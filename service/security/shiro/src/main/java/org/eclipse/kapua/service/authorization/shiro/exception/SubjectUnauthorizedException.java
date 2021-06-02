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
package org.eclipse.kapua.service.authorization.shiro.exception;

import org.eclipse.kapua.service.authorization.permission.Permission;

/**
 * Authorization exception.
 *
 * @since 1.0
 */
public class SubjectUnauthorizedException extends KapuaAuthorizationException {

    private static final String KAPUA_ERROR_MESSAGES = "kapua-service-error-messages";

    private Permission permission;

    public SubjectUnauthorizedException(Permission permission) {
        super(KapuaAuthorizationErrorCodes.SUBJECT_UNAUTHORIZED, null, permission);

        setPermission(permission);
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
