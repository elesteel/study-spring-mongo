
function( key, reduceValue ) {
	if( reduceValue.counts > 0 ) {
		reduceValue.avgLatitude = reduceValue.totalLatitude / reduceValue.counts;
		reduceValue.avgLongitude = reduceValue.totalLongitude / reduceValue.counts;
	}
	return reduceValue;
}