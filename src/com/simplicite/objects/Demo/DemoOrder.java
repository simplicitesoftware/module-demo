package com.simplicite.objects.Demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.simplicite.commons.Demo.DemoCommon;
import com.simplicite.util.AppLog;
import com.simplicite.util.Mail;
import com.simplicite.util.Message;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.PrintTemplate;
import com.simplicite.util.Tool;

/**
 * Order business object
 */
public class DemoOrder extends ObjectDB {
	private static final long serialVersionUID = 1L;

	@Override
	public List<String> postValidate() {
		List<String> msgs = new ArrayList<>();
		// Order quantity checking
		if (getField("demoOrdQuantity").getInt(0) <= 0) {
			AppLog.error(getClass(), "postValidate", "Order quantity <0 for order " + getField("demoOrdNumber").getValue(), null, getGrant());
			msgs.add(Message.formatError("ERR_DEMO_ORD_QUANTITY", null, "demoOrdQuantity"));
		}
		// Quantity checking
		if ("D".equals(getStatus()) && getField("demoOrdPrdId.demoPrdStock").getInt(0) - getField("demoOrdQuantity").getInt(0) <= 0) {
			AppLog.error(getClass(), "postValidate", "Zero stock on " + getField("demoOrdPrdId.demoPrdReference").getValue(), null, getGrant());
			msgs.add(Message.formatSimpleError("ERR_DEMO_PRD_STOCK"));
		}
		// Set order unit price only at creation
		if (isNew())
			getField("demoOrdUnitPrice").setValue(getField("demoOrdPrdId.demoPrdUnitPrice").getValue());
		return msgs;
	}

	/** Hook override: invitation for delivery + stock decrease on shipment */
	@Override
	public String postUpdate() {
		if ("V".equals(getOldStatus()) && "D".equals(getStatus())) { // Upon state transition to delivered
			try {
				String n = getFieldValue("demoOrdNumber");
				Date d = Tool.fromDateTime(getFieldValue("demoOrdDeliveryDate"));
				String name = getFieldValue("demoOrdCliId.demoCliFirstname") + " " + getFieldValue("demoOrdCliId.demoCliLastname");
				String desc = "Hello " + name + ". Your order " + n + " delivery is scheduled";
				new Mail(getGrant()).sendInvitation(
					d, Tool.shiftSeconds(d, 2*3600),
					getFieldValue("demoOrdCliId.demoCliAddress1") + " " + getFieldValue("demoOrdCliId.demoCliAddress2")
						+ getFieldValue("demoOrdCliId.demoCliZipCode") + getFieldValue("demoOrdCliId.demoCliCity"),
					"demo@simplicite.fr", "Simplicité",
					getFieldValue("demoOrdCliId.demoCliEmail"), name,
					"Order " + n + " delivery schedule",
					desc, desc);
			} catch (Exception e) {
				AppLog.warning(getClass(), "postUpdate", "Error sending invitation", e, getGrant());
			}

			try {
				ObjectDB prd = getGrant().getTmpObject("DemoProduct");
				prd.select(getField("demoOrdPrdId").getValue());
				int q = getField("demoOrdQuantity").getInt(0);
				prd.setParameter("QUANTITY", q);
				prd.invokeAction("DEMO_DECSTOCK");
				prd.removeParameter("QUANTITY");
				// Log
				AppLog.info(getClass(), "postUpdate", "Stock decreased by " + q + " on " + getField("demoOrdPrdId.demoPrdReference").getValue(), getGrant());
				// User message
				return Message.formatSimpleInfo("DEMO_PRD_STOCK_DECREASED");
			} catch (Exception e) {
				String msg = "Error decreasing stock: " + e.getMessage();
				// Log
				AppLog.error(getClass(), "postUpdate", msg, e, getGrant());
				// User message
				return Message.formatSimpleError(msg);
			}
		}
		return super.postUpdate();
	}

	/** Hook override: check low stock */
	@Override
	public String postSave() {
		if (DemoCommon.isLowStock(getGrant(), getFieldValue("demoOrdPrdId"), getField("demoOrdPrdId.demoPrdStock").getInt(0))) {
			// Notify responsible user if stock is low
			try {
				new Mail(getGrant()).send(
					"demo@simplicite.fr",
					"demo@simplicite.fr",
					"Low stock on " + getField("demoOrdPrdId.demoPrdReference").getValue(),
					"<html><body>" +
					"<h3>Hello,</h3>" +
					"<p>The stock is low for product <b>" + getField("demoOrdPrdId.demoPrdReference").getValue() + "</b> " +
					"(" + getField("demoOrdPrdId.demoPrdStock").getValue() + ")<br/>Please order new ones !</p>" +
					"</body></html>");
			} catch (Exception e) {
				AppLog.warning(getClass(), "postSave", "Error sending low stock alert email", e, getGrant());
			}

			// User message
			return Message.formatSimpleWarning("ERR_DEMO_PRD_LOWSTOCK");
		}
		return super.postSave();
	}

	/** Hook override: custom short label */
	@Override
	public String getUserKeyLabel(String[] row) {
		return getGrant().T("DEMO_ORDER_NUMBER") + (row!=null ? row[getFieldIndex("demoOrdNumber")] : getFieldValue("demoOrdNumber"));
	}

	/** Hook override: hide history records on tree view */
	@Override
	public boolean canReference(String objName, String fkFieldName) {
		return !isTreeviewInstance() || "DemoOrderHistoric".equals(objName);
	}

	/** Publication: PDF receipt */
	public Object printReceipt(PrintTemplate pt) {
		try {
			return DemoCommon.orderReceipt(this); // Implemented in common class
		} catch (Exception e) {
			AppLog.error(getClass(), "printReceipt", "Unable to publish " + pt.getName(), e, getGrant());
			return e.getMessage();
		}
	}

	/** Hook override: Allow custom publication only if status is validated or shipped */
	@Override
	public boolean isPrintTemplateEnable(String[] row, String ptName) {
		if ("DemoOrder-PDF".equals(ptName)) {
			String s = row!=null ? row[getStatusIndex()] : getStatus();
			return "V".equals(s) || "D".equals(s);
		}
		return super.isPrintTemplateEnable(row, ptName);
	}
}
