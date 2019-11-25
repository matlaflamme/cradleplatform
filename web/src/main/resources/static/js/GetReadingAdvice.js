/**
 * generates an "advice" object containing strings for the advice according to:
 * https://d1b10bmlvqabco.cloudfront.net/attach/jzli7yk348i4l5/jcjx7qvtk3138u/k0ew5jkq9s32/CRADLE_VSA_Support_App__RetestAlg.pdf
 * This information should be stored in the (a?) database or somewhere
 * @param digit Colour key
 * @returns advice object containing attributes {light, analysis, summary, condition, details}
 */
export function getReadingAdvice(digit){
	let pdf_tail = '_advice.jpg'
	let advice = {};
	switch (digit) {
		case 0:
			advice.light = 'green';
				advice.analysis = 'Patient is likely healthy';
				advice.summary = 'Continue normal care';
				advice.condition = 'BP <140 systolic and <90 diastolic and shock index<0.9';
				advice.details = "Patient is likely to be health.\n" +
									"Continue with normal care";
			break;
		case 1:
			advice.light = 'yellow_up';
				advice.analysis = 'Raised BP';
				advice.summary = 'Monitor for preclampsia.\n Transfer to health centre within 24hr';
				advice.condition = 'Systolic BP 140-159 and or diastolic 90-109';
				advice.details = "This is raised BP this patient may have preeclampsia. Action is needed.\n" +
									"Manage as you would normally e.g. measure urine dipstick, check for sign \n" +
									"and symptoms (e.g. headaches, visual disturbance) and act accordingly.\n" +
									"If in the community transfer when practical (preferrably within 24 hours)";
			break;
		case 2:
			advice.light = 'yellow_down';
				advice.analysis = 'Low BP';
				advice.summary = 'Common, but assess for infection, bleeding, anaemia, and dehydration';
				advice.condition = 'Shock index (HR/Systolic BP) 0.9-1.7';
				advice.details = 'This result can be common in pregnant women, however; It may indicate that' +
									' the mother is developing an infection or bleeding. The patient needs to be ' +
									'assessed to decide what action is required. \nIf she is well (no bleeding, no ' +
									'signs of infection, feels well) she may have anaemia, dehydration, an irregular' +
									' heart rythm or endocrine disease or her blood pressure may be low in pregnancy.' +
									' Consider undertaking routine checks for these when possible. \nIf she is unwell e.g.' +
									' vaginal bleeding, fever, discharge, constant abdominal pain or if she feels unwell' +
									' e.g. feverish, pale, sweaty, breathless:\n' +
										'- Resuscitate as necessary e.g. keep warm, elevate legs.\n' +
										'- Transfer urgently (preferably within 4 hours).\n' +
										'- If bleeding, uterine massage after delivery of placenta, control of ' +
										'bleeding e.g. misoprostol, oxytocin, depending on what\'s available.\n' +
										'- If sepsis, consider starting antibiotics if available.';
			break;
		case 3:
			advice.light = 'red_up';
				advice.analysis = 'Very raised BP';
				advice.summary = 'Urgent action needed.\n Transfer to health centre within 4hrs. Monitor baby.';
				advice.condition = 'Systolic >= 160 and or diastolic >= 110';
				advice.details = 'This is very raised BP and indicates urgent action is needed.\n' +
									'Manage as you would normally e.g. measure urine dipstick, check for signs' +
									' and symptoms and act accordingly. \nGive antihypertensives if available e.g. ' +
									'methyldopa, nifedipine, labetolol.\nIf in the community transfer as soon as ' +
									'possible (preferably within 4 hours).\nMonitor the baby.\nIf BP remains uncontrolled' +
									' and gestation appropriate, seek senior advice regarding need to deliver.';
			break;
		case 4:
			advice.light = 'red_down';
				advice.analysis = 'In severe shock';
				advice.summary = 'Urgent action needed. Get help and assess mother.\n Immediate transfer to health centre (within 1 hour)';
				advice.condition = 'In severe shock';
				advice.details = '- Stay calm. DO NOT leave the woman alone.\n' +
								'- Get HELP\n' +
								'- Assess the mother\n' +
								'- Is she pale, sweaty, cold, breathing fast, drowsy or confused?\n' +
								'- is she unwell e.g. vaginal bleeding, fever, discharge, constant pain?\n' +
								'Treatment:\n' +
								'- Keep her warm and elevate legs if possible\n' +
								'- Organize immediate transfer (within 1 hour)\n' +
								'- If bleeding, uterine massage after delivery of placenta, give medication to contract' +
								' uterus if available e.g. misoprostil 600mcg orally.\n' +
								'- If sepsis, consider starting antibiotics';
			break;
		default:
			return null;
	}
	//advice.details = '/img/' + advice.light + pdf_tail;
	return advice;
}