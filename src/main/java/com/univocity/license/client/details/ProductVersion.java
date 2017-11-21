/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *
 */

package com.univocity.license.client.details;

import com.univocity.api.common.*;
import com.univocity.commons.lang.*;
import com.univocity.commons.lang.time.*;

import java.util.*;

/**
 * Provides the essential information required from a product version
 */
public final class ProductVersion {

	private final String formattedReleaseDate;
	private final String id;
	private final Calendar releaseDate;

	/**
	 * Builds a new product version, with ID and release date, for a given product variant instance
	 *
	 * @param id          the version identifier, typically specified as {@code [MAJOR].[MINOR].[MAINTENANCE]}, e.g. {@code "2.4.16"}
	 * @param releaseDate date this particular version was released. <strong>MUST</strong> be formatted as {@code "yyyy-MM-dd"}.
	 */
	public ProductVersion(String id, String releaseDate) {
		Args.notBlank(id, "Version ID");
		Args.notBlank(releaseDate, "Release date of version '" + id + "'");

		releaseDate = releaseDate.trim();

		this.releaseDate = Args.isoDateStringToCalendar(releaseDate);
		this.id = id;
		this.formattedReleaseDate = releaseDate;
	}

	/**
	 * Returns the current product version, typically formatted as {@code "1.0.0"} but no hard restriction are imposed.
	 *
	 * @return the current product version.
	 */
	public final String id() {
		return id;
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
		return id;
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductVersion that = (ProductVersion) o;

		return id.equals(that.id);
	}

	@Override
	public final int hashCode() {
		return id.hashCode();
	}
}
