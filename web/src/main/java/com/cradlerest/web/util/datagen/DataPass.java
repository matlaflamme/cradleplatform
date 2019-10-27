package com.cradlerest.web.util.datagen;

import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for post generation data passes.
 *
 * Allows for some post processing of the data after it has been generated. One
 * major usage would be to enforce a unique constraint on a given field or set
 * of fields.
 */
public interface DataPass {

	/**
	 * Scans the generated data, makes modifications (additions, removals) and
	 * returns a new data vector as the result of the pass.
	 * @param data The data to scan.
	 * @return The updated data.
	 */
	@NotNull
	Vec<Data> traverse(@NotNull Vec<Data> data);
}
