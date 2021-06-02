/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaConfigurationExceptionTest extends Assert {

    @Test(expected = KapuaConfigurationException.class)
    public void testKapuaConfigurationExceptionWithIllegalArgument() throws KapuaConfigurationException {
        throw new KapuaConfigurationException(KapuaConfigurationErrorCodes.ILLEGAL_ARGUMENT);
    }

    @Test(expected = KapuaConfigurationException.class)
    public void testKapuaConfigurationExceptionWithIllegalArguments() throws KapuaConfigurationException {
        throw new KapuaConfigurationException(KapuaConfigurationErrorCodes.ILLEGAL_ARGUMENT, KapuaConfigurationErrorCodes.ATTRIBUTE_INVALID, KapuaConfigurationErrorCodes.INTERNAL_ERROR);
    }

    @Test(expected = KapuaConfigurationException.class)
    public void testKapuaConfigurationExceptionWithIllegalArgumentsAndThrowableCause() throws KapuaConfigurationException {
        throw new KapuaConfigurationException(KapuaConfigurationErrorCodes.ILLEGAL_ARGUMENT, new Throwable("Some message"), KapuaConfigurationErrorCodes.INTERNAL_ERROR);
    }

    @Test
    public void testInternalError() {
        KapuaConfigurationException.internalError("InternalError");
    }

    @Test
    public void testGetKapuaErrorMessagesBundle() {
        try {
            throw new KapuaConfigurationException(KapuaConfigurationErrorCodes.ILLEGAL_ARGUMENT, new Throwable("Some message"), KapuaConfigurationErrorCodes.INTERNAL_ERROR);
        } catch (KapuaConfigurationException e) {
            assertEquals("kapua-configuration-service-error-messages", e.getKapuaErrorMessagesBundle());
        }
    }
}
