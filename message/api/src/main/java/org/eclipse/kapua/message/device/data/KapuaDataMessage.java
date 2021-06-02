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
package org.eclipse.kapua.message.device.data;

import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.device.data.xml.DataMessageXmlRegistry;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link KapuaDataMessage} definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "dataMessage")
@XmlType(factoryClass = DataMessageXmlRegistry.class, factoryMethod = "newKapuaDataMessage")
public interface KapuaDataMessage extends KapuaMessage<KapuaDataChannel, KapuaDataPayload> {

}
