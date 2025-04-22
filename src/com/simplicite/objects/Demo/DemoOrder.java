package com.simplicite.objects.Demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.simplicite.commons.Demo.DemoCommon;
import com.simplicite.util.AppLog;
import com.simplicite.util.Mail;
import com.simplicite.util.Message;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.Tool;

/**
 * Order business object
 */
public class DemoOrder extends ObjectDB {
	private static final long serialVersionUID = 1L;

	/** Quantity field name */
	private static final String QUANTITY_FIELDNAME = "demoOrdQuantity";
	/** Number field name */
	private static final String NUMBER_FIELDNAME = "demoOrdNumber";
	/** Product field name */
	private static final String PRODUCT_FIELDNAME = "demoOrdPrdId";
	/** Reference field name (from product object) */
	private static final String REFERENCE_FIELDNAME = PRODUCT_FIELDNAME + "." + DemoProduct.REFERENCE_FIELDNAME;
	/** Stock field name (from product object) */
	private static final String STOCK_FIELDNAME = PRODUCT_FIELDNAME + "." + DemoProduct.STOCK_FIELDNAME;

	/** Error message text code for invalid quantity */
	public static final String QUANTITY_ERROR = "ERR_DEMO_ORD_QUANTITY";

	/** Error message text code for low stock */
	public static final String STOCK_ERROR = "ERR_DEMO_PRD_STOCK";

	@Override
	public List<String> postValidate() {
		List<String> msgs = new ArrayList<>();
		// Order quantity checking
		if (getField(QUANTITY_FIELDNAME).getInt(0) <= 0) {
			AppLog.log("DEMO_ERR", getClass(), "postValidate", "Order quantity <0 for order " + getFieldValue(NUMBER_FIELDNAME), null, getGrant());
			msgs.add(Message.formatError(QUANTITY_ERROR, null, QUANTITY_FIELDNAME));
		}
		// Quantity checking
		if ("D".equals(getStatus()) && getField(STOCK_FIELDNAME).getInt(0) - getField(QUANTITY_FIELDNAME).getInt(0) <= 0) {
			AppLog.log("DEMO_ERR", getClass(), "postValidate", "Zero stock on " + getFieldValue(REFERENCE_FIELDNAME), getGrant());
			msgs.add(Message.formatSimpleError(STOCK_ERROR));
		}
		// Set order unit price only at creation
		if (isNew())
			setFieldValue("demoOrdUnitPrice", getFieldValue(PRODUCT_FIELDNAME + ".demoPrdUnitPrice"));
		return msgs;
	}

	@Override
	public String postUpdate() {
		if ("V".equals(getOldStatus()) && "D".equals(getStatus())) { // Upon state transition to delivered
			try {
				// Send invitation
				String n = getFieldValue(NUMBER_FIELDNAME);
				Date d = Tool.fromDateTime(getFieldValue("demoOrdDeliveryDate"));
				String name = getFieldValue("demoOrdCliId.demoCliFirstname") + " " + getFieldValue("demoOrdCliId.demoCliLastname");
				String desc = "Hello " + name + ". Your order " + n + " delivery is scheduled";
				new Mail(getGrant()).sendInvitation(
					d, Tool.shiftSeconds(d, 2 * 3600),
					getFieldValue("demoOrdCliId.demoCliAddress1") + " " + getFieldValue("demoOrdCliId.demoCliAddress2")
						+ getFieldValue("demoOrdCliId.demoCliZipCode") + getFieldValue("demoOrdCliId.demoCliCity"),
					null, null,
					getFieldValue("demoOrdCliId.demoCliEmail"), name,
					"Order " + n + " delivery schedule",
					desc, desc);
			} catch (Exception e) {
				AppLog.warning("Error sending invitation", e, getGrant());
			}

			try {
				// Decrease stock
				ObjectDB prd = getGrant().getTmpObject("DemoProduct");
				prd.select(getFieldValue(PRODUCT_FIELDNAME));
				int q = getField(QUANTITY_FIELDNAME).getInt(0);
				prd.setParameter("QUANTITY", q);
				prd.invokeAction("DEMO_DECSTOCK");
				prd.removeParameter("QUANTITY");
				// Log
				AppLog.info("Stock decreased by " + q + " on " + getFieldValue(REFERENCE_FIELDNAME), getGrant());
				// User message
				return Message.formatSimpleInfo("DEMO_PRD_STOCK_DECREASED");
			} catch (Exception e) {
				String msg = "Error decreasing stock: " + e.getMessage();
				// Log
				AppLog.error(msg, e, getGrant());
				// User message
				return Message.formatSimpleError(msg);
			}
		}
		return super.postUpdate();
	}

	@Override
	public String postSave() {
		// Check stock level
		int stock = getField(STOCK_FIELDNAME).getInt(0);
		if (DemoCommon.getInstance().isLowStock(getGrant(), getFieldValue(PRODUCT_FIELDNAME), stock)) {
			String ref = getFieldValue(REFERENCE_FIELDNAME);

			// Notify responsible user if stock is low
			try {
				String to = getGrant().getEmail();
				if (!Tool.isEmpty(to))
					new Mail(getGrant()).send(to, null,
						"Low stock on " + ref,
						"<html><body>" +
						"<h3>Hello,</h3>" +
						"<p>The stock is low for product <b>" + ref + "</b> " +
						"(" + stock + ")<br/>Please order new ones !</p>" +
						"</body></html>");
			} catch (Exception e) {
				AppLog.warning("Error sending low stock alert email", e, getGrant());
			}

			// Log
			AppLog.log("DEMO_WARN", getClass(), "postSave", "Low stock on " + ref + ": " + stock, getGrant());

			// User message
			return Message.formatSimpleWarning("ERR_DEMO_PRD_LOWSTOCK");
		}
		return super.postSave();
	}

	@Override
	public String getUserKeyLabel(String[] row) {
		// Custom short label
		return getGrant().T("DEMO_ORDER_NUMBER") + getFieldValue(NUMBER_FIELDNAME, row);
	}

	@Override
	public boolean canReference(String objName, String fkFieldName) {
		// Hide history records on tree view
		return !isTreeviewInstance() || "DemoOrderHistoric".equals(objName);
	}

	@Override
	public boolean isPrintTemplateEnable(String[] row, String ptName) {
		// Allow receipt publication only if status is validated or shipped
		if ("DemoOrder-Receipt".equals(ptName)) {
			String s = getStatus(row);
			return "V".equals(s) || "D".equals(s);
		}
		return super.isPrintTemplateEnable(row, ptName);
	}
}
