package com.cradlerest.web.service;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.builder.ReadingViewBuilder;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.service.repository.ReadingRepository;
import com.cradlerest.web.service.repository.SymptomReadingRelationRepository;
import org.jetbrains.annotations.NotNull;

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
		var symptoms = symptomReadingRelationRepository.getSymptomsForReading(readingId);
		return new ReadingViewBuilder()
				.reading(reading)
				.symptoms(symptoms)
				.build();
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
	public void saveReadingView(@NotNull ReadingView readingView) throws EntityNotFoundException {
		// use copyFields to extract the reading and persist it
		var reading = new Reading();
		copyFields(readingView, reading);
		readingRepository.save(reading);

		// persist all symptoms as well
		for (var symptomView : readingView.getSymptoms()) {
			symptomManager.relateReadingWithSymptom(readingView.getId(), symptomView.getText());
		}
	}
}
