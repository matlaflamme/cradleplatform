package com.cradlerest.web.util.datagen;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages {@code DataPass} objects for various class types.
 */
class DataPassManager {

	private Map<Class<?>, DataPass> registeredPasses = new HashMap<>();

	void register(@NotNull Class<?> type, @NotNull DataPass pass) {
		registeredPasses.put(type, pass);
	}

	@Nullable
	DataPass get(@NotNull Class<?> type) {
		return registeredPasses.get(type);
	}
}
