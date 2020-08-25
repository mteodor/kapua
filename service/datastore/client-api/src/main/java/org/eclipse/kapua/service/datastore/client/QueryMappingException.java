/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client;

/**
 * Query mapping exception.<br>
 * This exception is raised if some error occurred when mapping a query
 *
 * @since 1.0
 */
public class QueryMappingException extends ClientException {

    private static final long serialVersionUID = 5211237236391747299L;

    /**
     * Construct the exception with the provided message
     *
     * @param message
     */
    public QueryMappingException(String message) {
        super(ClientErrorCodes.QUERY_MAPPING_EXCEPTION, message);
    }

    /**
     * Construct the exception with the provided message and throwable
     *
     * @param message
     * @param t
     */
    public QueryMappingException(String message, Throwable t) {
        super(ClientErrorCodes.QUERY_MAPPING_EXCEPTION, t, message);
    }

}
