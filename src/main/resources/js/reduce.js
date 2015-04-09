
function ( key, values ) {
	var reducedObject = {
		cellid: key.cellid,
		lac: key.lac,
		avgLatitude: 0.0,
		avgLongitude: 0.0,
		totalLatitude: 0.0,
		totalLongitude: 0.0,
		counts: 0
	};
	
	values.forEach( function(value) {
		reducedObject.totalLatitude += value.totalLatitude;
		reducedObject.totalLongitude += value.totalLongitude;
		reducedObject.counts += value.counts;
	});
	
	return reducedObject;
}
