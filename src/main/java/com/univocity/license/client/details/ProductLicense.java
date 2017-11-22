/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *  
 */

package com.univocity.license.client.details;


import com.univocity.api.*;
import com.univocity.api.common.*;
import com.univocity.license.client.*;

import java.util.*;

/**
 * License details of a given product and its user.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
class ProductLicense implements License {

	private final License license;

	/**
	 * Creates a new trial license for a given product.
	 *
	 * @param email       e-mail address of the user to whom the trial will be assigned.
	 * @param firstName   first name the user to whom the trial will be assigned.
	 * @param lastName    last name of the user to whom the trial will be assigned.
	 * @param productInfo the product details for which a trial will be generated.
	 *
	 * @throws LicenseAssignmentException if any error occurs assigning the trial license
	 */
	static final License trial(String email, String firstName, String lastName, Product productInfo) throws LicenseAssignmentException {
		return new ProductLicense(email, firstName, lastName, null, productInfo);
	}

	/**
	 * Assigns a new license for a given product to a user.
	 *
	 * @param email       e-mail address of the user to whom the license was assigned.
	 * @param firstName   first name the user to whom the license was assigned.
	 * @param lastName    last name of the user to whom the license was assigned.
	 * @param serialKey   the license key associated with the given user.
	 * @param productInfo the product details for which a license will be assigned.
	 *
	 * @throws LicenseAssignmentException if any error occurs assigning the license
	 */
	static final License assign(String email, String firstName, String lastName, String serialKey, Product productInfo) throws LicenseAssignmentException {
		Args.notBlank(serialKey, "Serial key");
		return new ProductLicense(email, firstName, lastName, serialKey, productInfo);
	}

	private ProductLicense(String email, String firstName, String lastName, String serialKey, Product productInfo) throws LicenseAssignmentException {
		Args.notBlank(email, "E-mail address");
		Args.notBlank(firstName, "First name");
		Args.notBlank(lastName, "Last name");
		Args.notNull(productInfo, "Product information");

		license = Builder.build(License.class, email, firstName, lastName, serialKey, productInfo);
	}

	@Override
	public final String getStoreName() {
		return license.getStoreName();
	}

	@Override
	public final String getEmail() {
		return license.getEmail();
	}

	@Override
	public final String getFirstName() {
		return license.getFirstName();
	}

	@Override
	public final String getLastName() {
		return license.getLastName();
	}

	@Override
	public final String getSerialKey() {
		return license.getSerialKey();
	}

	@Override
	public final String getProductName() {
		return license.getProductName();
	}

	@Override
	public final String getProductVersion() {
		return license.getProductVersion();
	}

	@Override
	public final String getProductVariant() {
		return license.getProductVariant();
	}

	@Override
	public final Calendar getVersionReleaseDate() {
		return license.getVersionReleaseDate();
	}

	@Override
	public final Calendar getLicenseExpirationDate() {
		return license.getLicenseExpirationDate();
	}

	@Override
	public final Calendar getSupportEndDate() {
		return license.getSupportEndDate();
	}

	@Override
	public final String toString() {
		return license.toString();
	}
}

