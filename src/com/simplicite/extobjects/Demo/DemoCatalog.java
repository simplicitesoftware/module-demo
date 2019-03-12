package com.simplicite.extobjects.Demo;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simplicite.objects.Demo.DemoProduct;
import com.simplicite.util.AppLog;
import com.simplicite.util.ExternalObject;
import com.simplicite.util.tools.Parameters;

/**
 * Catalog custom webservice
 */
public class DemoCatalog extends ExternalObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Display method
	 * @param params Request parameters
	 */
	@Override
	public Object display(Parameters params) {
		// ZZZ API external object ZZZ
		setJSONMIMEType();

		String method = params.getMethod();
		AppLog.info(getClass(), "display", "Method = " + method, getGrant());

		JSONObject req = params.getJSONObject();
		AppLog.info(getClass(), "display", "Request = " + req.toString(), getGrant());

		JSONArray ps = new JSONArray();
		DemoProduct prd = (DemoProduct)getGrant().getTmpObject("DemoProduct");
		List<String[]> rows = prd.search();
		for (int i = 0; i < rows.size(); i++) {
			prd.setValues(rows.get(i));
			JSONObject p = new JSONObject();
			p.put("ref", prd.getFieldValue("demoPrdReference"));
			p.put("name", prd.getFieldValue("demoPrdName"));
			ps.put(p);
		}

		JSONObject res = new JSONObject();
		if (req!=null) res.put("request", req);
		res.put("response", ps);
		return res.toString();
	}
}
