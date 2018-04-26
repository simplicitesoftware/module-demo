//-----------------------------------------------
// Server side script for contact business object
//-----------------------------------------------

DemoContact.printExcel = function(pt) {
	importPackage(Packages.org.apache.poi.ss.usermodel);
	
	// Build rows from selected IDs or from current filters
	var rows = new ArrayList();
	var ids = this.getSelectedIds();
	if (ids && ids.size()>0) {
		for (var k = 0; k < ids.size(); k++)
			if (this.select(ids.get(k)))
				rows.add(this.getValues());
	} else
		rows = this.search(false);
	
	var xls = new ExcelPOITool();
	var sh = xls.newSheet("Test");
	for (var i = 0; i < rows.size(); i++) {
		var r = xls.newRow(i);
		var row = rows.get(i);
		for (var j = 0; j < row.length; j++) {
			var c = xls.newCell(j, row[j]);
			r.add(c);
		}
		sh.add(r);
	}
	xls.add(sh);
	
	return xls.generateToByteArray();
};

DemoContact.canReference = function(objectName, fieldName) {
	// Hide history records on tree view
	return !this.isTreeviewInstance() || objectName != "DemoContactHistoric";
};
