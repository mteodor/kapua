/*******************************************************************************
 * Copyright (c) 2018, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.exception;

import org.eclipse.kapua.model.id.KapuaId;

public class JobMissingStepException extends JobEngineException {

    public JobMissingStepException(KapuaId scopeId, KapuaId jobId) {
        super(KapuaJobEngineErrorCodes.JOB_STEP_MISSING, scopeId, jobId);
    }
}
