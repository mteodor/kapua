/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.jbatch.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Authorization setting key
 */
public enum JobEngineSettingKeys implements SettingKey {
    JOB_ENGINE_KEY("jobEngine.key"),

    JOB_ENGINE_STOP_WAIT_CHECK("jobEngine.stop.wait.check"),

    JOB_ENGINE_STOP_WAIT_CHECK_TIME_MAX("jobEngine.stop.wait.check.time.max"),

    JOB_ENGINE_STOP_WAIT_CHECK_TIME_INTERVAL("jobEngine.stop.wait.check.time.interval"),

    JOB_ENGINE_QUEUE_CHECK_DELAY("jobEngine.queue.check.delay"),

    JOB_ENGINE_QUEUE_PROCESSING_RUN_DELAY("jobEngine.queue.processing.run.delay");


    private String key;

    private JobEngineSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
