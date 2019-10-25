package com.cradlerest.web.model.builder;

import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.model.view.SymptomView;
import com.github.maumay.jflow.iterator.Iter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.cradlerest.web.util.Validation.assertFieldNotNull;
import static com.cradlerest.web.util.CopyFields.copyFields;

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

	/**
	 * Copies all fields from a {@code Reading} value to the {@code ReadingView}
	 * being built.
	 * @param reading Object to copy fields from.
	 * @return The builder.
	 */
	public ReadingViewBuilder reading(@NotNull Reading reading) {
		copyFields(reading, value);
		return self();
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
