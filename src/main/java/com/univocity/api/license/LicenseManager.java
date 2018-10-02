/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *
 */

package com.univocity.api.license;

import com.univocity.api.license.details.*;

import java.awt.*;
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
	 * The license file is not mandatory and will only be used if a license can't be found on the
	 * operating-system store. Use {@code setLicenseFilePath(null)} to disable usage of local files.
	 *
	 * On Windows, defaults to {@code %APPDATA%/[store name]/[product_variant_version]/license} and
	 * On Mac/Linux defaults to {@code [user.home]/.local/share/[store name]/[product_variant_version]/license},
	 * or {@code [user.home]/.[store name]/[product_variant_version]/license} if no {@code .local/share} folder exists under {@code user.home} .
	 *
	 * If the {@code user.home} can't be determined the relative path {@code .[store name]/[product_variant_version]/license} will be used.
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
	 * On Windows, defaults to {@code %APPDATA%/[store name]/[product_variant_version]/license} and
	 * On Mac/Linux defaults to {@code [user.home]/.local/share/[store name]/[product_variant_version]/license},
	 * or {@code [user.home]/.[store name]/[product_variant_version]/license} if no {@code .local/share} folder exists under {@code user.home} .
	 *
	 * If the {@code user.home} can't be determined the relative path {@code .[store name]/[product_variant_version]/license} will be used.
	 *
	 * Upon calling this method, the given path will be tested using the following steps:
	 *  - the given directory path will be created if it doesn't exist;
	 *  - a dummy license file will be created in that directory if no license file exists;
	 *  - if a license file already exists at the given path, it must have read and write permissions.
	 *
	 * This method will return {@code false} if the given path causes any of the aforementioned validations to fail.
	 *
	 * @param licenseFilePath the path to the license file.
	 *
	 * @return a flag indicating whether the path can be successfully used as the license path.
	 */
	boolean setLicenseFilePath(String licenseFilePath);

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
	 * license will be updated accordingly. Use {@link #validate(LicenseValidationAction)} to handle the remote
	 * response (asynchronously) when the server returns the validation result.
	 *
	 * @return the result of the offline license validation operation, i.e. whether the current license is valid
	 * for the current computer, where the license is not expired, the current product version was released within the
	 * support period, and the computer using this product is the same since the license has been granted.
	 */
	LicenseValidationResult validate();


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
	 * @param password  the proxy password (the char array will be copied)
	 */
	void setProxy(Proxy.Type proxyType, String proxyHost, int proxyPort, String user, char[] password);

	/**
	 * Configures the license manager to connect to the license server through a proxy, with authentication.
	 *
	 * @param proxy    the proxy to be used
	 * @param user     the proxy user.
	 * @param password the proxy password (the char array will be copied)
	 */
	void setProxy(Proxy proxy, String user, char[] password);

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
	 * Returns the configured proxy hostname to be used to connect to the license server.
	 *
	 * @return the proxy host.
	 */
	String getProxyHost();

	/**
	 * Returns the configured proxy port to be used to connect to the license server.
	 *
	 * @return the proxy port.
	 */
	int getProxyPort();

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

	/**
	 * Defines the license agreement terms of your product in plain text (optional). Useful for command-line license
	 * activation.
	 *
	 * This is displayed to users during the license activation process. If you provide a non-null value, users must
	 * agree to the terms provided before their license can be activated.
	 *
	 * @param licenseAgreementText the terms of your license, in plain text.
	 */
	void setLicenseAgreementText(String licenseAgreementText);

	/**
	 * Defines the license agreement terms of your product in HTML (optional). Useful for UI-based license
	 * activation. If {@code null} then the plain text version will be used if available.
	 *
	 * This is displayed to users during the license activation process. If you provide a non-null value, or
	 * if {@link #getLicenseAgreementText()} returns a non-null value, users must agree to the terms provided before
	 * their license can be activated.
	 *
	 * @param licenseAgreementHtml the terms of your license, in HTML.
	 */
	void setLicenseAgreementHtml(String licenseAgreementHtml);

	/**
	 * Returns the license agreement terms of your product in plain text. Useful for command-line license
	 * activation.
	 *
	 * This is displayed to users during the license activation process. If a non-null value is returned, users will
	 * have to agree to the terms provided before their license can be activated.
	 *
	 * @return the terms of your license, in plain text.
	 */
	String getLicenseAgreementText();

	/**
	 * Returns the license agreement terms of your product in HTML. Useful for UI-based license activation.
	 *
	 * This is displayed to users during the license activation process. If a non-null value is returned, users will
	 * have to agree to the terms provided before their license can be activated.
	 *
	 * @return the terms of your license, in HTML.
	 */
	String getLicenseAgreementHtml();

	/**
	 * Returns the license agreement terms of your product in HTML if available, or plain text. Useful for
	 * UI-based license activation. Command-line license activation should not rely on this method as it may return
	 * HTML without any rendering.
	 *
	 * This is displayed to users during the license activation process. If a non-null value is returned, users will
	 * have to agree to the terms provided before their license can be activated.
	 *
	 * @return the terms of your license, in HTML or plain text.
	 */
	String getLicenseAgreement();

	/**
	 * Starts the appropriate license management user interface based on the user's graphics environment. Essentially, if
	 * {@link GraphicsEnvironment#isHeadless()} evaluates to {@code true} the command line interface will be started
	 * (same as {@link #startCmd()}); otherwise the license manager dialog will be displayed (same as {@link #startGui()})
	 */
	void start();

	/**
	 * Displays a license management window to allow users request an evaluation license, activate/deactivate their
	 * purchased license and configure a proxy to be able to access the license server if required.
	 */
	void startGui();

	/**
	 * Displays a license management command-line interface to allow users request an evaluation license,
	 * activate/deactivate their purchased license and configure a proxy to be able to access the license server if required.
	 */
	void startCmd();

	/**
	 * Defines an icon image to be used on the windows and dialogs displayed to the user when
	 * {@link #startGui()} is called.
	 *
	 * @param icon an icon for the license management UI
	 */
	void setIcon(Image icon);

	/**
	 * Defines a logo to be displayed at the top of the license management window displayed when {@link #startGui()}}
	 * is called.
	 *
	 * @param logo a logo for the license management UI
	 */
	void setLogo(Image logo);

	/**
	 * Returns the icon image to be used on the windows and dialogs displayed to the user when
	 * {@link #startGui()} is called.
	 *
	 * @return an icon for the license management UI
	 */
	Image getIcon();

	/**
	 * Returns the logo to be displayed at the top of the license management window displayed when {@link #startGui()}}
	 * is called.
	 *
	 * @return the logo to be displayed by the license management UI
	 */
	Image getLogo();

}