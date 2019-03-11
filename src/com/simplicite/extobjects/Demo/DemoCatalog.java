package com.simplicite.extobjects.Demo;

import java.util.*;
import com.simplicite.util.*;
import com.simplicite.util.tools.*;
import org.json.*;

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
		//console.log("Method = " + method);
	
		JSONObject req = params.getJSONObject();
		//console.log("Request = " + req);
	
		JSONArray ps = new JSONArray();
		ObjectDB prd = this.getGrant().getTmpObject("DemoProduct");
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
