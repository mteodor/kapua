/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.quartz.driver.exception;

import org.quartz.Trigger;

import javax.validation.constraints.NotNull;

/**
 * Exception to {@code throw} when the {@link Trigger} will never fire as defined.
 *
 * @since 1.1.0
 */
public class TriggerNeverFiresException extends QuartzTriggerDriverException {

    private final Trigger trigger;

    /**
     * Constructor that keeps the information about the {@link Trigger} that'll never fire.
     *
     * @param trigger The {@link Trigger} that'll never fire.
     * @since 1.1.0
     */
    public TriggerNeverFiresException(@NotNull Trigger trigger) {
        super(QuartzTriggerDriverErrorCodes.TRIGGER_NEVER_FIRES, trigger);

        this.trigger = trigger;
    }

    /**
     * Gets the {@link Trigger} that'll never fire.
     *
     * @return The {@link Trigger} that'll never fire.
     */
    public Trigger getTrigger() {
        return trigger;
    }
}
