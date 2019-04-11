/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.domain;

import org.eclipse.kapua.model.KapuaEntityFactory;

/**
 * {@link DomainFactory} definition.
 *
 * @see org.eclipse.kapua.model.KapuaEntityFactory
 * @since 1.0.0
 */
public interface DomainFactory extends KapuaEntityFactory<Domain, DomainCreator, DomainQuery, DomainListResult> {

    /**
     * Instantiates a new {@link DomainCreator}
     *
     * @param name The name to set into the {@link Domain}.
     * @return The newly instantiated {@link Domain}.
     * @since 1.0.0
     */
    DomainCreator newCreator(String name);

}
