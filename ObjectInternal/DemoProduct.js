//-----------------------------------------------
// Server side script for product business object
//-----------------------------------------------

DemoProduct.increaseStock = function() {
	// Increase stock
	var s = this.getField("demoPrdStock");
	s.setValue(s.getInt(0) + 10);
	this.save();
	// Log
	console.log("Stock for " + this.getFieldValue("demoPrdReference") + " is now " + s.getValue());
	// Information message
	return Message.formatSimpleInfo("DEMO_PRD_STOCK_INCREASED");
};

DemoProduct.decreaseStock = function() {
	// Decrease stock
	var q = this.getIntParameter("QUANTITY", 0);
	var s = this.getField("demoPrdStock");
	s.setValue(s.getInt() - q);
	this.save();
	// Log
	console.log("Stock for " + this.getFieldValue("demoPrdReference") + " is now " + s.getValue());
	// Information message
	return Message.formatSimpleInfo("DEMO_PRD_STOCK_DECREASED:" + q);
};

// Custom short label
DemoProduct.getUserKeyLabel = function(row) {
	return row ? row[this.getFieldIndex("demoPrdReference")] : this.getFieldValue("demoPrdReference");
};

DemoProduct.canReference = function(objectName, fieldName) {
	// Hide history records on tree view
	return !this.isTreeviewInstance() || objectName != "DemoProductHistoric";
};

// Publication
DemoProduct.catalog = function(pt) {
	var d = new DocxTool();
	d.newDocument();
	d.addStyledParagraph(DocxTool.STYLE_TITLE, this.getFieldValue("demoPrdName") + " (" + this.getFieldValue("demoPrdReference") + ")");
	d.addParagraph(this.getFieldValue("demoPrdDescription"));
	d.addHTML(this.getFieldValue("demoPrdDocumentation"));
	return d.toByteArray();
};