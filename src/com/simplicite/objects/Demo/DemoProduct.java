package com.simplicite.objects.Demo;

import com.simplicite.util.AppLog;
import com.simplicite.util.Message;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.ObjectField;
import com.simplicite.util.PrintTemplate;
import com.simplicite.util.tools.DocxTool;

/**
 * Product business object
 */
public class DemoProduct extends ObjectDB {
	private static final long serialVersionUID = 1L;

	/** Action: increase stock */
	public String increaseStock() {
		ObjectField s = getField("demoPrdStock");
		s.setValue(s.getInt(0) + 10);
		save();
		// Log
		AppLog.info(getClass(), "increaseStock", "Stock for " + getFieldValue("demoPrdReference") + " is now " + s.getValue(), getGrant());
		// User message
		return Message.formatSimpleInfo("DEMO_PRD_STOCK_INCREASED");
	}

	/** Action: decrease stock */
	public String decreaseStock() {
		// Decrease stock
		int q = getIntParameter("QUANTITY", 0);
		ObjectField s = getField("demoPrdStock");
		s.setValue(s.getInt() - q);
		save();
		// Log
		AppLog.info(getClass(), "decreaseStock", "Stock for " + getFieldValue("demoPrdReference") + " is now " + s.getValue(), getGrant());
		// User message
		return Message.formatSimpleInfo("DEMO_PRD_STOCK_DECREASED:" + q);
	}

	/** Publication: Microsoft Word(R) catalog */
	public Object catalog(PrintTemplate pt) {
		try {
			DocxTool d = new DocxTool();
			d.newDocument();
			d.addStyledParagraph(DocxTool.STYLE_TITLE, getFieldValue("demoPrdName") + " (" + getFieldValue("demoPrdReference") + ")");
			d.addParagraph(getFieldValue("demoPrdDescription"));
			d.addHTML(getFieldValue("demoPrdDocumentation"));
			return d.toByteArray();
		} catch (Exception e) {
			AppLog.error(getClass(), "catalog", "Unable to publish catalog", e, getGrant());
			return null;
		}
	}

	/** Hook override: custom short label */
	@Override
	public String getUserKeyLabel(String[] row) {
		return row!=null ? row[getFieldIndex("demoPrdReference")] : getFieldValue("demoPrdReference");
	}

	/** Hook override: hide history records on tree view */
	@Override
	public boolean canReference(String objName, String fkFieldName) {
		return !isTreeviewInstance() || "DemoProductHistoric".equals(objName);
	}
}
