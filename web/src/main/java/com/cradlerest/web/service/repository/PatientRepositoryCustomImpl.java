package com.cradlerest.web.service.repository;

import com.cradlerest.web.model.PatientWithLatestReadingView;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.ReadingColour;
import com.cradlerest.web.model.Sex;
import com.cradlerest.web.model.builder.PatientBuilder;
import com.cradlerest.web.model.builder.ReadingBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Custom extension component for {@code PatientRepository}.
 *
 * Implementation class is linked with {@code PatientRepository} automatically
 * by Spring.
 */
@SuppressWarnings("unused")
public class PatientRepositoryCustomImpl implements PatientRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<PatientWithLatestReadingView> getAllPatientsAndLatestReadings() {
		var queryResult = entityManager.createNativeQuery("" +
				"SELECT p.id,\n" +
				"       p.name,\n" +
				"       p.village,\n" +
				"       p.birth_year,\n" +
				"       p.sex,\n" +
				"       p.medical_history,\n" +
				"       p.drug_history,\n" +
				"       p.last_updated,\n" +
				"       r.id AS rid,\n" +
				"       r.systolic,\n" +
				"       r.diastolic,\n" +
				"       r.heart_rate,\n" +
				"       r.is_pregnant,\n" +
				"       r.gestational_age,\n" +
				"       r.colour,\n" +
				"       r.other_symptoms,\n" +
				"       r.timestamp\n" +
				"FROM patient p LEFT OUTER JOIN reading r on p.id = r.pid\n" +
				"WHERE r.id IS NULL OR r.id = (SELECT r1.id\n" +
				"              FROM reading r1\n" +
				"              WHERE r1.pid = p.id\n" +
				"                AND r1.timestamp = (SELECT MAX(r2.timestamp)\n" +
				"                                    FROM reading r2\n" +
				"                                    WHERE r2.pid = p.id LIMIT 1)\n" +
				"              LIMIT 1);", Tuple.class)
				.getResultList();

		var resultList = new ArrayList<PatientWithLatestReadingView>();
		for (var result : queryResult) {
			var tuple = (Tuple) result;
			var patient = new PatientBuilder()
					.id(tuple.get("id", String.class))
					.name(tuple.get("name", String.class))
					.villageNumber(tuple.get("village", String.class))
					.birthYear(tuple.get("birth_year", Integer.class))
					.sex(Sex.valueOf(tuple.get("sex", Integer.class)))
					.medicalHistory(tuple.get("medical_history", String.class))
					.drugHistory(tuple.get("drug_history", String.class))
					.lastUpdated(tuple.get("last_updated", Date.class))
					.build();

			var reading = (Reading) null;

			if (tuple.get("rid") != null) {
				reading = new ReadingBuilder()
						.id(tuple.get("rid", Integer.class))
						.pid(tuple.get("id", String.class))
						.systolic(tuple.get("systolic", Integer.class))
						.diastolic(tuple.get("diastolic", Integer.class))
						.heartRate(tuple.get("heart_rate", Integer.class))
						.pregnant(tuple.get("is_pregnant", Boolean.class))
						.gestationalAgeDays(tuple.get("gestational_age", Integer.class))
						.colour(ReadingColour.valueOf(tuple.get("colour", Integer.class)))
						.timestamp(tuple.get("timestamp", Date.class))
						.build();
			}

			resultList.add(new PatientWithLatestReadingView(patient, reading));
		}
		return resultList;
	}
}
