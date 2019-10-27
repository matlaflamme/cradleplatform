package com.cradlerest.web.model;

import com.cradlerest.web.model.view.SymptomView;
import com.cradlerest.web.util.datagen.annotations.Omit;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Symptom entity contains information about a single symptom.
 *
 * New symptoms are not meant to be created at runtime. Instead, services should
 * reference the pre-existing set of symptoms which already exist in the database.
 */
@Entity
@Table(name = "symptom")
@Omit
public class Symptom implements SymptomView {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "text")
	private String text;

	public Symptom() {
	}

	public Symptom(Integer id, String text) {
		this.id = id;
		this.text = text;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Computes and returns a hash code for this object.
	 *
	 * We use the symptom's primary key ({@code id}) as, under normal
	 * circumstances, two {@code Symptom} objects must be equal if their ids are
	 * the same. Note that the {@code equals} method does not make this
	 * assumption, but it is fine for this method to.
	 *
	 * {@code id} is asserted to be not {@code null} as it is the primary key
	 * for this table.
	 *
	 * @return A hash code.
	 */
	@Override
	public int hashCode() {
		assert id != null;

		return id.hashCode();
	}

	/**
	 * Checks for equivalence against {@code obj}.
	 *
	 * Both {@code id} and {@code text} are asserted to be not {@code null} to
	 * conform to the database schema.
	 *
	 * @param obj The object to compare to.
	 * @return {@code true} is {@code this} is equal to {@code obj}.
	 */
	@Override
	public boolean equals(@Nullable Object obj) {
		assert text != null;

		var validComp = obj != null
				&& obj.hashCode() == hashCode()
				&& obj instanceof Symptom;
		var other = (Symptom) obj;
		return validComp && id.equals(other.id) && text.equals(other.text);
	}
}
