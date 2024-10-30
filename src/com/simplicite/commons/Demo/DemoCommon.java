package com.simplicite.commons.Demo;

import java.util.List;

import com.simplicite.util.AppLog;
import com.simplicite.util.Grant;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.Tool;

/**
 * Demo commons.
 * Note that this class is a <b>singleton</b> to avoid static methods
 */
public class DemoCommon implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	// Hidden constructor
	private DemoCommon() {
	}

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
}
