package com.simplicite.extobjects.Demo;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simplicite.util.ObjectDB;
import com.simplicite.util.Tool;
import com.simplicite.util.exceptions.HTTPException;
import com.simplicite.util.tools.Parameters;

/**
 * Catalog custom API
 */
public class DemoCatalog extends com.simplicite.webapp.services.RESTServiceExternalObject {
	private static final long serialVersionUID = 1L;

	@Override
	public Object get(Parameters params) throws HTTPException {
		JSONArray res = new JSONArray();

		ObjectDB prd = getGrant().getTmpObject("DemoProduct");
		prd.resetFilters();
		prd.getField("demoPrdAvailable").setFilter(Tool.TRUE);
		List<String[]> rows = prd.search();
		for (int i = 0; i < rows.size(); i++) {
			prd.setValues(rows.get(i), false);

			res.put(new JSONObject()
				.put("ref", prd.getFieldValue("demoPrdReference"))
				.put("name", prd.getFieldValue("demoPrdName"))
			);
		}

		return res;
	}
}
