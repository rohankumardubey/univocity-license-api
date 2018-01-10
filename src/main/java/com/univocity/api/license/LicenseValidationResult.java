/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *
 */

package com.univocity.api.license;

import com.univocity.api.license.details.*;

import java.io.*;

/**
 * Encodes all possible types of license validation results. {@link #VALID} is the only type of result that indicates
 * the license is valid. {@link #ERROR} encodes internal errors during the license validation and does not mean necessarily
 * that the license is invalid. Any other result indicates the current license is not valid.
 */
public enum LicenseValidationResult {
	/**
	 * The license registration details are incomplete.
	 */
	INCOMPLETE,
	/**
	 * The license has expired, i.e. {@link License#getLicenseExpirationDate()} is not {@code null} and the current date
	 * is after the expiration date.
	 */
	EXPIRED,
	/**
	 * The current version of the software being used is not supported by the current license, i.e.
	 * {@link License#getSupportEndDate()} is not {@code null} and {@link ProductVersion#releaseDate()} is after the
	 * support end date.
	 */
	SUPPORT_ENDED,

	/**
	 * Indicates the current hardware doesn't match the original hardware signature from when the license was generated.
	 * The license can be reassigned to the current hardware using {@link LicenseManager#assignLicense(File)}
	 * or {@link LicenseManager#assignLicense(String, String)}
	 */
	UNKNOWN_HOST,

	/**
	 * Indicates that the current license is a trial and the trial period has ended
	 */
	TRIAL_EXPIRED,

	/**
	 * Indicates the user tried to register for a trial license for a second time. This error is only returned by the
	 * remote server.
	 */
	RETRIAL_ATTEMPTED,

	/**
	 * There is no license present locally. Note that license validation process will update the local license after
	 * every call to {@link LicenseManager#validate(LicenseValidationAction)}. If the license got reassigned to someone
	 * else the local license will be deleted.
	 */
	NOT_FOUND,

	/**
	 * Indicates the license is invalid, i.e. product or registration information doesn't match, or license has been
	 * tampered with.
	 */
	INVALID,

	/**
	 * Returned by the remote server to indicate that trial licenses have been disabled, or that a purchased license has
	 * been reassigned to someone else. The local license will be removed automatically and further calls to
	 * {@link LicenseManager#validate(LicenseValidationAction)} should produce {@link #NOT_FOUND}
	 */
	DISABLED,

	/**
	 * The license is valid
	 */
	VALID,

	/**
	 * An internal error occurred while validating the license. Does not mean that the license is invalid.
	 */
	ERROR
}
