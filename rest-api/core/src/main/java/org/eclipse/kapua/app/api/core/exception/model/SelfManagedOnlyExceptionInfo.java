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
package org.eclipse.kapua.app.api.core.exception.model;

import org.eclipse.kapua.service.authorization.shiro.exception.SelfManagedOnlyException;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "selfManagedOnlyExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class SelfManagedOnlyExceptionInfo extends ExceptionInfo {

    protected SelfManagedOnlyExceptionInfo() {
        super();
    }

    public SelfManagedOnlyExceptionInfo(Response.Status httpStatus, SelfManagedOnlyException kapuaException) {
        super(httpStatus, kapuaException.getCode(), kapuaException);
    }

}
