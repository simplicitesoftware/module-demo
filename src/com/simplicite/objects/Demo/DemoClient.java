package com.simplicite.objects.Demo;

import com.simplicite.util.Message;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.ObjectField;

/**
 * Customer
 */
public class DemoClient extends ObjectDB {
	private static final long serialVersionUID = 1L;

	/** Action: increase stock */
	public String increaseStock() {
		ObjectField s = this.getField("demoPrdStock");
		s.setValue(s.getInt(0) + 10);
		this.save();

		// Log
		console.log("Stock for " + this.getFieldValue("demoPrdReference") + " is now " + s.getValue());
		// User message
		return Message.formatSimpleInfo("DEMO_PRD_STOCK_INCREASED");
	}
	/*
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

	// Publication
	DemoProduct.catalog = function(pt) {
		var d = new DocxTool();
		d.newDocument();
		d.addStyledParagraph(DocxTool.STYLE_TITLE, this.getFieldValue("demoPrdName") + " (" + this.getFieldValue("demoPrdReference") + ")");
		d.addParagraph(this.getFieldValue("demoPrdDescription"));
		d.addHTML(this.getFieldValue("demoPrdDocumentation"));
		return d.toByteArray();
	};
	*/

	/** Hook override: custom short label */
	@Override
	public String getUserKeyLabel(String[] row) {
		return row!=null ? row[getFieldIndex("demoPrdReference")] : getFieldValue("demoPrdReference");
	}

	/** Hook override: hide history records on tree view */
	@Override
	public boolean canReference(String objName, String fkFieldName) {
		return !this.isTreeviewInstance() || objName != "DemoProductHistoric";
	}
}
