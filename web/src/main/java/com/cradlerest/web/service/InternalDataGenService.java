package com.cradlerest.web.service;

import com.cradlerest.web.service.repository.RawDatabaseAccessRepository;
import com.cradlerest.web.util.datagen.Data;
import com.cradlerest.web.util.datagen.GenerateDummyData;
import com.cradlerest.web.util.datagen.impl.UniformNoise;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import static com.cradlerest.web.util.Algorithm.removeDuplicates;

/**
 * Service wrapping around the data generation system to dynamically rebuild
 * the database with dummy data.
 */
public class InternalDataGenService {

	@Autowired
	private RawDatabaseAccessRepository repo;

	/**
	 * Deletes all existing data in the database and populates it with new,
	 * automatically generated dummy data.
	 * @param baseAmount The base amount of data to generate.
	 */
	public void generateDummyData(int baseAmount) {
		var data = GenerateDummyData.generate(new UniformNoise(), baseAmount);

		// clear the current contents of the database
		deleteStatements(data).forEach(this::executeUpdate);

		// insert the generated data
		data.map(Data::toSqlStatement).forEach(this::executeUpdate);
	}

	private static String deleteAllFromTableStatement(@NotNull String tableName) {
		return String.format("DELETE FROM %s WHERE 1 = 1;", tableName);
	}

	private static Vec<String> deleteStatements(@NotNull Vec<Data> data) {
		var tableNames = removeDuplicates(data.map(Data::getTable));
		return tableNames.map(InternalDataGenService::deleteAllFromTableStatement);
	}

	private void executeUpdate(@NotNull String query) {
		System.out.printf("%s\n\n", query);
		repo.update(query);
	}
}
