package com.cradlerest.web.service.mock;

import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.service.ReadingManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Mock implementation of {@code ReadingManager}
 */
public class MockReadingManager implements ReadingManager {

	@Override
	public ReadingView getReadingView(@NotNull Integer readingId) {
		// TODO: implement me
		return null;
	}

	@Override
	public List<ReadingView> getAllReadingViewsForPatient(@NotNull String patientId) {
		// TODO: implement me
		return null;
	}

	@Override
	public void saveReadingView(@NotNull ReadingView readingView) {
		// TODO: implement me
	}
}
