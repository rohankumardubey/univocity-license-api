/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *
 */

package com.univocity.api.license.details;

import com.univocity.api.common.*;

import java.util.*;

/**
 * Provides the essential information required from a product version
 */
public final class ProductVersion {

	private final String formattedReleaseDate;
	private final String identifier;
	private final Calendar releaseDate;

	/**
	 * Builds a new product version, with ID and release date, for a given product variant instance
	 *
	 * @param versionIdentifier the version identifier, typically specified as {@code [MAJOR].[MINOR].[MAINTENANCE]}, e.g. {@code "2.4.16"}
	 * @param releaseDate   date this particular version was released. <strong>MUST</strong> be formatted as {@code "yyyy-MM-dd"}.
	 */
	public ProductVersion(String versionIdentifier, String releaseDate) {
		Args.notBlank(versionIdentifier, "Version ID");
		Args.notBlank(releaseDate, "Release date of version '" + versionIdentifier + "'");

		releaseDate = releaseDate.trim();

		this.releaseDate = Args.isoDateStringToCalendar(releaseDate);
		this.identifier = versionIdentifier;
		this.formattedReleaseDate = releaseDate;
	}

	/**
	 * Returns the current product version, typically formatted as {@code "1.0.0"} but no hard restriction are imposed.
	 *
	 * @return the current product version.
	 */
	public final String identifier() {
		return identifier;
	}

	/**
	 * Returns the release date of the current product version.
	 *
	 * @return the release date of the current product version.
	 */
	public final Calendar releaseDate() {
		return releaseDate;
	}

	/**
	 * Returns the release date of the current product version formatted as {@code "yyyy-MM-dd"}.
	 *
	 * @return the release date of the current product version.
	 */
	public final String formattedReleaseDate() {
		return formattedReleaseDate;
	}

	@Override
	public final String toString() {
		return identifier;
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductVersion that = (ProductVersion) o;

		return identifier.equals(that.identifier);
	}

	@Override
	public final int hashCode() {
		return identifier.hashCode();
	}
}
