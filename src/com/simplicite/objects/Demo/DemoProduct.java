package com.simplicite.objects.Demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

import com.simplicite.util.AppLog;
import com.simplicite.util.Grant;
import com.simplicite.util.Message;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.ObjectField;
import com.simplicite.util.PrintTemplate;
import com.simplicite.util.Tool;
import com.simplicite.util.tools.DocxTool;
import com.simplicite.util.tools.JUnitTool;

/**
 * Product business object
 */
public class DemoProduct extends ObjectDB {
	private static final long serialVersionUID = 1L;

	/** Action: increase stock */
	public String increaseStock(Map<String, String> params) {
		int q = Tool.parseInt(params.get("demoPrdIncrement"), 0);
		if (q > 0) {
			ObjectField s = getField("demoPrdStock");
			s.setValue(s.getInt(0) + q);
			save();
			// Log
			AppLog.info(getClass(), "increaseStock", "Stock for " + getFieldValue("demoPrdReference") + " is now " + s.getValue(), getGrant());
			// User message
			return Message.formatSimpleInfo("DEMO_PRD_STOCK_INCREASED:" + s.getValue());
		} else
			return Message.formatSimpleError("DEMO_PRD_ERR_INCREMENT:" + q);
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
	public Object printCatalog(PrintTemplate pt) {
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

	/** JUnit test class */
	public static class DemoProductTest {
		/** Decrement test */
		@Test
		public void testDecrement() {
			try {
				ObjectDB prd = Grant.getSystemAdmin().getTmpObject("DemoProduct");
				prd.setValues(prd.search().get(0));
				ObjectField s = prd.getField("demoPrdStock");
				int n = s.getInt(0);
				prd.setParameter("QUANTITY", 10);
				prd.invokeAction("DEMO_DECSTOCK");
				assertEquals(n - 10, s.getInt(0));
			} catch (Exception e) {
				fail(e.getMessage());
			}
		}
	}

	/** Hook override: launch JUnit tests classes */
	@Override
	public String unitTests() {
		JUnitTool t = new JUnitTool(getGrant());
		return
			t.run("com.simplicite.commons.Demo.DemoProductTest") + // Shared code test class
			t.run(DemoProductTest.class); // Nested test class
	}
}
