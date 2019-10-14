package com.cradlerest.web.util.datagen;

import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import com.cradlerest.web.util.datagen.error.DeadlockException;
import com.cradlerest.web.util.datagen.error.DuplicateItemException;
import com.cradlerest.web.util.datagen.error.MissingAnnotationException;
import com.cradlerest.web.util.datagen.mock.*;
import com.github.maumay.jflow.vec.Vec;
import org.junit.Test;

import javax.persistence.Column;
import javax.persistence.Entity;

import static org.assertj.core.api.Assertions.assertThat;

public class GenerateDummyDataTests {

	@Test
	public void referencesOf_WhenClassHasReferences_ReturnListOfReferences() {
		assertThat(GenerateDummyData.referencesOf(MockEntityB.class))
				.isEqualTo(Vec.of(MockEntityA.class, MockEntityC.class));
	}

	@Test
	public void referencesOf_WhenClassHasNoReferences_ReturnEmptyList() {
		assertThat(GenerateDummyData.referencesOf(MockEntityA.class))
				.isEmpty();
	}

	@Test(expected = MissingAnnotationException.class)
	public void referencesOf_WhenClassReferencesNonEntityClass_ThrowException() {
		@Entity
		class MockEntity {
			@Column(name = "x")
			@ForeignKey(Object.class)
			private Integer x;
		}

		GenerateDummyData.referencesOf(MockEntity.class);
	}

	// This will cause issues elsewhere but it is not this function's job to detect this
	@Test
	public void referencesOf_WhenClassReferencesItself_ReturnListOfReferences() {
		@Entity
		class MockEntity {
			@Column(name = "x")
			@ForeignKey(MockEntity.class)
			private Integer x;
		}

		assertThat(GenerateDummyData.referencesOf(MockEntity.class))
				.isEqualTo(Vec.of(MockEntity.class));
	}

	@Test
	public void linearize_WithNoCircularReferences_OrderClasses() {
		var classes = Vec.of(MockEntityC.class, MockEntityB.class, MockEntityA.class);

		assertThat(GenerateDummyData.linearize(classes))
				.isEqualTo(Vec.of(MockEntityA.class, MockEntityC.class, MockEntityB.class));
	}

	@Test
	public void linearize_WhenPassedEmptyList_ReturnEmptyList() {
		assertThat(GenerateDummyData.linearize(Vec.of()))
				.isEmpty();
	}

	@Test(expected = DeadlockException.class)
	public void linearize_WhenPassedClassWithoutItsDependencies_ThrowDeadlockException() {
		var classes = Vec.of(MockEntityB.class, MockEntityC.class);

		GenerateDummyData.linearize(classes);
	}

	@Test(expected = DeadlockException.class)
	public void linearize_WhenPassedClassesWithCircularReferences_ThrowDeadlockException() {
		var classes = Vec.of(MockEntityD.class, MockEntityE.class);

		GenerateDummyData.linearize(classes);
	}

	@Test(expected = DeadlockException.class)
	public void linearize_WhenAClassReferencesItself_ThrowDeadlockException() {
		@Entity
		class MockEntity {
			@Column(name = "x")
			@ForeignKey(MockEntity.class)
			private Integer x;
		}

		GenerateDummyData.linearize(Vec.of(MockEntity.class));
	}

	@Test(expected = DuplicateItemException.class)
	public void linearize_WhenPassedListWithDuplicates_ThrowDuplicateItemException() {
		var classes = Vec.of(MockEntityA.class, MockEntityC.class, MockEntityA.class);

		GenerateDummyData.linearize(classes);
	}
}
