package com.cradlerest.web.model.builder;

import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.model.view.SymptomView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The builder class for {@code ReadingView} objects.
 *
 * Utilizes the {@code AbstractReadingBuilder} to provide builder methods for
 * the common fields in the {@code Reading} base class.
 *
 * @see AbstractReadingBuilder
 * @see ReadingView
 */
public class ReadingViewBuilder extends AbstractReadingBuilder<ReadingView, ReadingViewBuilder> {

	public ReadingViewBuilder() {
		this.value = new ReadingView();
	}

	public ReadingViewBuilder symptoms(@NotNull List<SymptomView> symptomViews) {
		value.setSymptoms(symptomViews);
		return self();
	}

	// TODO: override `build` to validate before building

	@Override
	protected ReadingViewBuilder self() {
		return this;
	}
}
