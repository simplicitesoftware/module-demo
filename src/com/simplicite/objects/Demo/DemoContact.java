package com.simplicite.objects.Demo;

import com.simplicite.util.ObjectDB;

/**
 * Contact business object
 */
public class DemoContact extends ObjectDB {
	private static final long serialVersionUID = 1L;

	/* TODO
	DemoContact.printExcel = function(pt) {
		// Build rows from selected IDs or from current filters
		var rows = new ArrayList();
		var ids = this.getSelectedIds();
		if (ids && ids.size()>0) {
			for (var k = 0; k < ids.size(); k++)
				if (this.select(ids.get(k)))
					rows.add(this.getValues());
		} else {
			rows = this.search(false);
		}

		var xls = new ExcelPOITool(true); // true = XLSX format
		var sh = xls.newSheet("Contacts");
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
	*/
}
