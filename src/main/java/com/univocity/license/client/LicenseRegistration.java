/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *
 */
package com.univocity.license.client;

import com.univocity.api.*;
import com.univocity.api.common.*;
import com.univocity.license.client.details.*;


/**
 * Essential information used to assign a license for a given product to a user.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public final class LicenseRegistration {

	private final Object licenseRegistration;

	/**
	 * Creates a new trial license for a given product.
	 *
	 * @param email       e-mail address of the user to whom the trial will be assigned.
	 * @param firstName   first name the user to whom the trial will be assigned.
	 * @param lastName    last name of the user to whom the trial will be assigned.
	 * @param productInfo the product details for which a trial will be generated.
	 */
	public static final LicenseRegistration trial(String email, String firstName, String lastName, ProductDetails productInfo) {
		return new LicenseRegistration(email, firstName, lastName, null, productInfo);
	}

	/**
	 * Assigns a new license for a given product to a user.
	 *
	 * @param email       e-mail address of the user to whom the license was assigned.
	 * @param firstName   first name the user to whom the license was assigned.
	 * @param lastName    last name of the user to whom the license was assigned.
	 * @param serialKey   the license key associated with the given user.
	 * @param productInfo the product details for which a license will be assigned.
	 */
	public static final LicenseRegistration assign(String email, String firstName, String lastName, String serialKey, ProductDetails productInfo) {
		Args.notBlank(serialKey, "Serial key");
		return new LicenseRegistration(email, firstName, lastName, serialKey, productInfo);
	}

	private LicenseRegistration(String email, String firstName, String lastName, String serialKey, ProductDetails productInfo) {
		Args.notBlank(email, "E-mail address");
		Args.notBlank(firstName, "First name");
		Args.notBlank(lastName, "Last name");
		Args.notNull(productInfo, "Product information");

		licenseRegistration = Builder.build(LicenseRegistration.class, email, firstName, lastName, serialKey, productInfo);
	}

	public final String toString() {
		return licenseRegistration.toString();
	}
}

