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
package org.eclipse.kapua.service.authentication;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Username and password {@link LoginCredentials} definition.
 *
 * @since 1.0
 */
@XmlRootElement(name = "usernamePasswordCredentials")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "username", "password", "authenticationCode", "trustKey" }, factoryClass = AuthenticationXmlRegistry.class, factoryMethod = "newUsernamePasswordCredentials")
public interface UsernamePasswordCredentials extends LoginCredentials {

    /**
     * return the username
     *
     * @return
     */
    String getUsername();

    /**
     * Set the username
     *
     * @param username
     */
    void setUsername(String username);

    /**
     * return the password
     *
     * @return
     */
    String getPassword();

    /**
     * Set the password
     *
     * @param password
     */
    void setPassword(String password);

    /**
     * return the authenticationCode
     *
     * @return
     */
    String getAuthenticationCode();

    /**
     * Set the authenticationCode
     *
     * @param authenticationCode
     */
    void setAuthenticationCode(String authenticationCode);

    /**
     * return the trustKey
     *
     * @return the trust key
     */
    String getTrustKey();

    /**
     * Set the trust key
     *
     * @param trustKey
     */
    void setTrustKey(String trustKey);
}
