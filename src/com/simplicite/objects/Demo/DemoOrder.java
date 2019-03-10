package com.simplicite.objects.Demo;

import java.util.*;
import com.simplicite.util.*;
import com.simplicite.util.tools.*;

/**
 * Order
 */
public class DemoOrder extends ObjectDB {
	private static final long serialVersionUID = 1L;
	
	/*
	DemoOrder.postValidate = function() {
		// Order quantity checking
		if (this.getField("demoOrdQuantity").getInt(0) <= 0) {
			console.error("Order quantity <0 for order " + this.getField("demoOrdNumber").getValue());
			return Message.formatError("ERR_DEMO_ORD_QUANTITY", null, "demoOrdQuantity");
		} else if (this.getStatus() == "D" && this.getField("demoOrdPrdId.demoPrdStock").getInt(0) - this.getField("demoOrdQuantity").getInt(0) <= 0) {
			console.error("Zero stock on " + this.getField("demoOrdPrdId.demoPrdReference").getValue());
			return Message.formatSimpleError("ERR_DEMO_PRD_STOCK");
		}
		// Set order unit price only at creation
		if (this.isNew())
			this.getField("demoOrdUnitPrice").setValue(this.getField("demoOrdPrdId.demoPrdUnitPrice").getValue());
	};
	
	DemoOrder.postUpdate = function() {
		// Invitation + stock decrease on shipment
		if (this.getOldStatus() == "V" && this.getStatus() == "D") {
			try {
				var n = this.getFieldValue("demoOrdNumber");
				var d = Tool.fromDateTime(this.getFieldValue("demoOrdDeliveryDate"));
				var name = this.getFieldValue("demoOrdCliId.demoCliFirstname") + " " + this.getFieldValue("demoOrdCliId.demoCliLastname");
				var desc = "Hello " + name + ". Your order " + n + " delivery is scheduled";
				new Mail(this.getGrant()).sendInvitation(
					d, Tool.shiftSeconds(d, 2*3600),
					this.getFieldValue("demoOrdCliId.demoCliAddress1") + " " + this.getFieldValue("demoOrdCliId.demoCliAddress2") + " " + this.getFieldValue("demoOrdCliId.demoCliAddress3")
						+ this.getFieldValue("demoOrdCliId.demoCliZipCode") + this.getFieldValue("demoOrdCliId.demoCliCity"),
					"demo@simplicite.fr", "Simplicité",
					this.getFieldValue("demoOrdCliId.demoCliEmail"), name,
					"Order " + n + " delivery schedule",
					desc, desc);
			} catch (e) {
				console.error("Error sending invitation: " + e.getMessage());
			}
	
			var prd = this.getGrant().getTmpObject("DemoProduct");
			prd.select(this.getField("demoOrdPrdId").getValue());
			var q = this.getField("demoOrdQuantity").getInt(0);
			prd.setParameter("QUANTITY", q);
			prd.invokeAction("DEMO_DECSTOCK");
			prd.removeParameter("QUANTITY");
			console.info("Stock decreased by " + q + " on " + this.getField("demoOrdPrdId.demoPrdReference").getValue());
			return Message.formatSimpleInfo("DEMO_PRD_STOCK_DECREASED");
		}
	};
	
	DemoOrder.postSave = function() {
		// The Demo.isLowStock function is defined in the DemoCommon shared script
		if (Demo.isLowStock(this.getGrant(), this.getField("demoOrdPrdId.demoPrdStock").getInt())) {
			// Notify responsible user if stock is low
			try {
				new Mail(this.getGrant()).send(
						"demo@simplicite.fr",
						"demo@simplicite.fr",
						"Low stock on " + this.getField("demoOrdPrdId.demoPrdReference").getValue(),
						"<html><body>" +
						"<h3>Hello,</h3>" +
						"<p>The stock is low for product <b>" + this.getField("demoOrdPrdId.demoPrdReference").getValue() + "</b> " +
						"(" + this.getField("demoOrdPrdId.demoPrdStock").getValue() + ")<br/>Please order new ones !</p>" +
						"</body></html>");
			} catch (e) {
				console.error("Error sending low stock email: " + e.getMessage());
			}
	
			// Show warning to current user
			return Message.formatSimpleWarning("ERR_DEMO_PRD_LOWSTOCK");
		}
	};
	
	// Custom short label
	DemoOrder.getUserKeyLabel = function(row) {
		return this.getGrant().T("DEMO_ORDER_NUMBER") + (row ? row[this.getFieldIndex("demoOrdNumber")] : this.getFieldValue("demoOrdNumber"));
	};
	
	DemoOrder.canReference = function(objectName, fieldName) {
		// Hide history records on tree view
		return !this.isTreeviewInstance() || objectName != "DemoOrderHistoric";
	};
	
	// -----------------------------
	// Custom PDF publication
	// -----------------------------
	
	// Call to publication method (the printOrderReceipt function is defined in the DemoCommon shared script)
	DemoOrder.printReceipt = function(pt) {
		return Demo.orderReceipt(this);
	};
	
	// Allow custom publication only if status is validated or shipped
	DemoOrder.isPrintTemplateEnable = function(row, printtemplate) {
		var s = row !== null ? row[this.getStatusIndex()] : this.getStatus();
		if (printtemplate == "DemoOrder-PDF")
			return s == "V" || s == "D";
	};
	*/
}
