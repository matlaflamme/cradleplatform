package com.cradlerest.web.util.datagen;

import com.cradlerest.web.util.datagen.error.ForeignKeyException;
import com.github.maumay.jflow.utils.Tup;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Container for storing and retrieving foreign key values.
 */
public class ForeignKeyRepository {

	@NotNull
	private Map<Tup<? extends Class<?>, String>, Vec<Object>> repository = new HashMap<>();

	/**
	 * Registers a foreign key candidate value for a given class and column.
	 * This value may be used by other classes which hold a foreign key
	 * reference to this particular class + column pair.
	 * @param type The class to register this value under.
	 * @param column The column to register this value under.
	 * @param value The value to register.
	 */
	public void put(@NotNull Class<?> type, @NotNull String column, @NotNull Object value) {
		var tup = Tup.of(type, column);
		var keys = repository.get(tup);
		if (keys == null) {
			keys = Vec.of(value);
			repository.put(tup, keys);
			return;
		}

		// sanity check to ensure that all keys have the same type
		assert keys.all(key -> key.getClass().equals(value.getClass()));
		repository.put(tup, keys.append(value));
	}

	/**
	 * Returns all possible valid foreign key values for a particular class and
	 * column.
	 * @param type The class to get foreign key values for.
	 * @param column The column to get foreign key values for.
	 * @return A list of valid foreign key values.
	 * @throws ForeignKeyException if unable to find any foreign key values for
	 * 	the given class, column pair.
	 */
	public Vec<Object> get(@NotNull Class<?> type, @NotNull String column) {
		var keys = repository.get(Tup.of(type, column));
		if (keys == null) {
			throw new ForeignKeyException(type.getName() + " has not registered any keys for column: " + column);
		}
		return keys;
	}
}
