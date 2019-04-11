/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.jbatch;

import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link EntityManagerFactory} for the job-engine-jbatch module.
 *
 * @since 1.1.0
 */
public class JobEngineEntityManagerFactory extends AbstractEntityManagerFactory implements EntityManagerFactory {

    private static final String PERSISTENCE_UNIT_NAME = "kapua-job-engine";
    private static final String DATASOURCE_NAME = "kapua-dbpool";
    private static final Map<String, String> UNIQUE_CONTRAINTS = new HashMap<>();

    private static JobEngineEntityManagerFactory instance = new JobEngineEntityManagerFactory();

    /**
     * Constructs a new entity manager factory and configure it to use the job persistence unit.
     */
    private JobEngineEntityManagerFactory() {
        super(PERSISTENCE_UNIT_NAME,
                DATASOURCE_NAME,
                UNIQUE_CONTRAINTS);
    }

    /**
     * Return the {@link EntityManager} singleton instance
     *
     * @return
     */
    public static JobEngineEntityManagerFactory getInstance() {
        return instance;
    }
}
