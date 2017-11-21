DemoClient.preSave = function() {
	var coords = this.getField("demoCliCoords");

	var a1 = this.getField("demoCliAddress1");
	var a2 = this.getField("demoCliAddress2");
	var a3 = this.getField("demoCliAddress3");
	var zc = this.getField("demoCliZipCode");
	var ci = this.getField("demoCliCity");
	var co = this.getField("demoCliCountry");
	
	if (coords.isEmpty() || a1.hasChanged() || a2.hasChanged() || a3.hasChanged() || zc.hasChanged() || ci.hasChanged() || co.hasChanged()) {
		var a = a1.getValue() + (a2.isEmpty() ? "" : ", " + a2.getValue()) + (a3.isEmpty() ? "" : ", " + a3.getValue()) + ", " + zc.getValue() + ", " + ci.getValue() + ", " + co.getValue();
		console.log("Try to geocode " + a);
		var c = GMapTool.geocodeOne(a);
		console.log("Coordinates = " + c);
		if (c)
			coords.setValue(c.toString());
	}
};

DemoClient.getUserKeyLabel = function(row) {
	return row
		? row[this.getFieldIndex("demoCliFirstname")] + " " + row[this.getFieldIndex("demoCliLastname")]
		: this.getFieldValue("demoCliFirstname") + " " + this.getFieldValue("demoCliLastname");
};