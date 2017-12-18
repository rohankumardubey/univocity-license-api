/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *
 */

package com.univocity.api.license;

import java.util.*;

/**
 * License details of a given product and its user.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public interface License {

	/**
	 * Name of the store/company that owns the product
	 *
	 * @return the store name
	 */
	String getStoreName();

	/**
	 * E-mail address of the user who has this license assigned to.
	 *
	 * @return the user e-mail address
	 */
	String getEmail();

	/**
	 * First name of the user who has this license assigned to.
	 *
	 * @return the first name of the user
	 */
	String getFirstName();

	/**
	 * Last name of the user who has this license assigned to.
	 *
	 * @return the last name of the user
	 */
	String getLastName();

	/**
	 * Serial key of the license, given to the user via e-mail.
	 *
	 * @return the license serial key
	 */
	String getSerialKey();

	/**
	 * Indicates whether this license is a trial license. Shorthand for {@code getSerialKey() == null}.
	 *
	 * @return {@code true} if this is a trial license, {@code false} otherwise.
	 */
	boolean isTrial();

	/**
	 * Name of the product associated with this license
	 *
	 * @return the product name
	 */
	String getProductName();

	/**
	 * Version of the product associated with this license
	 *
	 * @return the product version
	 */
	String getProductVersion();

	/**
	 * Variant description of the product associated with this license (e.g. "enterprise", "professional", etc).
	 * Will be blank if no variants exist.
	 *
	 * @return the product variant.
	 */
	String getProductVariant();

	/**
	 * Date the current product version was released.
	 *
	 * @return the release date of this particular product version.
	 */
	Calendar getVersionReleaseDate();

	/**
	 * The license expiration date, if any. The license won't be valid after the given expiration date.
	 *
	 * @return the license expiration date.
	 */
	Calendar getLicenseExpirationDate();

	/**
	 * The support end date. The license won't work with product versions released after this date..
	 *
	 * @return the license support end date.
	 */
	Calendar getSupportEndDate();

	/**
	 * An encoded representation of this license. Used for license validation. Not human readable.
	 *
	 * @return the license text.
	 */
	String toString();
}
