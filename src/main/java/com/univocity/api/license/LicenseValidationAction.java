/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *
 */

package com.univocity.api.license;

/**
 * An action to be performed asynchronously when the license server returns its {@link LicenseValidationResult} and
 * the result is different from the local license validation.
 *
 * Used by {@link LicenseManager#validate(LicenseValidationAction)}
 */
public interface LicenseValidationAction {

	/**
	 * Notifies that the remote license validation produced a different result than the local offline validation. This
	 * indicates that the license has been modified (renewed, revoked, etc) and the local copy has been updated accordingly.
	 *
	 * Use the {@link LicenseValidationResult} to identify whether the license is valid, or if not, why.
	 *
	 * @param result the result of the license validation performed in the server.
	 */
	void licenseValidated(LicenseValidationResult result);
}
