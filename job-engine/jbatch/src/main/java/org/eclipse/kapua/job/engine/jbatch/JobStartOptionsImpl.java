/*******************************************************************************
 * Copyright (c) 2018, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.model.id.KapuaId;

import java.util.HashSet;
import java.util.Set;

/**
 * {@link JobStartOptions} implementation.
 *
 * @since 1.0.0
 */
public class JobStartOptionsImpl implements JobStartOptions {

    private Set<KapuaId> targetIdSublist;
    private Integer fromStepIndex;
    private boolean enqueue;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public JobStartOptionsImpl() {
    }

    /**
     * Clone constructor.
     *
     * @param jobStartOptions The {@link JobStartOptions} to clone.
     * @since 1.1.0
     */
    public JobStartOptionsImpl(JobStartOptions jobStartOptions) {
        this();

        setTargetIdSublist(jobStartOptions.getTargetIdSublist());
        setFromStepIndex(jobStartOptions.getFromStepIndex());
        setEnqueue(jobStartOptions.getEnqueue());
    }

    @Override
    public Set<KapuaId> getTargetIdSublist() {
        if (targetIdSublist == null) {
            targetIdSublist = new HashSet<>();
        }

        return targetIdSublist;
    }

    @Override
    public void setTargetIdSublist(Set<KapuaId> targetIdSublist) {
        this.targetIdSublist = targetIdSublist;
    }

    @Override
    public void addTargetIdToSublist(KapuaId targetId) {
        getTargetIdSublist().add(targetId);
    }

    @Override
    public void removeTargetIdToSublist(KapuaId targetId) {
        getTargetIdSublist().remove(targetId);
    }

    @Override
    public Integer getFromStepIndex() {
        return fromStepIndex;
    }

    @Override
    public void setFromStepIndex(Integer fromStepIndex) {
        this.fromStepIndex = fromStepIndex;
    }

    @Override
    public boolean getEnqueue() {
        return enqueue;
    }

    @Override
    public void setEnqueue(boolean enqueue) {
        this.enqueue = enqueue;
    }

    public static JobStartOptionsImpl parse(JobStartOptions jobStartOptions) {
        return jobStartOptions != null ? (jobStartOptions instanceof JobStartOptionsImpl ? (JobStartOptionsImpl) jobStartOptions : new JobStartOptionsImpl(jobStartOptions)) : null;
    }
}
