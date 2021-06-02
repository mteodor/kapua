/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.server;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.module.device.shared.model.management.inventory.GwtInventoryBundle;
import org.eclipse.kapua.app.console.module.device.shared.model.management.inventory.GwtInventoryDeploymentPackage;
import org.eclipse.kapua.app.console.module.device.shared.model.management.inventory.GwtInventoryItem;
import org.eclipse.kapua.app.console.module.device.shared.model.management.inventory.GwtInventorySystemPackage;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceInventoryManagementService;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementService;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.inventory.DeviceInventoryBundle;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.inventory.DeviceInventoryBundles;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventory;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventoryItem;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.packages.DeviceInventoryPackage;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.packages.DeviceInventoryPackages;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.system.DeviceInventorySystemPackage;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.system.DeviceInventorySystemPackages;

import java.util.ArrayList;
import java.util.List;

/**
 * The server side implementation of the Device RPC service.
 */
public class GwtDeviceInventoryManagementServiceImpl extends KapuaRemoteServiceServlet implements GwtDeviceInventoryManagementService {

    private static final long serialVersionUID = -1391026997499175151L;

    private static final ConsoleSetting CONSOLE_SETTING = ConsoleSetting.getInstance();

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DeviceInventoryManagementService DEVICE_INVENTORY_MANAGEMENT_SERVICE = LOCATOR.getService(DeviceInventoryManagementService.class);
    private static final DeviceInventoryManagementFactory DEVICE_INVENTORY_MANAGEMENT_FACTORY = LOCATOR.getFactory(DeviceInventoryManagementFactory.class);

    @Override
    public ListLoadResult<GwtInventoryItem> findDeviceInventory(String scopeIdString, String deviceIdString)
            throws GwtKapuaException {
        try {
            KapuaId scopeId = KapuaEid.parseCompactId(scopeIdString);
            KapuaId id = KapuaEid.parseCompactId(deviceIdString);

            DeviceInventory deviceInventory = DEVICE_INVENTORY_MANAGEMENT_SERVICE.getInventory(scopeId, id, null);

            List<GwtInventoryItem> gwtInventoryItems = new ArrayList<GwtInventoryItem>();
            for (DeviceInventoryItem inventoryItem : deviceInventory.getInventoryItems()) {
                GwtInventoryItem gwtInventoryItem = new GwtInventoryItem();
                gwtInventoryItem.setName(inventoryItem.getName());
                gwtInventoryItem.setVersion(inventoryItem.getVersion());
                gwtInventoryItem.setType(inventoryItem.getItemType());

                gwtInventoryItems.add(gwtInventoryItem);
            }

            return new BaseListLoadResult<GwtInventoryItem>(gwtInventoryItems);
        } catch (Exception exception) {
            throw KapuaExceptionHandler.buildExceptionFromError(exception);
        }
    }

    @Override
    public ListLoadResult<GwtInventoryBundle> findDeviceBundles(String scopeIdString, String deviceIdString)
            throws GwtKapuaException {
        try {
            KapuaId scopeId = KapuaEid.parseCompactId(scopeIdString);
            KapuaId id = KapuaEid.parseCompactId(deviceIdString);

            DeviceInventoryBundles inventoryBundles = DEVICE_INVENTORY_MANAGEMENT_SERVICE.getBundles(scopeId, id, null);

            List<GwtInventoryBundle> gwtInventoryBundles = new ArrayList<GwtInventoryBundle>();
            for (DeviceInventoryBundle inventoryBundle : inventoryBundles.getInventoryBundles()) {
                GwtInventoryBundle gwtInventoryBundle = new GwtInventoryBundle();
                gwtInventoryBundle.setId(inventoryBundle.getId());
                gwtInventoryBundle.setName(inventoryBundle.getName());
                gwtInventoryBundle.setVersion(inventoryBundle.getVersion());
                gwtInventoryBundle.setStatus(inventoryBundle.getStatus());

                gwtInventoryBundles.add(gwtInventoryBundle);
            }

            return new BaseListLoadResult<GwtInventoryBundle>(gwtInventoryBundles);
        } catch (Exception exception) {
            throw KapuaExceptionHandler.buildExceptionFromError(exception);
        }
    }

    @Override
    public ListLoadResult<GwtInventorySystemPackage> findDeviceSystemPackages(String scopeIdString, String deviceIdString)
            throws GwtKapuaException {
        try {
            KapuaId scopeId = KapuaEid.parseCompactId(scopeIdString);
            KapuaId id = KapuaEid.parseCompactId(deviceIdString);

            DeviceInventorySystemPackages systemPackages = DEVICE_INVENTORY_MANAGEMENT_SERVICE.getSystemPackages(scopeId, id, null);

            List<GwtInventorySystemPackage> gwtInventorySystemPackages = new ArrayList<GwtInventorySystemPackage>();
            for (DeviceInventorySystemPackage inventorySystemPackage : systemPackages.getSystemPackages()) {
                GwtInventorySystemPackage gwtInventorySystemPackage = new GwtInventorySystemPackage();

                gwtInventorySystemPackage.setName(inventorySystemPackage.getName());
                gwtInventorySystemPackage.setVersion(inventorySystemPackage.getVersion());
                gwtInventorySystemPackage.setType(inventorySystemPackage.getPackageType());

                gwtInventorySystemPackages.add(gwtInventorySystemPackage);
            }

            return new BaseListLoadResult<GwtInventorySystemPackage>(gwtInventorySystemPackages);
        } catch (Exception exception) {
            throw KapuaExceptionHandler.buildExceptionFromError(exception);
        }
    }

    @Override
    public List<GwtInventoryDeploymentPackage> findDeviceDeploymentPackages(String scopeShortId, String deviceShortId)
            throws GwtKapuaException {
        try {
            KapuaId scopeId = KapuaEid.parseCompactId(scopeShortId);
            KapuaId deviceId = KapuaEid.parseCompactId(deviceShortId);

            DeviceInventoryPackages deploymentPackages = DEVICE_INVENTORY_MANAGEMENT_SERVICE.getDeploymentPackages(scopeId, deviceId, null);

            List<GwtInventoryDeploymentPackage> gwtInventoryDeploymentPackages = new ArrayList<GwtInventoryDeploymentPackage>();
            for (DeviceInventoryPackage deploymentPackage : deploymentPackages.getPackages()) {

                GwtInventoryDeploymentPackage gwtInventoryDeploymentPackage = new GwtInventoryDeploymentPackage();
                gwtInventoryDeploymentPackage.setName(deploymentPackage.getName());
                gwtInventoryDeploymentPackage.setVersion(deploymentPackage.getVersion());

                List<DeviceInventoryBundle> packageBundles = deploymentPackage.getPackageBundles();

                List<GwtInventoryBundle> gwtPackageBundles = new ArrayList<GwtInventoryBundle>();
                for (DeviceInventoryBundle bundleInfo : packageBundles) {

                    GwtInventoryBundle inventoryBundle = new GwtInventoryBundle();
                    inventoryBundle.setId(bundleInfo.getId());
                    inventoryBundle.setName(bundleInfo.getName());
                    inventoryBundle.setVersion(bundleInfo.getVersion());
                    inventoryBundle.setStatus(bundleInfo.getStatus());

                    gwtPackageBundles.add(inventoryBundle);
                }
                gwtInventoryDeploymentPackage.setBundles(gwtPackageBundles);

                gwtInventoryDeploymentPackages.add(gwtInventoryDeploymentPackage);
            }

            return gwtInventoryDeploymentPackages;
        } catch (Exception exception) {
            throw KapuaExceptionHandler.buildExceptionFromError(exception);
        }
    }
}
