/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *
 */

package com.univocity.api.license;

import java.io.*;

/**
 * Exception for notification of license registration errors that may happen when activating a license with
 * <ul>
 *     <li>{@link LicenseManager#assignTrial(String, String, String)}</li>
 *     <li>{@link LicenseManager#assignLicense(String, String, String, String)}}</li>
 *     <li>{@link LicenseManager#assignLicense(File)}</li>
 * </ul>
 *
 *
 */
public class LicenseRegistrationException extends Exception {

	private static final long serialVersionUID = 7372252139622146723L;

	private final LicenseValidationResult validationResult;

	public LicenseRegistrationException(LicenseValidationResult validationResult, String message) {
		this(validationResult, message, null);
	}

	public LicenseRegistrationException(LicenseValidationResult validationResult, String message, Throwable cause) {
		super(message, cause);
		this.validationResult = validationResult;
	}

	public LicenseValidationResult getValidationResult() {
		return validationResult;
	}
}
