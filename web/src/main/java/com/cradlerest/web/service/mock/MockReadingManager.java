package com.cradlerest.web.service.mock;

import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.ReadingColour;
import com.cradlerest.web.model.builder.ReadingViewBuilder;
import com.cradlerest.web.model.builder.SymptomBuilder;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.service.ReadingManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cradlerest.web.util.Validation.assertFieldNotNull;

/**
 * Mock implementation of {@code ReadingManager}
 */
public class MockReadingManager implements ReadingManager {

	@Override
	public ReadingView getReadingView(@NotNull Integer readingId) {
		return new ReadingViewBuilder()
				.id(readingId)
				.pid("001")
				.systolic(110)
				.diastolic(80)
				.heartRate(70)
				.colour(ReadingColour.GREEN)
				.pregnant(true)
				.gestationalAgeDays(90)
				.symptoms(new SymptomBuilder().text("Headache").buildView())
				.timestamp("2019-10-24 09:32:10")
				.build();
	}

	@Override
	public List<ReadingView> getAllReadingViewsForPatient(@NotNull String patientId) {
		var reading1 = new ReadingViewBuilder()
				.id(1)
				.pid(patientId)
				.systolic(110)
				.diastolic(80)
				.heartRate(70)
				.colour(ReadingColour.GREEN)
				.pregnant(true)
				.gestationalAgeDays(90)
				.symptoms(new SymptomBuilder().text("Headache").buildView())
				.timestamp("2019-10-24 09:32:10")
				.build();
		var reading2 = new ReadingViewBuilder()
				.id(2)
				.pid(patientId)
				.systolic(130)
				.diastolic(90)
				.heartRate(90)
				.colour(ReadingColour.YELLOW_DOWN)
				.pregnant(true)
				.gestationalAgeDays(110)
				.symptoms(new SymptomBuilder().text("Headache").buildView())
				.timestamp("2019-10-30 09:32:10")
				.build();
		// result should be sorted by timestamp in descending order
		return Arrays.asList(reading2, reading1);
	}

	@Override
	public Reading saveReadingView(Authentication auth, @NotNull ReadingView readingView) throws InstantiationError {
		assertFieldNotNull(readingView.getPatientId(), "patientId");
		assertFieldNotNull(readingView.getSystolic(), "systolic");
		assertFieldNotNull(readingView.getDiastolic(), "diastolic");
		assertFieldNotNull(readingView.getHeartRate(), "heartRate");
		assertFieldNotNull(readingView.isPregnant(), "pregnant");
		if (readingView.isPregnant()) {
			assertFieldNotNull(readingView.getGestationalAge(), "gestationalAge");
		}
		assertFieldNotNull(readingView.getTimestamp(), "timestamp");
		assertFieldNotNull(readingView.getSymptoms(), "symptoms");
		return readingView;
	}

	@Override
	public List<ReadingView> getAllCreatedBy(int userId) {
		return new ArrayList<>();
	}
}
