/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.account.client;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

import org.eclipse.kapua.app.console.module.account.client.messages.ConsoleAccountMessages;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccountQuery;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaDateField;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public class AccountFilterPanel extends EntityFilterPanel<GwtAccount> {

    private static final int WIDTH = 193;
    private static final int MAX_LEN = 255;
    private static final ConsoleAccountMessages MSGS = GWT.create(ConsoleAccountMessages.class);

    private EntityGrid<GwtAccount> entityGrid;
    private final GwtSession currentSession;

    private final KapuaTextField<String> accountNameField;
    private final KapuaTextField<String> accountOrgNameField;
    private final KapuaTextField<String> accountOrgEmailField;
    private final KapuaDateField expirationDateField;
    private final KapuaTextField<String> organizationContactNameField;
    private final KapuaTextField<String> organizationPhoneNumberField;
    private final KapuaTextField<String> organizationAddressLine1Field;
    private final KapuaTextField<String> organizationAddressLine2Field;
    private final KapuaTextField<String> organizationZipPostCodeField;
    private final KapuaTextField<String> organizationCityField;
    private final KapuaTextField<String> organizationStateProvinceCountryField;
    private final KapuaTextField<String> organizationCountryField;

    public AccountFilterPanel(AbstractEntityView<GwtAccount> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;

        setHeading(MSGS.filterHeader());

        VerticalPanel fieldsPanel = getFieldsPanel();

        Label accountNameLabel = new Label(MSGS.accountFilterNameLabel());
        accountNameLabel.setWidth(WIDTH);
        accountNameLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(accountNameLabel);

        accountNameField = new KapuaTextField<String>();
        accountNameField.setName("name");
        accountNameField.setWidth(WIDTH);
        accountNameField.setMaxLength(MAX_LEN);
        accountNameField.setStyleAttribute("margin-top", "0px");
        accountNameField.setStyleAttribute("margin-left", "5px");
        accountNameField.setStyleAttribute("margin-right", "5px");
        accountNameField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(accountNameField);

        Label accountOrgNameLabel = new Label(MSGS.accountFilterOrgNameLabel());
        accountOrgNameLabel.setWidth(WIDTH);
        accountOrgNameLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(accountOrgNameLabel);

        accountOrgNameField = new KapuaTextField<String>();
        accountOrgNameField.setName("orgName");
        accountOrgNameField.setWidth(WIDTH);
        accountOrgNameField.setMaxLength(MAX_LEN);
        accountOrgNameField.setStyleAttribute("margin-top", "0px");
        accountOrgNameField.setStyleAttribute("margin-left", "5px");
        accountOrgNameField.setStyleAttribute("margin-right", "5px");
        accountOrgNameField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(accountOrgNameField);

        Label accountOrgEmailLabel = new Label(MSGS.accountFilterOrgEmailLabel());
        accountOrgEmailLabel.setWidth(WIDTH);
        accountOrgEmailLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(accountOrgEmailLabel);

        accountOrgEmailField = new KapuaTextField<String>();
        accountOrgEmailField.setName("name");
        accountOrgEmailField.setWidth(WIDTH);
        accountOrgEmailField.setMaxLength(MAX_LEN);
        accountOrgEmailField.setStyleAttribute("margin-top", "0px");
        accountOrgEmailField.setStyleAttribute("margin-left", "5px");
        accountOrgEmailField.setStyleAttribute("margin-right", "5px");
        accountOrgEmailField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(accountOrgEmailField);

        Label expirationDateLabel = new Label(MSGS.accountFilterExpirationDateLabel());
        expirationDateLabel.setWidth(WIDTH);
        expirationDateLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(expirationDateLabel);

        expirationDateField = new KapuaDateField();
        expirationDateField.setName("expirationDate");
        expirationDateField.setWidth(WIDTH);
        expirationDateField.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        expirationDateField.setMaxLength(10);
        expirationDateField.setStyleAttribute("margin-top", "0px");
        expirationDateField.setStyleAttribute("margin-left", "5px");
        expirationDateField.setStyleAttribute("margin-right", "5px");
        expirationDateField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(expirationDateField);

        Label organizationContactNameLabel = new Label(MSGS.accountFilterOrgContactName());
        organizationContactNameLabel.setWidth(WIDTH);
        organizationContactNameLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(organizationContactNameLabel);

        organizationContactNameField = new KapuaTextField<String>();
        organizationContactNameField.setWidth(WIDTH);
        organizationContactNameField.setMaxLength(MAX_LEN);
        organizationContactNameField.setStyleAttribute("margin-top", "0px");
        organizationContactNameField.setStyleAttribute("margin-left", "5px");
        organizationContactNameField.setStyleAttribute("margin-right", "5px");
        organizationContactNameField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(organizationContactNameField);

        Label organizationPhoneNumberLabel = new Label(MSGS.accountFilterOrgPhoneNumber());
        organizationPhoneNumberLabel.setWidth(WIDTH);
        organizationPhoneNumberLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(organizationPhoneNumberLabel);

        organizationPhoneNumberField = new KapuaTextField<String>();
        organizationPhoneNumberField.setWidth(WIDTH);
        organizationPhoneNumberField.setMaxLength(MAX_LEN);
        organizationPhoneNumberField.setStyleAttribute("margin-top", "0px");
        organizationPhoneNumberField.setStyleAttribute("margin-left", "5px");
        organizationPhoneNumberField.setStyleAttribute("margin-right", "5px");
        organizationPhoneNumberField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(organizationPhoneNumberField);

        Label organizationAddresLine1Label = new Label(MSGS.accountFilterOrgAddress1());
        organizationAddresLine1Label.setWidth(WIDTH);
        organizationAddresLine1Label.setStyleAttribute("margin", "5px");
        fieldsPanel.add(organizationAddresLine1Label);

        organizationAddressLine1Field = new KapuaTextField<String>();
        organizationAddressLine1Field.setWidth(WIDTH);
        organizationAddressLine1Field.setMaxLength(MAX_LEN);
        organizationAddressLine1Field.setStyleAttribute("margin-top", "0px");
        organizationAddressLine1Field.setStyleAttribute("margin-left", "5px");
        organizationAddressLine1Field.setStyleAttribute("margin-right", "5px");
        organizationAddressLine1Field.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(organizationAddressLine1Field);

        Label organizationAddresLine2Label = new Label(MSGS.accountFilterOrgAddress2());
        organizationAddresLine2Label.setWidth(WIDTH);
        organizationAddresLine2Label.setStyleAttribute("margin", "5px");
        fieldsPanel.add(organizationAddresLine2Label);

        organizationAddressLine2Field = new KapuaTextField<String>();
        organizationAddressLine2Field.setWidth(WIDTH);
        organizationAddressLine2Field.setMaxLength(MAX_LEN);
        organizationAddressLine2Field.setStyleAttribute("margin-top", "0px");
        organizationAddressLine2Field.setStyleAttribute("margin-left", "5px");
        organizationAddressLine2Field.setStyleAttribute("margin-right", "5px");
        organizationAddressLine2Field.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(organizationAddressLine2Field);

        Label organizationZipPostCodeLabel = new Label(MSGS.accountFilterOrgZipPostCode());
        organizationZipPostCodeLabel.setWidth(WIDTH);
        organizationZipPostCodeLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(organizationZipPostCodeLabel);

        organizationZipPostCodeField = new KapuaTextField<String>();
        organizationZipPostCodeField.setWidth(WIDTH);
        organizationZipPostCodeField.setMaxLength(MAX_LEN);
        organizationZipPostCodeField.setStyleAttribute("margin-top", "0px");
        organizationZipPostCodeField.setStyleAttribute("margin-left", "5px");
        organizationZipPostCodeField.setStyleAttribute("margin-right", "5px");
        organizationZipPostCodeField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(organizationZipPostCodeField);

        Label organizationCityLabel = new Label(MSGS.accountFilterOrgCity());
        organizationCityLabel.setWidth(WIDTH);
        organizationCityLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(organizationCityLabel);

        organizationCityField = new KapuaTextField<String>();
        organizationCityField.setWidth(WIDTH);
        organizationCityField.setMaxLength(MAX_LEN);
        organizationCityField.setStyleAttribute("margin-top", "0px");
        organizationCityField.setStyleAttribute("margin-left", "5px");
        organizationCityField.setStyleAttribute("margin-right", "5px");
        organizationCityField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(organizationCityField);

        Label organizationStateProvinceLabel = new Label(MSGS.accountFilterOrgStateProvince());
        organizationStateProvinceLabel.setWidth(WIDTH);
        organizationStateProvinceLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(organizationStateProvinceLabel);

        organizationStateProvinceCountryField = new KapuaTextField<String>();
        organizationStateProvinceCountryField.setWidth(WIDTH);
        organizationStateProvinceCountryField.setMaxLength(MAX_LEN);
        organizationStateProvinceCountryField.setStyleAttribute("margin-top", "0px");
        organizationStateProvinceCountryField.setStyleAttribute("margin-left", "5px");
        organizationStateProvinceCountryField.setStyleAttribute("margin-right", "5px");
        organizationStateProvinceCountryField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(organizationStateProvinceCountryField);

        Label organizationSCountryLabel = new Label(MSGS.accountFilterOrgCountry());
        organizationSCountryLabel.setWidth(WIDTH);
        organizationSCountryLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(organizationSCountryLabel);

        organizationCountryField = new KapuaTextField<String>();
        organizationCountryField.setWidth(WIDTH);
        organizationCountryField.setMaxLength(MAX_LEN);
        organizationCountryField.setStyleAttribute("margin-top", "0px");
        organizationCountryField.setStyleAttribute("margin-left", "5px");
        organizationCountryField.setStyleAttribute("margin-right", "5px");
        organizationCountryField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(organizationCountryField);
    }

    @Override
    public void resetFields() {
        accountNameField.setValue(null);
        accountOrgEmailField.setValue(null);
        accountOrgNameField.setValue(null);
        expirationDateField.setValue(null);
        organizationContactNameField.setValue(null);
        organizationPhoneNumberField.setValue(null);
        organizationAddressLine1Field.setValue(null);
        organizationAddressLine2Field.setValue(null);
        organizationZipPostCodeField.setValue(null);
        organizationCityField.setValue(null);
        organizationStateProvinceCountryField.setValue(null);
        organizationCountryField.setValue(null);
        GwtAccountQuery query = new GwtAccountQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);
    }

    @Override
    public void doFilter() {
        GwtAccountQuery query = new GwtAccountQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        query.setName(accountNameField.getValue());
        query.setOrganizationName(accountOrgNameField.getValue());
        query.setOrganizationEmail(accountOrgEmailField.getValue());
        query.setExpirationDate(expirationDateField.getValue());
        query.setOrganizationContactName(organizationContactNameField.getValue());
        query.setOrganizationPhoneNumber(organizationPhoneNumberField.getValue());
        query.setOrganizationAddressLine1(organizationAddressLine1Field.getValue());
        query.setOrganizationAddressLine2(organizationAddressLine2Field.getValue());
        query.setOrganizationZipPostCode(organizationZipPostCodeField.getValue());
        query.setOrganizationCity(organizationCityField.getValue());
        query.setOrganizationStateProvinceCountry(organizationStateProvinceCountryField.getValue());
        query.setOrganizationCountry(organizationCountryField.getValue());
        entityGrid.refresh(query);
    }

}
