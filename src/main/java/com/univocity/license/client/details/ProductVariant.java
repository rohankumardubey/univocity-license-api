/*
 * Copyright (c) 2017 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 *
 */

package com.univocity.license.client.details;

import com.univocity.api.common.*;

/**
 * Provides the essential information required from a product variant
 */
public final class ProductVariant {

	private final String description;
	private final Long id;

	/**
	 * Builds a new variant, with ID and description, for a given product
	 *
	 * @param id          the ID of this product variant
	 * @param description the description of this product variant.
	 */
	public ProductVariant(Long id, String description) {
		Args.positiveOrZero(id, "Variant ID");
		Args.notBlank(description, "Variant description");

		this.id = id;
		this.description = description;
	}

	/**
	 * Builds a new default variant, with ID only and no description, for a given product
	 *
	 * @param id the ID of this product variant
	 */
	public ProductVariant(Long id) {
		Args.positiveOrZero(id, "Variant ID");

		this.id = id;
		this.description = "";
	}


	/**
	 * Returns the description of the product variant.
	 *
	 * @return the product variant description.
	 */
	public final String description() {
		return description;
	}

	/**
	 * The unique ID of the product variant.
	 *
	 * @return the ID of the product variant.
	 */
	public final Long id() {
		return id;
	}

	@Override
	public final String toString() {
		if (description.length() > 0) {
			return description;
		}
		return super.toString();
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductVariant that = (ProductVariant) o;

		if (!description.equals(that.description)) return false;
		return id.equals(that.id);
	}

	@Override
	public final int hashCode() {
		int result = description.hashCode();
		result = 31 * result + id.hashCode();
		return result;
	}
}
