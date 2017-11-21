/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *
 */

package com.univocity.license.client;

public enum LicenseValidationResult {
	INCOMPLETE,
	EXPIRED,
	SUPPORT_ENDED,
	UNKNOWN_HOST,
	TRIAL_EXPIRED,
	RETRIAL_ATTEMPTED, //remote only
	NOT_FOUND,
	INVALID,
	DISABLED, //remote only
	VALID,

}
