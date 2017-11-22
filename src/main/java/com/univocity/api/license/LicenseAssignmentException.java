/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *
 */

package com.univocity.api.license;

public class LicenseAssignmentException extends Exception {

	private final LicenseValidationResult validationResult;

	public LicenseAssignmentException(LicenseValidationResult validationResult, String message) {
		this(validationResult, message, null);
	}

	public LicenseAssignmentException(LicenseValidationResult validationResult, String message, Throwable cause) {
		super(message, cause);
		this.validationResult = validationResult;
	}

}
