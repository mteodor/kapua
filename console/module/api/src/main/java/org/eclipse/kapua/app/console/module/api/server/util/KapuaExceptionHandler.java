/*******************************************************************************
 * Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.api.server.util;

import org.apache.shiro.authc.AuthenticationException;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaEntityUniquenessException;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaMaxNumberOfItemsReachedException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.KapuaUnauthenticatedException;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurationErrorCodes;
import org.eclipse.kapua.commons.configuration.KapuaConfigurationException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.service.authentication.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationException;
import org.eclipse.kapua.service.authorization.shiro.exception.SubjectUnauthorizedException;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementException;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class KapuaExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(KapuaExceptionHandler.class);

    private KapuaExceptionHandler() {
    }

    /**
     * Old way of handling {@link Throwable}s from {@link org.eclipse.kapua.service.KapuaService}s and othe components.
     * <p>
     * It was used to always {@code throw} a {@link GwtKapuaException} which worked but IDEs were not able to handle that,
     * which lead to pieces of code like the following:
     *
     * <pre>
     *             // Populate Session
     *             return establishSession();
     *         } catch (Throwable t) {
     *             internalLogout();
     *
     *             KapuaExceptionHandler.handle(t);
     *         }
     *         return null;
     *     }
     * </pre>
     *
     * @param throwable The {@link Throwable} to handle
     * @throws GwtKapuaException The {@link GwtKapuaException} that maps to the given {@link Throwable}
     * @since 1.0.0
     * @deprecated Since 1.5.0. See description. Please make use of {@link #buildExceptionFromError(Throwable)}
     */
    @Deprecated
    public static void handle(Throwable throwable) throws GwtKapuaException {
        throw buildExceptionFromError(throwable);
    }

    /**
     * Builds the {@link GwtKapuaException} that matches the given {@link Throwable}.
     * The returned {@link GwtKapuaException} <b>MUST</b> be {@code throw}n
     * <p>
     * It replaces {@link #handle(Throwable)} to improve usage when handling the {@link Throwable}s.
     * Now usage is like the following:
     *
     * <pre>
     *             // Populate Session
     *             return establishSession();
     *         } catch (Throwable t) {
     *             internalLogout();
     *
     *             throw KapuaExceptionHandler.buildExceptionFromError(t);
     *         }
     *     }
     * </pre>
     *
     * @param throwable The {@link Throwable} to build from.
     * @return The {@link GwtKapuaException} that suits the given {@link Throwable}
     * @since 1.5.0
     */
    public static GwtKapuaException buildExceptionFromError(Throwable throwable) {
        if (throwable instanceof KapuaUnauthenticatedException) {

            // sessions has expired
            return new GwtKapuaException(GwtKapuaErrorCode.UNAUTHENTICATED, throwable);
        } else if (throwable instanceof KapuaAuthenticationException) {

            KapuaAuthenticationException ke = (KapuaAuthenticationException) throwable;
            String cause = ke.getCode().name();

            // INVALID_USERNAME_PASSWORD

            if (cause.equals(KapuaAuthenticationErrorCodes.INVALID_LOGIN_CREDENTIALS.name())) {
                return new GwtKapuaException(GwtKapuaErrorCode.INVALID_USERNAME_PASSWORD, throwable);
            }
            if (cause.equals(KapuaAuthenticationErrorCodes.UNKNOWN_LOGIN_CREDENTIAL.name())) {
                return new GwtKapuaException(GwtKapuaErrorCode.INVALID_USERNAME_PASSWORD, throwable);
            }

            // MFA Case
            if (cause.equals(KapuaAuthenticationErrorCodes.REQUIRE_MFA_CREDENTIALS.name())) {
                return new GwtKapuaException(GwtKapuaErrorCode.REQUIRE_MFA_CODE, throwable);
            }

            // LOCKED_USER

            if (cause.equals(KapuaAuthenticationErrorCodes.LOCKED_LOGIN_CREDENTIAL.name())) {
                return new GwtKapuaException(GwtKapuaErrorCode.LOCKED_USER, throwable);
            }
            if (cause.equals(KapuaAuthenticationErrorCodes.DISABLED_LOGIN_CREDENTIAL.name())) {
                return new GwtKapuaException(GwtKapuaErrorCode.LOCKED_USER, throwable);
            }
            if (cause.equals(KapuaAuthenticationErrorCodes.EXPIRED_LOGIN_CREDENTIALS.name())) {
                return new GwtKapuaException(GwtKapuaErrorCode.LOCKED_USER, throwable);
            }

            // default
            return new GwtKapuaException(GwtKapuaErrorCode.INVALID_USERNAME_PASSWORD, throwable);
        } else if (throwable instanceof AuthenticationException) {
            return new GwtKapuaException(GwtKapuaErrorCode.UNAUTHENTICATED, throwable);
        } else if (throwable instanceof KapuaRuntimeException && ((KapuaRuntimeException) throwable).getCode().equals(KapuaErrorCodes.ENTITY_ALREADY_EXISTS) ||
                throwable.getCause() instanceof KapuaRuntimeException && ((KapuaRuntimeException) throwable.getCause()).getCode().equals(KapuaErrorCodes.ENTITY_ALREADY_EXISTS)) {
            logger.error("entity already exists", throwable);
            return new GwtKapuaException(GwtKapuaErrorCode.ENTITY_ALREADY_EXISTS, throwable, throwable.getLocalizedMessage());
        } else if (throwable instanceof KapuaException && ((KapuaException) throwable).getCode().equals(KapuaErrorCodes.INTERNAL_ERROR) && throwable.getCause() instanceof ClientException) {
            logger.error("internal service error", throwable);
            if (throwable.getCause().getCause() != null) {
                return new GwtKapuaException(GwtKapuaErrorCode.INTERNAL_ERROR, throwable, throwable.getCause().getCause().getLocalizedMessage());
            } else {
                return new GwtKapuaException(GwtKapuaErrorCode.INTERNAL_ERROR, throwable, throwable.getCause().getLocalizedMessage());
            }
        } else if (throwable instanceof KapuaException && ((KapuaException) throwable).getCode().equals(KapuaErrorCodes.INTERNAL_ERROR)) {
            logger.error("internal service error", throwable);
            return new GwtKapuaException(GwtKapuaErrorCode.INTERNAL_ERROR, throwable, throwable.getLocalizedMessage());
        } else if (throwable instanceof KapuaException &&
                (
                        ((KapuaException) throwable).getCode().name().equals("TRIGGER_INVALID_DATES") ||
                                ((KapuaException) throwable).getCode().name().equals("TRIGGER_INVALID_SCHEDULE")
                )
        ) {
            return new GwtKapuaException(GwtKapuaErrorCode.TRIGGER_NEVER_FIRE, throwable, throwable.getLocalizedMessage());
        } else if (throwable instanceof KapuaException && ((KapuaException) throwable).getCode().name().equals(KapuaErrorCodes.PARENT_LIMIT_EXCEEDED_IN_CONFIG.name())) {
            logger.warn("Child accounts limitation error", throwable);
            return new GwtKapuaException(GwtKapuaErrorCode.PARENT_LIMIT_EXCEEDED_IN_CONFIG, throwable, throwable.getLocalizedMessage());
        } else if (throwable instanceof KapuaException && ((KapuaException) throwable).getCode().name().equals(KapuaErrorCodes.ADMIN_ROLE_DELETED_ERROR.name())) {
            logger.warn("Admin role delete error.", throwable);
            return new GwtKapuaException(GwtKapuaErrorCode.ADMIN_ROLE_DELETED_ERROR, throwable, throwable.getLocalizedMessage());
        } else if (throwable instanceof KapuaException && ((KapuaException) throwable).getCode().name().equals(KapuaErrorCodes.SUBJECT_UNAUTHORIZED.name())) {
            logger.warn("User unauthorize", throwable);
            return new GwtKapuaException(GwtKapuaErrorCode.SUBJECT_UNAUTHORIZED, throwable, ((SubjectUnauthorizedException) throwable).getPermission().toString());
        } else if (throwable instanceof KapuaException && ((KapuaException) throwable).getCode().name().equals(KapuaErrorCodes.ENTITY_ALREADY_EXIST_IN_ANOTHER_ACCOUNT.name())) {
            logger.warn("Entity already exist in another account", throwable);
            return new GwtKapuaException(GwtKapuaErrorCode.ENTITY_ALREADY_EXIST_IN_ANOTHER_ACCOUNT, throwable, throwable.getLocalizedMessage());
        } else if (throwable instanceof KapuaException && ((KapuaException) throwable).getCode().name().equals(KapuaErrorCodes.EXTERNAL_ID_ALREADY_EXIST_IN_ANOTHER_ACCOUNT.name())) {
            logger.warn("External Id already exist in another account", throwable);
            return new GwtKapuaException(GwtKapuaErrorCode.EXTERNAL_ID_ALREADY_EXIST_IN_ANOTHER_ACCOUNT, throwable, throwable.getLocalizedMessage());
        } else if (throwable instanceof KapuaException && ((KapuaException) throwable).getCode().name().equals(KapuaErrorCodes.DUPLICATE_NAME.name())) {
            logger.warn("Entity already exist with the same name", throwable);
            return new GwtKapuaException(GwtKapuaErrorCode.DUPLICATE_NAME, throwable, throwable.getLocalizedMessage());
        } else if (throwable instanceof KapuaException && ((KapuaException) throwable).getCode().name().equals(KapuaErrorCodes.DUPLICATE_EXTERNAL_ID.name())) {
            logger.warn("Entity already exist with the same externalId", throwable);
            return new GwtKapuaException(GwtKapuaErrorCode.DUPLICATE_EXTERNAL_ID, throwable, throwable.getLocalizedMessage());
        } else if (throwable instanceof KapuaConfigurationException && ((KapuaConfigurationException) throwable).getCode().name().equals(KapuaConfigurationErrorCodes.SELF_LIMIT_EXCEEDED_IN_CONFIG.name())) {
            logger.warn("Parent account limitation error", throwable);
            return new GwtKapuaException(GwtKapuaErrorCode.SELF_LIMIT_EXCEEDED_IN_CONFIG, throwable, throwable.getLocalizedMessage());
        } else if (throwable instanceof KapuaException && ((KapuaException) throwable).getCode().name().equals(KapuaErrorCodes.DOWNLOAD_PACKAGE_EXCEPTION.name())) {
            logger.warn("Another resource is currently downloading", throwable);
            return new GwtKapuaException(GwtKapuaErrorCode.DOWNLOAD_PACKAGE_EXCEPTION, throwable, throwable.getLocalizedMessage());
        } else if (throwable instanceof KapuaEntityNotFoundException) {
            logger.warn("Entity not found", throwable);
            KapuaEntityNotFoundException kapuaEntityNotFoundException = (KapuaEntityNotFoundException) throwable;
            return new GwtKapuaException(GwtKapuaErrorCode.ENTITY_NOT_FOUND, throwable, kapuaEntityNotFoundException.getEntityType(), kapuaEntityNotFoundException.getEntityName());
        } else if (throwable instanceof KapuaEntityUniquenessException) {
            logger.warn("Entity uniqueness error", throwable);
            KapuaEntityUniquenessException kapuaEntityUniquenessException = (KapuaEntityUniquenessException) throwable;
            StringBuilder errorFieldsSb = new StringBuilder("(");
            for (Map.Entry<String, Object> entry : kapuaEntityUniquenessException.getUniquesFieldValues()) {
                errorFieldsSb.append(entry.getKey()).append(", ");
            }
            errorFieldsSb.delete(errorFieldsSb.length() - 2, errorFieldsSb.length()).append(") - (");
            for (Map.Entry<String, Object> entry : kapuaEntityUniquenessException.getUniquesFieldValues()) {
                errorFieldsSb.append(entry.getValue()).append(", ");
            }
            errorFieldsSb.delete(errorFieldsSb.length() - 2, errorFieldsSb.length()).append(")");

            return new GwtKapuaException(GwtKapuaErrorCode.ENTITY_UNIQUENESS, throwable, errorFieldsSb.toString());
        } else if (throwable instanceof KapuaIllegalArgumentException) {
            KapuaIllegalArgumentException kiae = (KapuaIllegalArgumentException) throwable;
            if (kiae.getArgumentName().equals("name") && kiae.getArgumentValue().equals(SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_USERNAME))) {
                return new GwtKapuaException(GwtKapuaErrorCode.OPERATION_NOT_ALLOWED_ON_ADMIN_USER, throwable);
            } else {
                return new GwtKapuaException(GwtKapuaErrorCode.ILLEGAL_ARGUMENT, throwable, ((KapuaIllegalArgumentException) throwable).getArgumentName(), ((KapuaIllegalArgumentException) throwable).getArgumentValue());
            }
        } else if (throwable instanceof KapuaException && ((KapuaException) throwable).getCode().name().equals(KapuaErrorCodes.BUNDLE_START_ERROR.name())) {
            logger.warn("Bundle could not be started", throwable);
            return new GwtKapuaException(GwtKapuaErrorCode.BUNDLE_START_ERROR, throwable, throwable.getLocalizedMessage());
        } else if (throwable instanceof KapuaException && ((KapuaException) throwable).getCode().name().equals(KapuaErrorCodes.BUNDLE_STOP_ERROR.name())) {
            logger.warn("Bundle could not be stoped", throwable);
            return new GwtKapuaException(GwtKapuaErrorCode.BUNDLE_STOP_ERROR, throwable, throwable.getLocalizedMessage());
        } else if (throwable instanceof KapuaException && ((KapuaException) throwable).getCode().equals(KapuaErrorCodes.PACKAGE_URI_SYNTAX_ERROR)) {
            return new GwtKapuaException(GwtKapuaErrorCode.PACKAGE_URI_SYNTAX_ERROR, throwable, throwable.getLocalizedMessage());
        } else if (throwable instanceof KapuaMaxNumberOfItemsReachedException) {
            return new GwtKapuaException(GwtKapuaErrorCode.MAX_NUMBER_OF_ITEMS_REACHED, throwable, ((KapuaMaxNumberOfItemsReachedException) throwable).getArgValue());
        } else if (throwable instanceof DeviceManagementException) {
            return new GwtKapuaException(GwtKapuaErrorCode.valueOf(((DeviceManagementException) throwable).getCode().name()), throwable, throwable.getLocalizedMessage());
        } else if (throwable instanceof KapuaException && ((KapuaException) throwable).getCode().name().equals(KapuaErrorCodes.PERMISSION_DELETE_NOT_ALLOWED.name())) {
            return new GwtKapuaException(GwtKapuaErrorCode.PERMISSION_DELETE_NOT_ALLOWED, throwable, throwable.getLocalizedMessage());
        } else {
            // all others => log and return internal error code
            logger.warn("RPC service non-application error", throwable);
            return GwtKapuaException.internalError(throwable, throwable.getLocalizedMessage());
        }
    }
}
