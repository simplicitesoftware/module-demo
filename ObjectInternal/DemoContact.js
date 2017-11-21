//-----------------------------------------------
// Server side script for contact business object
//-----------------------------------------------

DemoContact.printExcel = function(pt) {
	importPackage(Packages.org.apache.poi.ss.usermodel);
	
	/*var doc = pt.getDocument(this.getGrant());
	var xls = new ExcelPOITool(this.getGrant(), doc);
	var wb = xls.getWorkbook();
	var sh = wb.getSheetAt(0);
	var r = sh.getRow(0);
	var c = r.getCell(0);
	c.setCellValue(this.getCount() + " contacts");*/
	
	var xls = new ExcelPOITool();
	var sh = xls.newSheet("Test");
	var rows = this.search(false);
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
