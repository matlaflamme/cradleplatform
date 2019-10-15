package com.cradlerest.web.util.datagen.mock;

import com.cradlerest.web.util.datagen.Generator;
import com.github.maumay.jflow.utils.Tup;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;

public class MockGenerator<T> implements Generator<T> {

	@NotNull
	private T value;

	@NotNull
	private Vec<Tup<String, Object>> curriedParameters = Vec.of();

	public MockGenerator(@NotNull T value) {
		this.value = value;
	}

	@NotNull
	public T getValue() {
		return value;
	}

	public void setValue(@NotNull T value) {
		this.value = value;
	}

	@NotNull
	public Vec<Tup<String, Object>> getCurriedParameters() {
		return curriedParameters;
	}

	@Override
	public T generate() {
		return value;
	}

	@Override
	public Generator<T> with(@NotNull String key, @NotNull Object value) {
		curriedParameters = curriedParameters.append(Tup.of(key, value));
		return this;
	}
}
