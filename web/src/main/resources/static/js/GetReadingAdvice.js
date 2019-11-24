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
			break;
		case 1:
			advice.light = 'yellow_up';
				advice.analysis = 'Raised BP';
				advice.summary = 'Monitor for preclampsia.\n Transfer to health centre within 24hr';
				advice.condition = 'Systolic BP 140-159 and or diastolic 90-109';
			break;
		case 2:
			advice.light = 'yellow_down';
				advice.analysis = 'Patient is likely healthy';
				advice.summary = 'Continue normal care';
				advice.condition = 'Shock index (HR/Systolic BP) 0.9-1.7';
			break;
		case 3:
			advice.light = 'red_up';
				advice.analysis = 'Patient is likely healthy';
				advice.summary = 'Continue normal care';
				advice.condition = 'Systolic >= 160 and or diastolic >= 110';
			break;
		case 4:
			advice.light = 'red_down';
				advice.analysis = 'In severe shock';
				advice.summary = 'Urgent action needed. Get help and assess mother. Immediate transfer to health centre (within 1 hour)';
				advice.condition = 'In severe shock';
			break;
		default:
			return null;
	}
	advice.details = '/img/' + advice.light + pdf_tail;
	return advice;
}