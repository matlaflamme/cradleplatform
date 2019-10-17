package com.cradlerest.web.util.datagen;

import com.cradlerest.web.util.datagen.annotations.DataGenNullChance;
import com.cradlerest.web.util.datagen.annotations.DataGenRange;
import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import com.cradlerest.web.util.datagen.annotations.Omit;
import com.cradlerest.web.util.datagen.error.ForeignKeyException;
import com.cradlerest.web.util.datagen.error.NoDefinedGeneratorException;
import com.cradlerest.web.util.datagen.impl.ForeignKeyRepositoryImpl;
import com.cradlerest.web.util.datagen.mock.MockEntityA;
import com.cradlerest.web.util.datagen.mock.MockEntityC;
import com.cradlerest.web.util.datagen.mock.MockGenerator;
import com.cradlerest.web.util.datagen.mock.MockNoise;
import com.github.maumay.jflow.utils.Tup;
import com.github.maumay.jflow.vec.Vec;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DataFactoryTests {

	@Entity
	@Table(name = "x")
	private static class SimpleMockEntity {
		@Id
		@Column(name = "id", nullable = false)
		private Integer id;
	}

	@Entity
	@Table(name = "x")
	private static class ParameterizedMockEntity {
		@Id
		@Column(name = "id", nullable = false)
		@DataGenRange(min = 10, max = 20)
		private Integer id;
	}

	@Entity
	@Table(name = "x")
	private static class ForeignKeyMockEntity {

		@Id
		@Omit
		private Integer id;

		@Column(name = "aid", nullable = false)
		@ForeignKey(MockEntityA.class)
		private Integer aid;
	}

	@Entity
	@Table(name = "x")
	private static class NonTrivialFieldMockEntity {

		@Id
		@Omit
		private Integer id;

		@Column(name = "obj")
		private Double obj;
	}

	@Entity
	@Table(name = "x")
	private static class EnumeratedMockEntity {
		public enum MockEnum {
			A, B, C
		}

		@Id
		@Omit
		private Integer id;

		@Column(name = "enum", nullable = false)
		private MockEnum e;
	}

	@Entity
	@Table(name = "x")
	private static class NullableMockEntity {

		@Id
		@Omit
		private Integer id;

		@Column(name = "a")
		@DataGenNullChance(0.5)
		private Integer a;
	}

	@Mock
	ForeignKeyRepository mockForeignKeyRepository;

	@Mock
	MockNoise mockNoise = new MockNoise();

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Test
	public void expectedBehaviour_SimpleFieldGeneration() {
		var noise = new MockNoise();
		var intGenerator = new MockGenerator<>(134);
		var factory = new DataFactory(noise, new ForeignKeyRepositoryImpl());
		factory.registerGenerator(Integer.class, intGenerator);

		var data = factory.prepare(SimpleMockEntity.class).take(1).get();
		assertThat(data.getTable())
				.isEqualTo("x");
		assertThat(data.getColumnValueMap())
				.containsExactly(Map.entry("id", intGenerator.getValue()));

		assertThat(intGenerator.getCurriedParameters())
				.isEmpty();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void expectedBehaviour_ParameterizedEntityGeneration() {
		var noise = new MockNoise();
		var intGenerator = new MockGenerator<>(15);
		var factory = new DataFactory(noise, new ForeignKeyRepositoryImpl());
		factory.registerGenerator(Integer.class, intGenerator);

		var data = factory.prepare(ParameterizedMockEntity.class).take(1).get();
		assertThat(data.getTable())
				.isEqualTo("x");
		assertThat(data.getColumnValueMap())
				.containsExactly(Map.entry("id", intGenerator.getValue()));

		assertThat(intGenerator.getCurriedParameters())
				.containsExactly(Tup.of("min", 10), Tup.of("max", 20));
	}

	@Test
	public void expectedBehaviour_ForeignKeyGeneration() {
		Mockito.when(mockForeignKeyRepository.get(MockEntityA.class))
				.thenReturn(Vec.of(100));

		var noise = new MockNoise();
		var factory = new DataFactory(noise, mockForeignKeyRepository);

		var data = factory.prepare(ForeignKeyMockEntity.class).take(1).get();
		assertThat(data.getColumnValueMap())
				.containsExactly(Map.entry("aid", 100));
	}

	@Test
	public void expectedBehaviour_RetrieveForeignKeyFromRepository() {
		var noise = new MockNoise();
		var factory = new DataFactory(noise, new ForeignKeyRepositoryImpl());

		var intGenerator = new MockGenerator<>(100);
		factory.registerGenerator(Integer.class, intGenerator);

		var dataA = factory.prepare(MockEntityA.class).take(1).get();
		assertThat(dataA.getColumnValueMap())
				.containsExactly(Map.entry("id", 100));

		intGenerator.setValue(101);
		var dataC = factory.prepare(MockEntityC.class).take(1).get();
		assertThat(dataC.getColumnValueMap())
				.containsExactly(Map.entry("id", 101), Map.entry("aid", 100));
	}

	@Test
	public void expectedBehaviour_EnumFieldGeneration() {
		var noise = new MockNoise();
		var factory = new DataFactory(noise, new ForeignKeyRepositoryImpl());

		var data = factory.prepare(EnumeratedMockEntity.class).take(1).get();
		assertThat(data.getColumnValueMap())
				.containsExactly(Map.entry("enum", EnumeratedMockEntity.MockEnum.A));
	}

	@Test
	public void expectedBehaviour_NullableFields() {
		var factory = new DataFactory(mockNoise, new ForeignKeyRepositoryImpl());
		var intGenerator = new MockGenerator<>(200);
		factory.registerGenerator(Integer.class, intGenerator);

		// non null case
		Mockito.when(mockNoise.generateBool(0.5))
				.thenReturn(false);
		var nonNullData = factory.prepare(NullableMockEntity.class).take(1).get();
		assertThat(nonNullData.getColumnValueMap())
				.containsExactly(Map.entry("a", intGenerator.getValue()));

		// null case
		Mockito.when(mockNoise.generateBool(0.5))
				.thenReturn(true);
		var nullData = factory.prepare(NullableMockEntity.class).take(1).get();
		assertThat(nullData.getColumnValueMap())
				.containsExactly(Map.entry("a", DataModel.NULL_VALUE));
	}

	@Test(expected = ForeignKeyException.class)
	public void whenUnableToFindForeignKeyValue_ThrowForeignKeyException() {
		var noise = new MockNoise();
		var factory = new DataFactory(noise, new ForeignKeyRepositoryImpl());

		factory.prepare(MockEntityC.class).take(1).get();
	}

	@Test(expected = NoDefinedGeneratorException.class)
	public void whenUnableToDetermineGenerator_ThrowNoDefinedGeneratorException() {
		var noise = new MockNoise();
		var factory = new DataFactory(noise, new ForeignKeyRepositoryImpl());

		factory.prepare(NonTrivialFieldMockEntity.class).take(1).get();
	}
}
