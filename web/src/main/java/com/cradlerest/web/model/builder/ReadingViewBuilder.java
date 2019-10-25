package com.cradlerest.web.model.builder;

import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.model.view.SymptomView;
import com.github.maumay.jflow.iterator.Iter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.cradlerest.web.util.Validation.assertFieldNotNull;

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

	public ReadingViewBuilder symptoms(@NotNull SymptomView... symptomViews) {
		return symptoms(Arrays.asList(symptomViews));
	}

	public ReadingViewBuilder symptoms(@NotNull String... symptomTexts) {
		var symptomViews = Iter.args(symptomTexts)
				.map(text -> (SymptomView) () -> text)
				.toList();
		return symptoms(symptomViews);
	}

	@Override
	public ReadingView build() throws InstantiationError {
		validate();
		return value;
	}

	@Override
	protected ReadingViewBuilder self() {
		return this;
	}

	@Override
	protected void validate() throws InstantiationError {
		super.validate();
		assertFieldNotNull(value.getSymptoms(), "symptoms");
	}
}
