package com.cradlerest.web.model.builder;

import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * SymptomReadingRelation expresses the relationship between a reading and the
 * symptoms associated with it.
 */
@Entity
@IdClass(SymptomReadingRelation.IdType.class)
@Table(name = "symptom_reading_relation")
public class SymptomReadingRelation {

	/**
	 * The composite id type for {@code SymptomReadingRelation}.
	 *
	 * ref: https://www.baeldung.com/jpa-composite-primary-keys
	 */
	public static class IdType implements Serializable {
		private Integer symptomId;

		private Integer readingId;

		public IdType() {
		}

		public IdType(Integer symptomId, Integer readingId) {
			this.symptomId = symptomId;
			this.readingId = readingId;
		}

		public Integer getSymptomId() {
			return symptomId;
		}

		public void setSymptomId(Integer symptomId) {
			this.symptomId = symptomId;
		}

		public Integer getReadingId() {
			return readingId;
		}

		public void setReadingId(Integer readingId) {
			this.readingId = readingId;
		}

		@Override
		public int hashCode() {
			return (symptomId << 1) ^ readingId;
		}

		@Override
		public boolean equals(@Nullable Object obj) {
			// this is a composite primary key,
			// neither of these fields should be null
			assert symptomId != null;
			assert readingId != null;

			var validComp = obj != null
					&& obj.hashCode() == hashCode()
					&& obj instanceof IdType;
			if (!validComp) {
				return false;
			}

			var other = (IdType) obj;
			return symptomId.equals(other.symptomId) && readingId.equals(other.readingId);
		}
	}

	@Id
	@Column(name = "sid")
	private Integer symptomId;

	@Id
	@Column(name = "rid")
	private Integer readingId;

	public SymptomReadingRelation() {
	}

	public SymptomReadingRelation(Integer symptomId, Integer readingId) {
		this.symptomId = symptomId;
		this.readingId = readingId;
	}

	public Integer getSymptomId() {
		return symptomId;
	}

	public void setSymptomId(Integer symptomId) {
		this.symptomId = symptomId;
	}

	public Integer getReadingId() {
		return readingId;
	}

	public void setReadingId(Integer readingId) {
		this.readingId = readingId;
	}
}
