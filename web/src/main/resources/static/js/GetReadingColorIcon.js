export function getReadingColorIcon(digit){
	let light = 'white';
	switch (digit) {
		case null:
			light = 'white';
			break;
		case 0:
			light = 'green';
			break;
		case 1:
			light = 'yellow_up';
			break;
		case 2:
			light = 'yellow_down';
			break;
		case 3:
			light = 'red_up';
			break;
		case 4:
			light = 'red_down';
			break;
	}
	return "/img/" + light + ".png";
}