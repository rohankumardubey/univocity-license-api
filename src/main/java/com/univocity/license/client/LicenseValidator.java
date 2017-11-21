/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *
 */

package com.univocity.license.client;

import com.univocity.license.client.details.*;

import java.io.*;

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

	void setLicense();

	/**
	 * Validates the license currently associated with the product <strong>asynchronously</strong>. The license is expected
	 * to be stored locally in an operating-system dependent store. A file-based copy of the license will be used if the
	 * operating system store is not available. The path to this file is determined by the result of method
	 * {@link #getLicenseFilePath()} - it will only be used if a license can't be found on the operating-system store.
	 *
	 * This is the preferred way to validate the license as the remote validation (using the servers provided by
	 * {@link StoreDetails#licenseServerDomains()}) is potentially slow.
	 *
	 * <strong>NOTE</strong> the remote validation result is cached locally and in the server for a few hours. Subsequent
	 * calls will return return the previous validation result immediately.
	 *
	 * @param licenseValidationAction action to be performed once the license validation completed. A
	 *                                {@link LicenseValidationResult} will be sent to the caller via
	 *                                {@link LicenseValidationAction#licenseValidated(LicenseValidationResult)}.
	 */
	void validateOnline(LicenseValidationAction licenseValidationAction);

	/**
	 * Validates the license currently associated with the product <strong>synchronously</strong>. The license is expected
	 * to be stored locally in an operating-system dependent store. A file-based copy of the license will be used if the
	 * operating system store is not available. The path to this file is determined by the result of method
	 * {@link #getLicenseFilePath()} - it will only be used if a license can't be found on the operating-system store.
	 *
	 * <strong>NOTE</strong> that this method will block and may render your application unresponsive as the remote
	 * validation (using the servers provided by {@link StoreDetails#licenseServerDomains()}) is potentially slow.
	 *
	 * @return the result of the license validation operation.
	 */
	LicenseValidationResult validateOnline();

	/**
	 * Validates a given license file. Does not attempt to validate the license stored in the operating-system store.
	 *
	 * @param licenseFile the file containing one or more licenses for the current product
	 *
	 * @return whether the given license file has a valid license entry, i.e. the license is not expired, the
	 * current product version was released within the support period, and the computer using this product is the same
	 * since the license has been granted.
	 */
	LicenseValidationResult validate(final File licenseFile);
}

