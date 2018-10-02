/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *
 */

package com.univocity.api.license;

import static com.univocity.api.license.LicenseValidationResult.*;

/**
 * Exception for notification of license registration errors that may happen when activating a license with
 * <ul>
 * <li>{@link LicenseManager#assignTrial(String, String, String)}</li>
 * <li>{@link LicenseManager#assignLicense(String, String)}}</li>
 * </ul>
 *
 * It wraps a {@link LicenseValidationResult} enum to assist in handling the errors.
 */
public class LicenseRegistrationException extends Exception {

	private static final long serialVersionUID = 7372252139622146723L;

	private final LicenseValidationResult validationResult;

	/**
	 * Creates a new license registration exception caused by a license validation error.
	 *
	 * @param validationResult the type of validation error
	 * @param message          the description of the error, with license-specific details about what went wrong.
	 */
	public LicenseRegistrationException(LicenseValidationResult validationResult, String message) {
		super(message, null);
		this.validationResult = validationResult;
	}

	/**
	 * Creates a new license registration exception caused by a system error. Method {@link #getValidationResult()}
	 * will return {@link LicenseValidationResult#ERROR}
	 *
	 * @param message the description of the error.
	 * @param cause   the cause of the error.
	 */
	public LicenseRegistrationException(String message, Throwable cause) {
		super(message, cause);
		this.validationResult = ERROR;
	}

	/**
	 * Returns the error type that occurred when processing the license registration.
	 *
	 * @return the type of error raised when processing the license.
	 */
	public LicenseValidationResult getValidationResult() {
		return validationResult;
	}
}
