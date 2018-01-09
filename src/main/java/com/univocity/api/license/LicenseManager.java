/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *
 */

package com.univocity.api.license;

import com.univocity.api.license.details.*;

import java.io.*;
import java.net.*;

/**
 * A validator for licenses of any given product whose information is provided by {@link Product}.
 *
 * Licenses can be individual to a computer and therefore stored locally in a operating-system dependent fashion
 * (for individual users), or grouped together in a single license file containing multiple license entries
 * (for multiple servers where individual license management would be painful).
 */
public interface LicenseManager {

	/**
	 * Returns the path where the license for this particular product version is expected to be located.
	 *
	 * The license file is not mandatory and it will only be this file will only be used if a license can't be found on the
	 * operating-system store. Use {@code setLicenseFilePath(null)} to disable usage of local files.
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
	 * The license file is not mandatory and it this file will only be used if a license can't be found on the
	 * operating-system store. Use {@code setLicenseFilePath(null)} to disable usage of local files.
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
	 * Assigns a license from a file containing one or multiple license entries. If a valid entry is found,
	 * that entry will be stored in the operating-system store and the file given by {@link #getLicenseFilePath()}.
	 *
	 * If the information provided in the chosen entry matches with a license purchase order and its hardware assignment
	 * details, the server will return the updated signed license details and this information will be stored locally.
	 *
	 * If the license server can't be reached, the license information can be updated offline by calling this method again
	 * with an updated license file.
	 *
	 * <strong>NOTE:</strong> this method call may take some time to complete. Running it in a separate thread is
	 * recommended to prevent locking up your user interface (if applicable).
	 *
	 * @param licenseFile a license file containing one or more license entries. It <strong>MUST</strong>
	 *                    have read permissions.
	 *
	 * @return a license for this product, assigned to the current hardware.
	 *
	 * @throws LicenseRegistrationException if any error occurs assigning the license
	 */
	License assignLicense(final File licenseFile) throws LicenseRegistrationException;

	/**
	 * Assigns a license for this product to a user. The information provided in the parameters and the hardware
	 * identity will be sent to the license server to generate a {@link License} object. If the information provided
	 * matches with a license purchase order and its user assignment details, the server will return the signed license
	 * details and this information will be stored locally.
	 *
	 * <strong>NOTE:</strong> this method call may take some time to complete. Running it in a separate thread is
	 * recommended to prevent locking up your user interface (if applicable).
	 *
	 * @param email     e-mail address of the user to whom a license was assigned.
	 * @param serialKey the license key associated with the given user. The serial should have been e-mailed to the
	 *                  {@code email} address provided beforehand.
	 *
	 * @return a license for this product, assigned to the given user and current hardware.
	 *
	 * @throws LicenseRegistrationException if any error occurs assigning the license
	 */
	License assignLicense(String email, String serialKey) throws LicenseRegistrationException;

	/**
	 * Assigns a trial license for this product to a user. The information provided in the parameters and the hardware
	 * identity will be sent to the license server to generate a {@link License} object. If the information provided
	 * is accepted, the server will return the signed license details and this information will be stored locally.
	 *
	 * <strong>NOTE:</strong> this method call may take some time to complete. Running it in a separate thread is
	 * recommended to prevent locking up your user interface (if applicable).
	 *
	 * @param email     e-mail address of the user to whom a license will be assigned.
	 * @param firstName first name the user to whom a license will be assigned.
	 * @param lastName  last name of the user to whom a license will be assigned.
	 *
	 * @return a trial license for this product, assigned to the given user and current hardware.
	 *
	 * @throws LicenseRegistrationException if any error occurs assigning the trial license
	 */
	License assignTrial(String email, String firstName, String lastName) throws LicenseRegistrationException;

	/**
	 * Returns the license associated with the current product, user and hardware, if available.
	 *
	 * If no license information is found, the license can be obtained from the license server using
	 * {@link #assignLicense(String, String)} or {@link #assignTrial(String, String, String)}.
	 * Alternatively, a license can be assigned offline using a
	 * license file and calling the {@link #assignLicense(File)} method to activate it.
	 *
	 * The {@code License} returned will contain the information stored in the operating-system dependent store. If
	 * that store is not available, the information will come from the file specified in {@link #getLicenseFilePath()}.
	 *
	 * @return the current license details, or {@code null} if no license could be found.
	 */
	License getLicense();

	/**
	 * Validates the local license currently associated with the product (via {@link #getLicense()}). The license
	 * is expected to be stored locally in an operating-system dependent store. A file-based copy of the license will
	 * be used if the operating system store is not available. The path to this file is determined by the result of method
	 * {@link #getLicenseFilePath()} - it will only be used if a license can't be found on the operating-system store.
	 *
	 * <strong>NOTE:</strong> this will return immediately for a quick initial validation based on the license stored
	 * locally, but it will also synchronize the local license with the license server in a separate process.
	 * The remote validation result is cached locally and in the server for a few hours. Subsequent calls to this method
	 * will produce the previous validation result immediately.
	 *
	 * The remote synchronization and validation uses the server provided by {@link Store#licenseServerDomain()})
	 * and is potentially slow. If any changes have been applied to the license (revoke, renewal, etc) the locally stored
	 * license will be updated accordingly, and if the online validation result is different from the initial offline
	 * validation, the {@link LicenseValidationAction} provided as a parameter to this method will be called. If both
	 * offline and online validation produce the same result the {@link LicenseValidationAction} won't be called.
	 *
	 * If the client code runs offline and license server can't be reached, updates to the license can only be performed
	 * manually by obtaining a updated license file and passing it to the method {@link #assignLicense(File)}.
	 *
	 * @param licenseValidationAction action to be performed once the remote license validation completed. A
	 *                                {@link LicenseValidationResult} will be sent to the caller via
	 *                                {@link LicenseValidationAction#licenseValidated(LicenseValidationResult)}.
	 *
	 * @return the result of the offline license validation operation, i.e. whether the current license is valid
	 * for the current computer, where the license is not expired, the current product version was released within the
	 * support period, and the computer using this product is the same since the license has been granted.
	 */
	LicenseValidationResult validate(LicenseValidationAction licenseValidationAction);

	/**
	 * Deletes the license information stored locally, forcing the user to register the license again.
	 */
	void deleteLicense();

	/**
	 * Returns the {@link Product} managed by the license manager.
	 *
	 * @return the {@link Product} associated with this license manager.
	 */
	Product getProduct();

	/**
	 * Convenience method to return the name of the product followed by its variant description, if any.
	 * The variant may determined by the license or be part of the product information itself. If the variant information
	 * is available (from either product information, or the license currently assigned), the description returned will
	 * be in the format {@code "product_name - variant_description"}, otherwise only the product name will be returned.
	 *
	 * @return a description of the current product, including its variant if available.
	 */
	String getProductDescription();

	/**
	 * Configures the license manager to connect to the license server through a proxy, with authentication.
	 *
	 * @param proxyType an optional existing proxy configuration.
	 * @param proxyHost the proxy host.
	 * @param proxyPort the proxy port.
	 * @param user      the proxy user.
	 * @param password  the proxy password
	 */
	void setProxy(Proxy.Type proxyType, String proxyHost, int proxyPort, String user, String password);

	/**
	 * Configures the license manager to connect to the license server through a proxy, with authentication.
	 *
	 * @param proxy    the proxy to be used
	 * @param user     the proxy user.
	 * @param password the proxy password
	 */
	void setProxy(Proxy proxy, String user, String password);

	/**
	 * Configures the license manager to connect to the license server through a proxy, without authentication.
	 *
	 * @param proxyType an optional existing proxy configuration.
	 * @param proxyHost the proxy host.
	 * @param proxyPort the proxy port.
	 */
	void setProxy(Proxy.Type proxyType, String proxyHost, int proxyPort);

	/**
	 * Configures the license manager to connect to the license server through a proxy, without authentication.
	 *
	 * @param proxy the proxy to be used
	 */
	void setProxy(Proxy proxy);

	/**
	 * Returns the configured proxy to be used to connect to the license server.
	 *
	 * @return the proxy.
	 */
	Proxy getProxy();

	/**
	 * Returns the username to authenticate with the configured proxy for connection to the license server.
	 *
	 * @return the proxy username.
	 */
	String getProxyUser();

	/**
	 * Returns the password to authenticate with the configured proxy for connection to the license server.
	 *
	 * @return the proxy user's password.
	 */
	char[] getProxyPassword();

}