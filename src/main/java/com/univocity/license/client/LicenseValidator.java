/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *
 */

package com.univocity.license.client;

import com.univocity.license.client.details.*;

import java.io.*;

/**
 * A validator for licenses of any given product whose information is provided by {@link ProductDetails}.
 *
 * Licenses can be individual to a computer and therefore stored locally in a operating-system dependent fashion
 * (for individual users), or grouped together in a single license file containing multiple license entries
 * (for multiple servers where individual license management would be painful).
 */
public interface LicenseValidator {

	/**
	 * Returns the path where the license for this particular product version is expected to be located.
	 *
	 * The license file is not mandatory and it will only be this file will only be used if a license can't be found on the
	 * operating-system store.
	 *
	 * Defaults to  {@code [user.home]/.[store name]/[product_variant_version]/license}. If the {@code user.home} can't
	 * be determined the relative path {@code .[store name]/[product_variant_version]/license} will be used.
	 *
	 * @return the path to the license file.
	 */
	String getLicenseFilePath();

	/**
	 * Defines the path where the license for this particular product version is expected to be located.
	 *
	 * The license file is not mandatory and it will only be this file will only be used if a license can't be found on the
	 * operating-system store.
	 *
	 * Defaults to  {@code [user.home]/.[store name]/[product_variant_version]/license}. If the {@code user.home} can't
	 * be determined the relative path {@code .[store name]/[product_variant_version]/license} will be used.
	 *
	 * Upon calling this method, the given path will be tested: the directory path will be created if it doesn't exist,
	 * and a dummy license file will be created if no license file exists. If a license file already exists at the given path,
	 * it must have read and write permissions. This method will return {@code false} if the given path causes any of the
	 * aforementioned validations to fail.
	 *
	 * @param licenseFilePath the path to the license file.
	 *
	 * @return a flag indicating whether the path can be successfully used as the license path.
	 */
	boolean setLicenseFilePath(String licenseFilePath);

	/**
	 * Validates the local license currently associated with the product. The license is expected
	 * to be stored locally in an operating-system dependent store. A file-based copy of the license will be used if the
	 * operating system store is not available. The path to this file is determined by the result of method
	 * {@link #getLicenseFilePath()} - it will only be used if a license can't be found on the operating-system store.
	 *
	 * <strong>NOTE:</strong> this will return immediately for a quick initial validation based on the license stored
	 * locally, but it will also synchronize the local license with the license server in a separate process.
	 * The remote validation result is cached locally and in the server for a few hours. Subsequent calls to this method
	 * will produce the previous validation result immediately.
	 *
	 * The remote synchronization and validation uses the servers provided by {@link StoreDetails#licenseServerDomains()})
	 * and is potentially slow. If any changes have been applied to the license (revoke, renewal, etc) the locally stored
	 * license will be updated accordingly, and if the online validation result is different from the initial offline
	 * validation, the {@link RemoteValidationAction} provided as a parameter to this method will be called. If both
	 * offline and online validation produce the same result the {@link RemoteValidationAction} won't be called.
	 *
	 * @param licenseValidationAction action to be performed once the remote license validation completed. A
	 *                                {@link LicenseValidationResult} will be sent to the caller via
	 *                                {@link RemoteValidationAction#licenseValidated(LicenseValidationResult)}.
	 *
	 * @return the result of the offline license validation operation, i.e. whether the current license is valid
	 * for the current computer, where the license is not expired, the current product version was released within the
	 * support period, and the computer using this product is the same since the license has been granted.
	 */
	LicenseValidationResult validate(RemoteValidationAction licenseValidationAction);


	/**
	 * Validates a given license file containing one or multiple license entries. Does not attempt to validate the
	 * license stored in the operating-system store nor the license file given by {@link #getLicenseFilePath()}.
	 *
	 * <strong>NOTE:</strong> this will return immediately for a quick initial validation based on the license entries
	 * found in the given file, but it will also synchronize the given file with the license server in a separate process.
	 * The remote validation result is cached locally and in the server for a few hours. Subsequent calls to this method
	 * will produce the previous validation result immediately.
	 *
	 * The remote synchronization and validation uses the servers provided by {@link StoreDetails#licenseServerDomains()})
	 * and is potentially slow. If any changes have been applied to the relevant license entry (revoke, renewal, etc)
	 * the given file will be updated accordingly, and if the online validation result is different from the initial offline
	 * validation, the {@link RemoteValidationAction} provided as a parameter to this method will be called. If both
	 * offline and online validation produce the same result the {@link RemoteValidationAction} won't be called.
	 *
	 * @param licenseFile             a license file containing one or more license entries. It <strong>MUST</strong>
	 *                                have read and write permissions.
	 * @param licenseValidationAction action to be performed once the remote license validation completed. A
	 *                                {@link LicenseValidationResult} will be sent to the caller via
	 *                                {@link RemoteValidationAction#licenseValidated(LicenseValidationResult)}.
	 *
	 * @return the result of the offline license validation operation, i.e. whether the given file has an entry with
	 * a license that valid for the current computer, where the license entry is not expired, the current product
	 * version was released within the support period, and the computer using this product is the same since the
	 * license has been granted.
	 */
	LicenseValidationResult validate(final File licenseFile, RemoteValidationAction licenseValidationAction);
}