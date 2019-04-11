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
package org.eclipse.kapua.job.engine.queue;

import org.eclipse.kapua.model.KapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link QueuedJobExecutionCreator} definition.
 *
 * @since 1.1.0
 */
@XmlRootElement(name = "queuedJobExecutionCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = QueuedJobExecutionXmlRegistry.class, factoryMethod = "newQueuedJobExecutionCreator")
public interface QueuedJobExecutionCreator extends KapuaUpdatableEntityCreator<QueuedJobExecution> {

    /**
     * Gets the {@link org.eclipse.kapua.service.job.Job} {@link KapuaId}.
     *
     * @return The {@link org.eclipse.kapua.service.job.Job} {@link KapuaId}.
     * @since 1.1.0
     */
    KapuaId getJobId();

    /**
     * Sets the {@link org.eclipse.kapua.service.job.Job} {@link KapuaId}.
     *
     * @param jobId The {@link org.eclipse.kapua.service.job.Job} {@link KapuaId}.
     * @since 1.1.0
     */
    void setJobId(KapuaId jobId);

    /**
     * Gets the {@link org.eclipse.kapua.service.job.execution.JobExecution} {@link KapuaId}.
     *
     * @return The {@link org.eclipse.kapua.service.job.execution.JobExecution} {@link KapuaId}.
     * @since 1.1.0
     */
    KapuaId getJobExecutionId();

    /**
     * Sets the {@link org.eclipse.kapua.service.job.execution.JobExecution} {@link KapuaId}.
     *
     * @param jobExecutionId The {@link org.eclipse.kapua.service.job.execution.JobExecution} {@link KapuaId}.
     * @since 1.1.0
     */
    void setJobExecutionId(KapuaId jobExecutionId);

    /**
     * Gets the {@link org.eclipse.kapua.service.job.execution.JobExecution} {@link KapuaId} that needs to complete.
     *
     * @return The {@link org.eclipse.kapua.service.job.execution.JobExecution} {@link KapuaId} that needs to complete.
     * @since 1.1.0
     */
    KapuaId getWaitForJobExecutionId();

    /**
     * Sets the {@link org.eclipse.kapua.service.job.execution.JobExecution} {@link KapuaId} that needs to complete.
     *
     * @param waitForJobExecutionId The {@link org.eclipse.kapua.service.job.execution.JobExecution} {@link KapuaId} that needs to complete.
     * @since 1.1.0
     */
    void setWaitForJobExecutionId(KapuaId waitForJobExecutionId);

    /**
     * Gets the {@link QueuedJobExecutionStatus}.
     *
     * @return The {@link QueuedJobExecutionStatus}.
     * @since 1.1.0
     */
    QueuedJobExecutionStatus getStatus();

    /**
     * Sets the {@link QueuedJobExecutionStatus}.
     *
     * @param status The {@link QueuedJobExecutionStatus}.
     * @since 1.1.0
     */
    void setStatus(QueuedJobExecutionStatus status);
}
