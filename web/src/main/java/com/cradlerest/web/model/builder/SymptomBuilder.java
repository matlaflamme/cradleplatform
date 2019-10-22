package com.cradlerest.web.model.builder;

import com.cradlerest.web.model.Symptom;
import com.cradlerest.web.model.view.SymptomView;
import org.jetbrains.annotations.NotNull;

import static com.cradlerest.web.util.Validation.assertFieldNotNull;

/**
 * Builder class for {@code Symptom} and {@code SymptomView} objects.
 */
public class SymptomBuilder extends AbstractBuilder<Symptom> {

	public SymptomBuilder() {
		this.value = new Symptom();
	}

	public SymptomBuilder id(int id) {
		value.setId(id);
		return this;
	}

	public SymptomBuilder text(@NotNull String text) {
		value.setText(text);
		return this;
	}

	public SymptomView buildView() throws InstantiationError {
		assertFieldNotNull(value.getText(), "text");
		return value;
	}

	@Override
	public Symptom build() throws InstantiationError {
		assertFieldNotNull(value.getId(), "id");
		assertFieldNotNull(value.getText(), "text");
		return value;
	}
}
