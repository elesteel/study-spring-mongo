function() {
	var key = {
			cellid: this.cellid,
			lac: this.lac
	}
	var value = {
		cellid: this.cellid,
		lac: this.lac,
		avgLatitude: 0.0,
		avgLongitude: 0.0,
		totalLatitude: this.latitude,
		totalLongitude: this.longitude,
		counts: 1
	}
	emit( key, value);
}