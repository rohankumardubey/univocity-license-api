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
	INCOMPLETE(1, "Incomplete"),
	/**
	 * The license has expired, i.e. {@link License#getLicenseExpirationDate()} is not {@code null} and the current date
	 * is after the expiration date.
	 */
	EXPIRED(2, "Expired"),
	/**
	 * The current version of the software being used is not supported by the current license, i.e.
	 * {@link License#getSupportEndDate()} is not {@code null} and {@link ProductVersion#releaseDate()} is after the
	 * support end date.
	 */
	SUPPORT_ENDED(3, "Support ended"),

	/**
	 * Indicates the current hardware doesn't match the original hardware signature from when the license was generated.
	 * The license can be reassigned to the current hardware using {@link LicenseManager#assignLicense(String, String)}
	 */
	UNKNOWN_HOST(4, "Unknown host"),

	/**
	 * Indicates that the current license is a trial and the trial period has ended
	 */
	TRIAL_EXPIRED(5, "Expired"),

	/**
	 * Indicates the user tried to register for a trial license for a second time. This error is only returned by the
	 * remote server.
	 */
	RETRIAL_ATTEMPTED(6, "Retrial attempted"),

	/**
	 * There is no license present locally. Note that license validation process will update the local license after
	 * every call to {@link LicenseManager#validate(LicenseValidationAction)}. If the license got reassigned to someone
	 * else the local license will be deleted.
	 */
	NOT_FOUND(7, "Not found"),

	/**
	 * Indicates the license is invalid, i.e. product or registration information doesn't match, or license has been
	 * tampered with.
	 */
	INVALID(8, "Invalid"),

	/**
	 * Returned by the remote server to indicate that a purchased license has
	 * been reassigned to someone else. The local license will be removed automatically and further calls to
	 * {@link LicenseManager#validate(LicenseValidationAction)} should produce {@link #NOT_FOUND}
	 */
	DISABLED(9, "Disabled"),

	/**
	 * Returned by the remote server to indicate that trial licenses have been disabled.
	 */
	TRIALS_DISABLED(11, "Trials disabled"),

	/**
	 * Indicates an invalid attempt to transfer an existing license to a new user or device.
	 */
	LICENSE_TRANSFER_DISABLED(12, "License transfer disabled"),

	/**
	 * The license is valid
	 */
	VALID(10, "Valid"),

	/**
	 * An internal error occurred while validating the license. Does not mean that the license is invalid.
	 */
	ERROR(0, "Error");

	public final int code;
	public final String description;

	private static final LicenseValidationResult[] CODE_MAP;

	static {
		int length = LicenseValidationResult.values().length;
		CODE_MAP = new LicenseValidationResult[length];
		for (LicenseValidationResult t : LicenseValidationResult.values()) {
			CODE_MAP[t.code] = t;
		}
	}

	LicenseValidationResult(int code, String description) {
		this.code = code;
		this.description = description;
	}

	/**
	 * Returns the {@code LicenseValidationResult} associated with a given code.
	 *
	 * @param code the code of a {@code LicenseValidationResult}
	 *
	 * @return the value associated with the code.
	 */
	public static LicenseValidationResult fromCode(int code) {
		return CODE_MAP[code];
	}
}
