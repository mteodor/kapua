/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.KapuaPosition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.StringWriter;
import java.math.BigInteger;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import static org.eclipse.kapua.message.internal.KapuaMessageUtil.*;

public class KapuaMessageTest extends Assert {

    private static final String KAPUA_MESSAGE_XML_STR = "missing";

    private ZonedDateTime referenceDate = ZonedDateTime.of(2017, 1, 18, 13, 10, 46, 0, ZoneId.systemDefault());

    private Date capturedDate = Date.from(referenceDate.toInstant());

    private Date sentDate = Date.from(referenceDate.plusSeconds(10).toInstant());

    private Date receivedDate = Date.from(referenceDate.plusMinutes(1).toInstant());

    @Before
    public void before() throws Exception {
        XmlUtil.setContextProvider(new MessageJAXBContextProvider());
    }

    @Test
    public void kapuaMessage() throws Exception {
        KapuaChannel kapuaChannel = new KapuaChannelImpl();
        KapuaPayload kapuaMetrics = new KapuaPayloadImpl();

        populateChannel(kapuaChannel);
        populatePayload(kapuaMetrics);
        KapuaMessage kapuaMessage = new KapuaMessageImpl(kapuaChannel, kapuaMetrics);
        populateKapuaMessage(kapuaMessage, referenceDate);

        assertEquals(UUID.fromString("11111111-2222-3333-4444-555555555555"), kapuaMessage.getId());
        assertEquals(new KapuaEid(BigInteger.ONE), kapuaMessage.getScopeId());
        assertEquals(new KapuaEid(BigInteger.ONE), kapuaMessage.getDeviceId());
        assertEquals(receivedDate, kapuaMessage.getReceivedOn());
        assertEquals(sentDate, kapuaMessage.getSentOn());
        assertEquals(capturedDate, kapuaMessage.getCapturedOn());
        KapuaPosition position = new KapuaPositionImpl();
        populatePosition(position, referenceDate);
        assertEquals(position.toString(), kapuaMessage.getPosition().toString());
        assertEquals(kapuaChannel, kapuaMessage.getChannel());
        assertEquals(kapuaMetrics, kapuaMessage.getPayload());
    }

    @Test
    public void setMessageChannelAndPayload() throws Exception {
        KapuaChannel kapuaChannel = new KapuaChannelImpl();
        KapuaPayload kapuaPayload = new KapuaPayloadImpl();

        populateChannel(kapuaChannel);
        populatePayload(kapuaPayload);
        KapuaMessage kapuaMessage = new KapuaMessageImpl();
        kapuaMessage.setChannel(kapuaChannel);
        kapuaMessage.setPayload(kapuaPayload);

        assertEquals(kapuaChannel, kapuaMessage.getChannel());
        assertEquals(kapuaPayload, kapuaMessage.getPayload());
    }

    @Test
    public void messageEquals() throws Exception {
        KapuaMessage kapuaMessageFirst = new KapuaMessageImpl();
        populateKapuaMessage(kapuaMessageFirst, referenceDate);
        KapuaMessage kapuaMessageSecond = new KapuaMessageImpl();
        populateKapuaMessage(kapuaMessageSecond, referenceDate);

        assertEquals(0, ((KapuaMessageImpl) kapuaMessageFirst).
                compareTo((KapuaMessageImpl) kapuaMessageSecond));
    }

    @Test
    @Ignore("KapuaMessage marshaling not working")
    public void marshallMessage() throws Exception {
        KapuaChannel kapuaChannel = new KapuaChannelImpl();
        KapuaPayload kapuaMetrics = new KapuaPayloadImpl();

        populateChannel(kapuaChannel);
        populatePayload(kapuaMetrics);
        KapuaMessage kapuaMessage = new KapuaMessageImpl(kapuaChannel, kapuaMetrics);
        populateKapuaMessage(kapuaMessage, referenceDate);

        StringWriter strWriter = new StringWriter();
        XmlUtil.marshal(kapuaMessage, strWriter);
        assertEquals(KAPUA_MESSAGE_XML_STR, strWriter.toString());
    }

}
