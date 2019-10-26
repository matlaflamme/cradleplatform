package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.datagen.Data;
import com.cradlerest.web.util.datagen.DataPass;
import com.github.maumay.jflow.utils.Tup;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class SymptomReadingRelationEnforceUniquePass implements DataPass {

	private Set<Tup<Integer, Integer>> primaryKeyTuples = new HashSet<>();

	@Override
	public @NotNull Vec<Data> traverse(@NotNull Vec<Data> data) {
		// ensure that the set is empty before starting in case this pass is
		// invoked multiple times
		primaryKeyTuples.clear();

		Vec<Data> result = Vec.of();
		for (var x : data) {
			var sid = (Integer) x.getValueForColumn("sid");
			var rid = (Integer) x.getValueForColumn("rid");

			assert sid != null;
			assert rid != null;

			var tup = Tup.of(sid, rid);
			if (primaryKeyTuples.contains(tup)) {
				// skip over duplicate values
				continue;
			}

			primaryKeyTuples.add(tup);
			result = result.append(x);
		}

		return result;
	}
}
