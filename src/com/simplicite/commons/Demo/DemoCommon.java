package com.simplicite.commons.Demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.simplicite.util.AppLog;
import com.simplicite.util.Globals;
import com.simplicite.util.Grant;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.ObjectField;
import com.simplicite.util.Tool;
import com.simplicite.util.tools.PDFTool;

/**
 * Demo commons.
 * Note that this class is a <b>singleton</b> to avoid static methods
 */
public class DemoCommon implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private DemoCommon() {} // Hidden constructor

	/** Singleton */
	private final static DemoCommon INSTANCE = new DemoCommon();

	/** Get singleton */
	public static DemoCommon getInstance() {
		return INSTANCE;
	}

	/**
	 * Check whether stock is low
	 * @param grant Grant
	 * @param prdId Product row ID
	 * @param stock Current stock for product
	 */
	public boolean isLowStock(Grant grant, String prdId, int stock) {
		// Get work instance for DemoOrder object
		ObjectDB ord = grant.getTmpObject("DemoOrder");

		// Set search filters (on the last N days)
		ord.resetFilters();
		ord.getField("demoOrdPrdId").setFilter(prdId);
		int checkPeriod = grant.getIntParameter("DEMO_PRD_LOWSTOCK_PERIOD", 90);
		ord.getField("demoOrdDate").setFilterDateMin(Tool.shiftDays(Tool.getCurrentDate(), -checkPeriod));

		// Search
		List<String[]> rows = ord.search();
		AppLog.info(rows.size() + " orders found", grant);

		// Iterate over search result to calculate total ordered quantity
		int quantity = 0;
		int quantityIndex = ord.getFieldIndex("demoOrdQuantity");
		for (String[] row : rows)
			quantity += Tool.parseInt(row[quantityIndex], 10);
		AppLog.info("Total ordered quantity = " + quantity, grant);

		// Stock is considered low if less than X% of total ordered quantity
		AppLog.info("Current stock = " + stock, grant);
		long threshold = Math.round(((double)grant.getIntParameter("DEMO_PRD_LOWSTOCK_THRESHOLD", 10) / 100) * quantity);
		AppLog.info("Low stock threshold " + threshold, grant);
		boolean lowStock = stock < threshold;
		AppLog.info("Low stock " + lowStock, grant);

		return lowStock;
	}

	private static final String CURRENCY = " Euros";

	/**
	 * Order receipt publication as PDF
	 * @param ord Order object
	 */
	public byte[] orderReceipt(ObjectDB ord) throws IOException, DocumentException {
		try (ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream()) {
			Document pdf = PDFTool.open(bos);

			ObjectField f = ord.getField("demoOrdNumber");
			String receipt = ord.getGrant().T("DEMO_RECEIPT");
			String title = ord.getDisplay() + " " + f.getDisplay().toLowerCase() + " " + f.getValue();

			// Properties
			pdf.addTitle(title);
			pdf.addSubject(receipt + ": " + title);
			pdf.addAuthor(Globals.getPlatformName() + " / " + ord.getModuleName());
			pdf.addCreator(Globals.getPlatformVendor());
			pdf.addKeywords(receipt.toLowerCase() + " " + ord.getDisplay().toLowerCase());
			
			// Logo
			pdf.add(PDFTool.getImageFromResource(ord.getGrant(), "DEMO_PRINT_LOGO"));

			pdf.add(new Paragraph(receipt, PDFTool.TITLE1));

			pdf.add(new Paragraph(f.getDisplay() + ": " + f.getValue(), PDFTool.TITLE2));
			f = ord.getField("demoOrdDate");
			pdf.add(new Paragraph(f.getDisplay() + ": " + ord.getGrant().toFormattedDate(f.getValue())));
			f = ord.getField("demoOrdDeliveryDate");
			pdf.add(new Paragraph(f.getDisplay() + ": " + ord.getGrant().toFormattedDatetime(f.getValue())));

			PdfPTable t = PDFTool.getTable(2, false);
			t.addCell(PDFTool.getHeaderCell(ord.getField("demoOrdCliId").getDisplay(), java.awt.Color.LIGHT_GRAY));
			t.addCell(PDFTool.getHeaderCell(ord.getField("demoOrdPrdId").getDisplay(), java.awt.Color.LIGHT_GRAY));

			PdfPCell c = PDFTool.getCell(null);
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
			f = ord.getField("demoOrdCliId.demoCliZipCode");
			c.addElement(new Paragraph(f.getDisplay() + ": " + f.getValue()));
			f = ord.getField("demoOrdCliId.demoCliCountry");
			c.addElement(new Paragraph(f.getDisplay() + ": " + f.getValue()));
			t.addCell(c);

			c = PDFTool.getCell(null);
			f = ord.getField("demoOrdPrdId.demoPrdSupId.demoSupName");
			c.addElement(new Paragraph(f.getDisplay() + ": " + f.getValue()));
			String v = ord.getField("demoOrdPrdId.demoPrdSupId.demoSupLogo").getValue();
			if (!Tool.isEmpty(v)) {
				Image i = PDFTool.getImageFromDBDoc(ord.getGrant(), v);
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
			if (!Tool.isEmpty(v)) {
				Image i = PDFTool.getImageFromDBDoc(ord.getGrant(), v);
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
			pdf.add(new Paragraph(f.getDisplay() + ": " + ord.getGrant().toFormattedFloat(f.getValue(), 10, 2) + CURRENCY, PDFTool.TITLE2));
			f = ord.getField("demoOrdTotal");
			pdf.add(new Paragraph(f.getDisplay() + ": " + ord.getGrant().toFormattedFloat(f.getValue(), 10, 2) + CURRENCY, PDFTool.TITLE1));
			f = ord.getField("demoOrdVAT");
			pdf.add(new Paragraph(f.getDisplay() + " (" + ord.getGrant().toFormattedFloat(ord.getGrant().getParameter("DEMO_VAT"), 10, 2) + "%): " + ord.getGrant().toFormattedFloat(f.getValue(), 10, 2) + CURRENCY, PDFTool.TITLE2));

			PDFTool.close(pdf);
			return bos.toByteArray();
		}
	}
}
