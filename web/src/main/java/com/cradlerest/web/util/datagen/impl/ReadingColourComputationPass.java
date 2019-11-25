package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.model.ReadingColour;
import com.cradlerest.web.util.datagen.Data;
import com.cradlerest.web.util.datagen.DataPass;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;

/**
 * Computes the traffic light for each reading.
 */
public class ReadingColourComputationPass implements DataPass {

	@Override
	public @NotNull Vec<Data> traverse(@NotNull Vec<Data> data) {
		for (var d : data) {
			var systolic = (Integer) d.getValueForColumn("systolic");
			var diastolic = (Integer) d.getValueForColumn("diastolic");
			var heartRate = (Integer) d.getValueForColumn("heart_rate");

			assert systolic != null;
			assert diastolic != null;
			assert heartRate != null;

			var colour = ReadingColour.computeColour(systolic, diastolic, heartRate);
			d.setValueForColumn("colour", colour.ordinal());
		}
		return data;
	}
}
