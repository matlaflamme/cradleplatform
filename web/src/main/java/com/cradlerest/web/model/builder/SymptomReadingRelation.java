package com.cradlerest.web.model.builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * SymptomReadingRelation expresses the relationship between a reading and the
 * symptoms associated with it.
 */
@Entity
@Table(name = "symptom_reading_relation")
public class SymptomReadingRelation {

	@Column(name = "sid")
	private Integer SymptomId;

	@Column(name = "rid")
	private Integer ReadingId;

	public SymptomReadingRelation() {
	}

	public SymptomReadingRelation(Integer symptomId, Integer readingId) {
		SymptomId = symptomId;
		ReadingId = readingId;
	}

	public Integer getSymptomId() {
		return SymptomId;
	}

	public void setSymptomId(Integer symptomId) {
		SymptomId = symptomId;
	}

	public Integer getReadingId() {
		return ReadingId;
	}

	public void setReadingId(Integer readingId) {
		ReadingId = readingId;
	}
}
