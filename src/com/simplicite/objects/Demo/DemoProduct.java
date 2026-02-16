package com.simplicite.objects.Demo;

import java.util.Map;

import com.simplicite.util.Action;
import com.simplicite.util.AppLog;
import com.simplicite.util.Message;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.ObjectField;
import com.simplicite.util.PrintTemplate;
import com.simplicite.util.Tool;
import com.simplicite.util.annotations.BusinessObjectAction;
import com.simplicite.util.annotations.BusinessObjectPublication;
import com.simplicite.util.tools.BarcodeTool;
import com.simplicite.util.tools.DocxTool;
import com.simplicite.util.tools.HTTPTool;
import com.simplicite.util.tools.MailTool;

/**
 * Product business object
 */
public class DemoProduct extends ObjectDB {
	private static final long serialVersionUID = 1L;

	/** Default increment */
	public static final int DEFAULT_INCREMENT = 10;

	public static final String NAME_FIELDNAME = "demoPrdName";
	public static final String REFERENCE_FIELDNAME = "demoPrdReference";
	public static final String DESCRIPTION_FIELDNAME = "demoPrdDescription";
	public static final String DOCUMENTATION_FIELDNAME = "demoPrdDocumentation";
	public static final String STOCK_FIELDNAME = "demoPrdStock";
	public static final String INCREMENT_FIELDNAME = "demoPrdIncrement";

	/** Init default increment */
	@Override
	public void initAction(Action action) {
		if ("DEMO_INCSTOCK".equals(action.getName())) {
			ObjectField f = action.getConfirmField(getGrant().getLang(), INCREMENT_FIELDNAME);
			if (f != null) {
				String def = String.valueOf(DEFAULT_INCREMENT);
				f.setDefaultValue(def);
			}
		}
	}

	/** Action: increase stock */
	@BusinessObjectAction
	public String increaseStock(Map<String, String> params) {
		try {
			int inc = Tool.parseInt(params.get(INCREMENT_FIELDNAME), DEFAULT_INCREMENT);
			if (inc > 0) {
				ObjectField s = getField(STOCK_FIELDNAME);
				s.setValue(s.getInt(0) + inc);
				getTool().validateAndSave();
				// Log
				AppLog.log("DEMO_INFO", getClass(), "increaseStock", "Stock for " + getFieldValue(REFERENCE_FIELDNAME) + " is now " + s.getValue(), getGrant());
				// User message
				return Message.formatSimpleInfo("DEMO_PRD_STOCK_INCREASED", s.getValue());
			} else {
				return Message.formatSimpleError("DEMO_PRD_ERR_INCREMENT", String.valueOf(inc));
			}
		} catch (Exception e) {
			return Message.formatSimpleError(e);
		}
	}

	/** Action: decrease stock */
	@BusinessObjectAction
	public String decreaseStock() {
		try {
			int dec = getIntParameter("QUANTITY", 0);
			if (dec > 0) {
				ObjectField s = getField(STOCK_FIELDNAME);
				s.setValue(s.getInt() - dec);
				getTool().validateAndSave();
				// Log
				AppLog.log("DEMO_INFO", getClass(), "decreaseStock", "Stock for " + getFieldValue(REFERENCE_FIELDNAME) + " is now " + s.getValue(), getGrant());
				// User message
				return Message.formatSimpleInfo("DEMO_PRD_STOCK_DECREASED", s.getValue());
			} else {
				return Message.formatSimpleError("DEMO_PRD_ERR_DECREMENT", String.valueOf(dec));
			}
		} catch (Exception e) {
			return Message.formatSimpleError(e);
		}
	}

	/** Action method: send product data in an email */
	@BusinessObjectAction
	public String sendEmail(Action a) {
		try {
			MailTool mt = new MailTool(getGrant());
			mt.addRcpt(getGrant().getEmail());
			String ref = getFieldValue(REFERENCE_FIELDNAME);
			mt.setSubject(getName() + " " + ref);
			mt.addAttach(this, getField("demoPrdBrochure"));
			String picCid = mt.addImage(this, getField("demoPrdPicture"));
			mt.setBody(
				"<h1>" + Tool.toHTML(ref) + "</h1>" +
				"<img src=\"cid:" + picCid +  "\"/>" +
				"<h3>" + Tool.toHTML(getFieldValue("demoPrdName")) + "</h3>" +
				"<h5>" + Tool.toHTML(getFieldValue("demoPrdDescription")) + "</h5>" +
				"<div>" + getFieldValue("demoPrdDocumentation") + "</div>"
			);
			mt.send();
			return Message.formatSimpleInfo("OK");
		} catch (Exception e) {
			AppLog.error(null, e, getGrant());
			return Message.formatSimpleError(e.getMessage());
		}
	}

	/** Publication: Microsoft Word(R) brochure */
	@BusinessObjectPublication
	public Object printBrochure(PrintTemplate pt) {
		try {
			String ref = getFieldValue(REFERENCE_FIELDNAME);
			pt.setFilename(ref + ".docx");

			// Build a Docx document from scratch:
			/* DocxTool dt = new DocxTool();
			dt.newDocument();
			dt.addStyledParagraph(DocxTool.STYLE_TITLE, getFieldValue(NAME_FIELDNAME) + " (" + ref + ")");
			dt.addParagraph(getFieldValue(DESCRIPTION_FIELDNAME));
			dt.addHTML(getFieldValue(DOCUMENTATION_FIELDNAME));
			return dt.toByteArray(); */

			// Build by filling a Docx template:
			DocxTool dt = new DocxTool(pt.getDocument(getGrant()).getFile());
			dt.replace("productName", getFieldValue(NAME_FIELDNAME));
			dt.replace("productReference", ref);
			dt.replace("productDescription", getFieldValue(DESCRIPTION_FIELDNAME));
			dt.addHTML(getFieldValue(DOCUMENTATION_FIELDNAME));
			return dt.toByteArray();
		} catch (Exception e) { // Unexpected error => text file with error message
			AppLog.error("Unable to publish " + pt.getName(), e, getGrant());
			pt.setMIMEType(HTTPTool.MIME_TYPE_TXT);
			pt.setFilename(getGrant().T("ERROR") + ".txt");
			return e.getMessage();
		}
	}

	@Override
	public String preSave() {
		// Manage the EAN13 image
		ObjectField ean13 = getField("demoPrdEan13");
		ObjectField ean13Img = getField("demoPrdEan13Image");
		if (ean13.isEmpty())
			ean13Img.setValue(""); // Remove image if EAN13 is empty
		else if (isNew() || ean13.hasChanged())
			ean13Img.setDocument(this, ean13.getValue() + ".png", BarcodeTool.ean13Image(ean13.getValue())); // (Re)generate image if ENA13 has changed
		return null;
	}

	/** Hook override: custom short label */
	@Override
	public String getUserKeyLabel(String[] row) {
		return getFieldValue(REFERENCE_FIELDNAME, row);
	}

	/** Hook override: hide history records on tree view */
	@Override
	public boolean canReference(String objName, String fkFieldName) {
		return !isTreeviewInstance() || "DemoProductHistoric".equals(objName);
	}
}
