/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.transport.mqtt.exception;

import org.eclipse.kapua.transport.message.mqtt.MqttTopic;
import org.eclipse.kapua.transport.mqtt.MqttClient;

/**
 * {@link Exception} to {@code throw} when the {@link MqttClient} cannot subscribe to the {@link org.eclipse.kapua.transport.message.mqtt.MqttTopic}
 *
 * @since 1.2.0
 */
public class MqttClientSubscribeException extends MqttClientException {

    /**
     * The {@link org.eclipse.kapua.transport.message.mqtt.MqttTopic} where the {@link MqttClient} tired to subscribe to.
     *
     * @since 1.2.0
     */
    final MqttTopic topic;

    /**
     * Constructor.
     *
     * @param cause    The root {@link Throwable} that caused the error.
     * @param clientId The clientId of the {@link org.eclipse.kapua.transport.mqtt.MqttClient} that produced this {@link MqttClientSubscribeException}.
     * @param topic    The {@link org.eclipse.kapua.transport.message.mqtt.MqttTopic} where the {@link MqttClient} tired to subscribe to.
     * @since 1.2.0
     */
    public MqttClientSubscribeException(Throwable cause, String clientId, MqttTopic topic) {
        super(MqttClientErrorCodes.SUBSCRIBE_ERROR, cause, clientId, topic.getTopic());

        this.topic = topic;
    }

    /**
     * Gets the {@link org.eclipse.kapua.transport.message.mqtt.MqttTopic} where the {@link MqttClient} tired to subscribe to.
     *
     * @return The {@link org.eclipse.kapua.transport.message.mqtt.MqttTopic} where the {@link MqttClient} tired to subscribe to.
     * @since 1.2.0
     */
    public MqttTopic getTopic() {
        return topic;
    }

}
