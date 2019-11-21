package com.cradlerest.web.service;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.UserDetailsImpl;
import com.cradlerest.web.model.builder.ReadingViewBuilder;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.service.repository.ReadingRepository;
import com.cradlerest.web.service.repository.SymptomReadingRelationRepository;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

import static com.cradlerest.web.util.CopyFields.copyFields;

public class ReadingManagerImpl implements ReadingManager {

	private ReadingRepository readingRepository;
	private SymptomReadingRelationRepository symptomReadingRelationRepository;
	private SymptomManager symptomManager;

	public ReadingManagerImpl(
			ReadingRepository readingRepository,
			SymptomReadingRelationRepository symptomReadingRelationRepository,
			SymptomManager symptomManager
	) {
		this.readingRepository = readingRepository;
		this.symptomReadingRelationRepository = symptomReadingRelationRepository;
		this.symptomManager = symptomManager;
	}

	@Override
	public ReadingView getReadingView(@NotNull Integer readingId) throws EntityNotFoundException {
		var optionalReading = readingRepository.findById(readingId);
		if (optionalReading.isEmpty()) {
			throw new EntityNotFoundException("unable to find reading with id: " + readingId);
		}

		var reading = optionalReading.get();
		return convertToReadingView(reading);
	}

	@Override
	public List<ReadingView> getAllReadingViewsForPatient(@NotNull String patientId) throws EntityNotFoundException {
		var readings = readingRepository.findAllByPatientId(patientId);
		var readingViews = new ArrayList<ReadingView>();
		for (var reading : readings) {
			readingViews.add(getReadingView(reading.getId()));
		}
		return readingViews;
	}

	@Override
	public List<ReadingView> getAllCreatedBy(int userId) {
		var readings = readingRepository.findAllByCreatedBy(userId);
		return Vec.copy(readings)
				.map(this::convertToReadingView)
				.toList();
	}

	@Override
	public Reading saveReadingView(@Nullable Authentication auth, @NotNull ReadingView readingView) throws Exception {
		if (auth == null) {
			// TODO: change to AccessDeniedException once that is merged
			throw new Exception("Permission Denied");
		}

		if (readingView.getCreatedBy() == null) {
			assert auth.getPrincipal() instanceof UserDetailsImpl;
			var details = (UserDetailsImpl) auth.getPrincipal();
			readingView.setCreatedBy(details.getId());
		}

		// use copyFields to extract the reading and persist it
		var reading = new Reading();
		copyFields(readingView, reading);
		var persistedReading = readingRepository.save(reading);

		// persist all symptoms as well
		for (var symptomView : readingView.getSymptoms()) {
			symptomManager.relateReadingWithSymptom(reading.getId(), symptomView.getText());
		}

		return persistedReading;
	}

	private ReadingView convertToReadingView(@NotNull Reading reading) {
		assert reading.getId() != null;
		var symptoms = symptomReadingRelationRepository.getSymptomsForReading(reading.getId());
		return new ReadingViewBuilder()
				.reading(reading)
				.symptoms(symptoms)
				.build();
	}
}
