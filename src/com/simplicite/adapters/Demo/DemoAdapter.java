package com.simplicite.adapters.Demo;

import org.json.JSONObject;

import com.simplicite.util.AppLog;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.exceptions.GetException;
import com.simplicite.util.exceptions.PlatformException;
import com.simplicite.util.tools.BusinessObjectTool;

/**
 * Custom CSV (TSV here) adapter (using the demo objects)
 */
public class DemoAdapter extends com.simplicite.util.integration.CSVLineBasedAdapter {
	private static final long serialVersionUID = 1L;

	private boolean debug = false;

	private ObjectDB sup;
	private transient BusinessObjectTool supt;

	private ObjectDB prd;
	private transient BusinessObjectTool prdt;

	private int nbErrors = 0;

	private static final int NB_COLS = 3; // Expected number of columns

	@Override
	public String preProcess() {
		AppLog.info("Start of " + getName(), getGrant());

		setSeparator('\t'); // Tab is the separator

		sup = getGrant().getObject("demo_adapter", "DemoSupplier");
		supt = new BusinessObjectTool(sup); // or sup.getTool(); in versions 5.0+

		prd = getGrant().getObject("demo_adapter", "DemoProduct");
		prdt = new BusinessObjectTool(prd); // or prd.getTool();

		debug = getBooleanParameter("debug", debug); // Enable debug mode with "debug=true"

		return super.preProcess();
	}

	@Override
	public String processValues(long n, String[] values) {
		try {
			if (n == 1)
				return null; // First line with column headers is ignored
			if (debug) appendLog("Processing line " + n + " = " + String.join(String.valueOf(getSeparator()), values));

			// Line format: <supplier code><tab><product reference><tab><product name>

			if (values.length != NB_COLS)
				throw new PlatformException("Line " + n + " has " + values.length + " columns instead of " + NB_COLS + ", ignored");

			// Get supplier row ID from supplier code code
			String supId;
			try {
				supId = supt.get(new JSONObject().put("demoSupCode", values[0]));
				if (debug) appendLog("Supplier " + values[0] + " found, row ID = " + supId);
			} catch (@SuppressWarnings("unused") GetException e) {
				throw new PlatformException("No supplier found for " + values[0]);
			}

			// Product upsert (= create or update)
			boolean exists = prdt.getForUpsert(
				new JSONObject()
					.put("demoPrdSupId", supId)
					.put("demoPrdReference", values[1]));
			if (!exists) {
				prd.setFieldValue("demoSupId", supId);
				prd.setFieldValue("demoPrdReference", values[1]);
			}
			prd.setFieldValue("demoPrdName", values[2]);
			prdt.validateAndSave();
			if (debug) appendLog("Product " + values[1] + " " + (exists ? "updated" : "created"));
		} catch (PlatformException e) {
			String msg = "Line " + n + " error: " + e.getMessage();
			appendLog(msg);
			AppLog.error(msg, e, getGrant());
			appendError(values); // Rejected line
			nbErrors++;
		}

		nbLines++;
		return null; // No XML processing
	}

	@Override
	public void postProcess() {
		super.postProcess();

		appendLog("=================================================================");
		appendLog("Nb processed lines = " + nbLines);
		appendLog("Nb errors = " + nbErrors);
		appendLog("=================================================================");

		AppLog.info("End of " + getName(), getGrant());
	}
}