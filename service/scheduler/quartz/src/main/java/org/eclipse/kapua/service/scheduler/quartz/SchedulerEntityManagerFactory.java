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
package org.eclipse.kapua.service.scheduler.quartz;

import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * `kapua-scheduler-internal` {@link EntityManagerFactory}.
 *
 * @since 1.0.0
 */
public class SchedulerEntityManagerFactory extends AbstractEntityManagerFactory implements EntityManagerFactory {

    private static final String PERSISTENCE_UNIT_NAME = "kapua-scheduler";
    private static final String DATASOURCE_NAME = "kapua-dbpool";
    private static final Map<String, String> UNIQUE_CONTRAINTS = new HashMap<>();

    private static SchedulerEntityManagerFactory instance = new SchedulerEntityManagerFactory();

    /**
     * Constructs a new entity manager factory and configure it to use the scheduler persistence unit.
     *
     * @since 1.0.0
     */
    private SchedulerEntityManagerFactory() {
        super(PERSISTENCE_UNIT_NAME,
                DATASOURCE_NAME,
                UNIQUE_CONTRAINTS);
    }

    /**
     * Gets the {@link EntityManager} singleton instance
     *
     * @return The {@link EntityManager} singleton instance
     * @since 1.0.0
     */
    public static SchedulerEntityManagerFactory getInstance() {
        return instance;
    }
}
