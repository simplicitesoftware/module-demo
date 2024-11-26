package com.simplicite.tests.Demo;

import com.simplicite.util.AppLog;
import com.simplicite.util.Grant;
import com.simplicite.util.Globals;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.ObjectField;
import com.simplicite.util.Tool;
import com.simplicite.util.exceptions.DBException;

import org.junit.Test;

/**
 * Random data generation
 */
public class DemoRandomDataGeneration {
	@Test
	public void generateData() {
		generateOrders(Tool.parseInt(System.getenv("DEMO_NB_ORDERS"), 0));
	}

	protected void generateOrders(int n) {
		if (n <= 0)
			return;

		Grant g = Grant.getSystemAdmin();
		ObjectDB ord = null;
		try {
			ord = g.getIsolatedObject("DemoOrder");

			for (int i = 0; i < n; i++) {
				ord.resetValues();
				ord.setFieldValue("demoOrdDate", getRandomDateInLastNDays(90));
				ord.setFieldValue("demoOrdStatus", getRandomStatus(ord.getStatusField()));
				ord.setFieldValue("demoOrdCliId", getRandomRowId("demo_client"));
				ord.setFieldValue("demoOrdPrdId", getRandomRowId("demo_product"));
				ord.setFieldValue("demoOrdQuantity", Tool.randomInt(1, 5));
				ord.setFieldValue("demoOrdComments", Tool.getCurrentDateTime());
				ord.getTool().validateAndSave();
			}
		} catch (Exception e) {
			AppLog.error(e.getMessage(), e, g);
		} finally {
			if (ord != null)
				ord.destroy();
		}
	}

	private static String getRandomStatus(ObjectField f) {
		String[] codes = f.getList().getCodes(true);
		return codes[Tool.randomInt(0, codes.length - 1)];
	}

	private static String getRandomDateInLastNDays(int n) {
		return Tool.shiftDays(Tool.getCurrentDate(), Tool.randomInt(-n, 0));
	}

	private static String getRandomRowId(String tableName) throws DBException {
		return Grant.getSystemAdmin().simpleQuery("SELECT row_id FROM " + tableName + " ORDER BY " + getSqlRand() + " LIMIT 1");
	}

	private static String getSqlRand() throws DBException {
		int dbv = Grant.getSystemAdmin().getDBVendor();
		if (dbv == Globals.DB_HSQLDB || dbv == Globals.DB_MYSQL)
			return "RAND()";
		else if (dbv == Globals.DB_POSTGRESQL)
			return "RANDOM()";
		else
			throw new DBException("This function doesn't support this database (only HSQLDB, MySQL/MariaDB, PostgreSQL)");
	}
}
