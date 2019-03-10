package com.simplicite.commons.Demo;

import java.util.*;
import com.simplicite.util.*;
import com.simplicite.util.tools.*;

/**
 * Demo commons
 */
public class DemoCommon implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	/*
	var Demo = {
	
	// -----------------------------
	// Simple echo method
	// -----------------------------
	echo: function(str) {
		console.log(Tool.getCurrentDatetime() + ": " + str);
	},
	
	// -----------------------------
	// Example of a advanced business rule example:
	// check whether stock is considered to be low in regards to previous sales
	// -----------------------------
	isLowStock: function(grant, stock) {
		// Get work instance for DemoOrder object
		var ord = grant.getTmpObject("DemoOrder");
	
		// Set search filters (same product and on the last N days)
		ord.resetFilters();
		ord.getField("demoOrdPrdId").setFilter(ord.getField("demoOrdPrdId").getValue());
		var checkPeriod = grant.getIntParameter("DEMO_PRD_LOWSTOCK_PERIOD", 90);
		ord.getField("demoOrdDate").setFilterDateMin(Tool.shiftDays(Tool.getCurrentDate(), -checkPeriod));
	
		// Search
		var rows = ord.search();
		console.info(rows.size() + " orders found");
		
		// Iterate over search result to calculate total ordered quantity
		var quantity = 0;
		var quantityIndex = ord.getFieldIndex("demoOrdQuantity");
		for (var i = 0; i < rows.size(); i++) {
			var row = rows.get(i);
			quantity += parseInt(row[quantityIndex], 10);
		}
		console.info("Total ordered quantity = " + quantity);
		
		// Stock is considered low if less than X% of total ordered quantity
		console.info("Current stock = " + stock);
		var threshold = Math.round((grant.getIntParameter("DEMO_PRD_LOWSTOCK_THRESHOLD", 10) / 100) * quantity);
		console.info("Low stock threshold " + threshold);
		var lowStock = stock < threshold;
		console.info("Low stock " + lowStock);
		return lowStock;
	},
	
	//-----------------------------
	// Order receipt publication as PDF
	//-----------------------------
	orderReceipt: function(ord) {
		importPackage(Packages.com.lowagie.text);
	
		var bos = new java.io.ByteArrayOutputStream();
		var pdf = PDFTool.open(bos);
		var c, t, f, v, i;
	
		// Logo
		pdf.add(PDFTool.getImageFromResource(ord.getGrant(), "DEMO_LOGO"));
	
		pdf.add(new Paragraph(ord.getGrant().T("DEMO_RECEIPT"), PDFTool.TITLE1));
	
		f = ord.getField("demoOrdNumber");
		pdf.add(new Paragraph(f.getDisplay() + ": " + f.getValue(), PDFTool.TITLE2));
		f = ord.getField("demoOrdDate");
		pdf.add(new Paragraph(f.getDisplay() + ": " + ord.getGrant().toFormattedDate(f.getValue())));
		f = ord.getField("demoOrdDeliveryDate");
		pdf.add(new Paragraph(f.getDisplay() + ": " + ord.getGrant().toFormattedDatetime(f.getValue())));
	
		t = PDFTool.getTable(2, false);
		t.addCell(PDFTool.getHeaderCell(ord.getField("demoOrdCliId").getDisplay(), java.awt.Color.LIGHT_GRAY));
		t.addCell(PDFTool.getHeaderCell(ord.getField("demoOrdPrdId").getDisplay(), java.awt.Color.LIGHT_GRAY));
	
		c = PDFTool.getCell(null);
		f = ord.getField("demoOrdCliId.demoCliCode");
		c.addElement(new Paragraph(f.getDisplay() + ": " + f.getValue()));
		f = ord.getField("demoOrdCliId.demoCliFirstname");
		c.addElement(new Paragraph(f.getDisplay() + ": " + f.getValue()));
		f = ord.getField("demoOrdCliId.demoCliLastname");
		c.addElement(new Paragraph(f.getDisplay() + ": " + f.getValue()));
	
		c.addElement(new Paragraph("\n"));
	
		f = ord.getField("demoOrdCliId.demoCliAddress1");
		c.addElement(new Paragraph(f.getDisplay() + ": " + f.getValue()));
		f = ord.getField("demoOrdCliId.demoCliAddress2");
		c.addElement(new Paragraph(f.getDisplay() + ": " + f.getValue()));
		f = ord.getField("demoOrdCliId.demoCliAddress3");
		c.addElement(new Paragraph(f.getDisplay() + ": " + f.getValue()));
		f = ord.getField("demoOrdCliId.demoCliZipCode");
		c.addElement(new Paragraph(f.getDisplay() + ": " + f.getValue()));
		f = ord.getField("demoOrdCliId.demoCliCountry");
		c.addElement(new Paragraph(f.getDisplay() + ": " + f.getValue()));
		t.addCell(c);
	
		c = PDFTool.getCell(null);
		f = ord.getField("demoOrdPrdId.demoPrdSupId.demoSupName");
		c.addElement(new Paragraph(f.getDisplay() + ": " + f.getValue()));
		v = ord.getField("demoOrdPrdId.demoPrdSupId.demoSupLogo").getValue();
		if (v !== "") {
			i = PDFTool.getImageFromDBDoc(ord.getGrant(), v);
			i.scaleAbsoluteWidth(100);
			i.setSpacingBefore(10);
			i.setSpacingAfter(10);
			c.addElement(i);
		}
		f = ord.getField("demoOrdPrdId.demoPrdReference");
		c.addElement(new Paragraph(f.getDisplay() + ": " + f.getValue()));
		f = ord.getField("demoOrdPrdId.demoPrdName");
		c.addElement(new Paragraph(f.getDisplay() + ": " + f.getValue()));
		v = ord.getField("demoOrdPrdId.demoPrdPicture").getValue();
		if (v !== "") {
			i = PDFTool.getImageFromDBDoc(ord.getGrant(), v);
			i.scaleAbsoluteWidth(150);
			i.setSpacingBefore(10);
			i.setSpacingAfter(10);
			c.addElement(i);
		}
		t.addCell(c);
	
		pdf.add(t);
	
		f = ord.getField("demoOrdQuantity");
		pdf.add(new Paragraph(f.getDisplay() + ": " + f.getValue(), PDFTool.TITLE1));
		f = ord.getField("demoOrdUnitPrice");
		pdf.add(new Paragraph(f.getDisplay() + ": " + ord.getGrant().toFormattedFloat(f.getValue(), 10, 2) + " Euros", PDFTool.TITLE2));
		f = ord.getField("demoOrdTotal");
		pdf.add(new Paragraph(f.getDisplay() + ": " + ord.getGrant().toFormattedFloat(f.getValue(), 10, 2) + " Euros", PDFTool.TITLE1));
		f = ord.getField("demoOrdVAT");
		pdf.add(new Paragraph(f.getDisplay() + " (" + ord.getGrant().toFormattedFloat(ord.getGrant().getParameter("DEMO_VAT"), 10, 2) + "%): " + ord.getGrant().toFormattedFloat(f.getValue(), 10, 2) + " Euros", PDFTool.TITLE2));
	
		PDFTool.close(pdf);
		return bos.toByteArray();
	}
	
	};
	*/
}
