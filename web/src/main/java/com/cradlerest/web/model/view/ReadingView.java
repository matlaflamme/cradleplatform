package com.cradlerest.web.model.view;

import com.cradlerest.web.model.Reading;
import com.cradlerest.web.util.datagen.annotations.Omit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code ReadingView} is the entity that the front-end interacts with when
 * dealing with readings. This class is an aggregation of the {@code Reading}
 * (which is faithful to the database's definition of a reading) and other
 * components (e.g., symptoms).
 *
 * It is the duty of the {@code ReadingManager} service to convert between
 * {@code ReadingView}s which the front-end interacts with and {@code Reading}
 * objects which the database interacts with.
 */
@Omit
public class ReadingView extends Reading {

	private List<SymptomView> symptoms = new ArrayList<>();

	public ReadingView() {}

	@NotNull
	public List<SymptomView> getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(@NotNull List<SymptomView> symptoms) {
		this.symptoms = symptoms;
	}
}
