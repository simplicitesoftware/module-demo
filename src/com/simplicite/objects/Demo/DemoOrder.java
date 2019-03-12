package com.simplicite.objects.Demo;

import java.util.List;

import com.simplicite.commons.Demo.DemoCommon;
import com.simplicite.util.Message;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.PrintTemplate;

/**
 * Order business object
 */
public class DemoOrder extends ObjectDB {
	private static final long serialVersionUID = 1L;

	@Override
	public List<String> postValidate() {
		List<String> msgs = super.postValidate();
		// Order quantity checking
		if (getField("demoOrdQuantity").getInt(0) <= 0) {
			console.error("Order quantity <0 for order " + getField("demoOrdNumber").getValue());
			msgs.add(Message.formatError("ERR_DEMO_ORD_QUANTITY", null, "demoOrdQuantity"));
		}
		// Quantity checking
		if ("D".equals(getStatus()) && getField("demoOrdPrdId.demoPrdStock").getInt(0) - getField("demoOrdQuantity").getInt(0) <= 0) {
			console.error("Zero stock on " + getField("demoOrdPrdId.demoPrdReference").getValue());
			msgs.add(Message.formatSimpleError("ERR_DEMO_PRD_STOCK"));
		}
		// Set order unit price only at creation
		if (isNew())
			getField("demoOrdUnitPrice").setValue(getField("demoOrdPrdId.demoPrdUnitPrice").getValue());
		return msgs;
	}

	/* TODO
	DemoOrder.postUpdate = function() {
		// Invitation + stock decrease on shipment
		if (getOldStatus() == "V" && getStatus() == "D") {
			try {
				var n = getFieldValue("demoOrdNumber");
				var d = Tool.fromDateTime(getFieldValue("demoOrdDeliveryDate"));
				var name = getFieldValue("demoOrdCliId.demoCliFirstname") + " " + getFieldValue("demoOrdCliId.demoCliLastname");
				var desc = "Hello " + name + ". Your order " + n + " delivery is scheduled";
				new Mail(getGrant()).sendInvitation(
					d, Tool.shiftSeconds(d, 2*3600),
					getFieldValue("demoOrdCliId.demoCliAddress1") + " " + getFieldValue("demoOrdCliId.demoCliAddress2") + " " + getFieldValue("demoOrdCliId.demoCliAddress3")
						+ getFieldValue("demoOrdCliId.demoCliZipCode") + getFieldValue("demoOrdCliId.demoCliCity"),
					"demo@simplicite.fr", "Simplicité",
					getFieldValue("demoOrdCliId.demoCliEmail"), name,
					"Order " + n + " delivery schedule",
					desc, desc);
			} catch (e) {
				console.error("Error sending invitation: " + e.getMessage());
			}

			var prd = getGrant().getTmpObject("DemoProduct");
			prd.select(getField("demoOrdPrdId").getValue());
			var q = getField("demoOrdQuantity").getInt(0);
			prd.setParameter("QUANTITY", q);
			prd.invokeAction("DEMO_DECSTOCK");
			prd.removeParameter("QUANTITY");
			console.info("Stock decreased by " + q + " on " + getField("demoOrdPrdId.demoPrdReference").getValue());
			return Message.formatSimpleInfo("DEMO_PRD_STOCK_DECREASED");
		}
	};

	DemoOrder.postSave = function() {
		// The Demo.isLowStock function is defined in the DemoCommon shared script
		if (Demo.isLowStock(getGrant(), getField("demoOrdPrdId.demoPrdStock").getInt())) {
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
			} catch (e) {
				console.error("Error sending low stock email: " + e.getMessage());
			}

			// Show warning to current user
			return Message.formatSimpleWarning("ERR_DEMO_PRD_LOWSTOCK");
		}
	};
	*/

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
		return DemoCommon.orderReceipt(this); // Implemented in common class
	};

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
