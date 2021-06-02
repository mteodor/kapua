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
package org.eclipse.kapua.service.device.management.inventory.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.management.bundle.model.inventory.internal.DeviceInventoryImpl;
import org.eclipse.kapua.service.device.management.bundle.model.inventory.internal.DeviceInventoryItemImpl;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.internal.DeviceInventoryBundleImpl;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.internal.DeviceInventoryBundlesImpl;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.inventory.DeviceInventoryBundle;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.inventory.DeviceInventoryBundles;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventory;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventoryItem;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.packages.DeviceInventoryPackage;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.packages.DeviceInventoryPackages;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.system.DeviceInventorySystemPackage;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.system.DeviceInventorySystemPackages;
import org.eclipse.kapua.service.device.management.inventory.model.packages.internal.DeviceInventoryPackageImpl;
import org.eclipse.kapua.service.device.management.inventory.model.packages.internal.DeviceInventoryPackagesImpl;
import org.eclipse.kapua.service.device.management.inventory.model.system.internal.DeviceInventorySystemPackageImpl;
import org.eclipse.kapua.service.device.management.inventory.model.system.internal.DeviceInventorySystemPackagesImpl;

/**
 * {@link DeviceInventoryManagementFactory} implementation.
 *
 * @since 1.5.0
 */
@KapuaProvider
public class DeviceInventoryManagementFactoryImpl implements DeviceInventoryManagementFactory {

    @Override
    public DeviceInventory newDeviceInventory() {
        return new DeviceInventoryImpl();
    }

    @Override
    public DeviceInventoryItem newDeviceInventoryItem() {
        return new DeviceInventoryItemImpl();
    }

    @Override
    public DeviceInventoryBundle newDeviceInventoryBundle() {
        return new DeviceInventoryBundleImpl();
    }

    @Override
    public DeviceInventoryBundles newDeviceInventoryBundles() {
        return new DeviceInventoryBundlesImpl();
    }

    @Override
    public DeviceInventorySystemPackage newDeviceInventorySystemPackage() {
        return new DeviceInventorySystemPackageImpl();
    }

    @Override
    public DeviceInventorySystemPackages newDeviceInventorySystemPackages() {
        return new DeviceInventorySystemPackagesImpl();
    }

    @Override
    public DeviceInventoryPackage newDeviceInventoryPackage() {
        return new DeviceInventoryPackageImpl();
    }

    @Override
    public DeviceInventoryPackages newDeviceInventoryPackages() {
        return new DeviceInventoryPackagesImpl();
    }
}
