package com.simplicite.extobjects.Demo;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simplicite.util.AppLog;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.tools.Parameters;

public class DemoSupplierStats extends com.simplicite.webapp.web.widgets.ChartjsExternalObject {
	private static final long serialVersionUID = 1L;

	@Override
	public JSONObject data(Parameters params) {
		JSONArray labels = new JSONArray();
		JSONArray counts = new JSONArray();
		JSONArray quantities = new JSONArray();
		JSONArray datasets = new JSONArray()
			.put(new JSONObject().put("label", getGrant().T("DEMO_SUPPLIER_STATS_COUNT")).put("data", counts).put("backgroundColor", COLORS[0]))
			.put(new JSONObject().put("label",  getGrant().T("DEMO_SUPPLIER_STATS_QUANTITIES")).put("data", quantities).put("backgroundColor", COLORS[1]));

		try {
			ObjectDB prd = getGrant().getTmpObject("DemoProduct");
			String supId = params.getParameter("row_id");

			ObjectDB ord = getGrant().getTmpObject("DemoOrder");

			List<String[]> ps = prd.getTool().search(new JSONObject().put("demoPrdSupId", supId));
			for (String[] p : ps) {
				labels.put(prd.getFieldValue("demoPrdReference", p) + " (" + prd.getFieldValue("demoPrdName", p) + ")");

				JSONObject filter = new JSONObject().put("demoOrdPrdId", prd.getRowId(p)).put("demoOrdStatus", "D");
				counts.put((double)ord.getTool().count(filter));
				quantities.put(ord.getTool().sum("demoOrdQuantity", filter));
			}
		} catch (Exception e) {
			AppLog.error("Unable to process product stats", e, getGrant());
		}

		return new JSONObject().put("labels", labels).put("datasets", datasets);
	}
}
