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
 * @author Univocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
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
	 * Serial key of the license, given to the user via e-mail and used for license activation.
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
	 * Will be blank if no variant exists (i.e. license is a trial or the product has a single default variant).
	 *
	 * @return the product variant description.
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
	 * @return the license expiration date or {@code null} if the license doesn't expire.
	 */
	Calendar getLicenseExpirationDate();

	/**
	 * The support end date. The license won't work with product versions released after this date.
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

	/**
	 * Returns a formatted {@code String} representation of the date when the current product version was released.
	 *
	 * @param datePattern the date mask to be used to format the date.
	 *
	 * @return the release date of this particular product version, formatted according to the given date pattern.
	 */
	String getVersionReleaseDate(String datePattern);

	/**
	 * Returns a formatted {@code String} representation of the license expiration date, if available. Otherwise
	 * {@code null}
	 *
	 * @param datePattern the date mask to be used to format the date.
	 *
	 * @return the formatted license expiration date or {@code null} if the license doesn't expire.
	 */
	String getLicenseExpirationDate(String datePattern);

	/**
	 * Returns a formatted {@code String} representation of the support end date. The license won't work with product
	 * versions released after this date.
	 *
	 * @param datePattern the date mask to be used to format the date.
	 *
	 * @return the formatted license support end date.
	 */
	String getSupportEndDate(String datePattern);
}
