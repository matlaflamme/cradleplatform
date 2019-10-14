package com.cradlerest.web.util.datagen;

import com.cradlerest.web.util.datagen.error.ForeignKeyException;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;

public interface ForeignKeyRepository {
	void put(@NotNull Class<?> type, @NotNull String column, @NotNull Object value);

	Vec<Object> get(@NotNull Class<?> type, @NotNull String column);

	Vec<Object> get(@NotNull Class<?> type) throws ForeignKeyException;
}
