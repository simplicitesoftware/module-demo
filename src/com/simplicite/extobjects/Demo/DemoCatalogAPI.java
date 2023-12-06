package com.simplicite.extobjects.Demo;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simplicite.util.ObjectDB;
import com.simplicite.util.Tool;
import com.simplicite.util.exceptions.HTTPException;
import com.simplicite.util.tools.Parameters;
import com.simplicite.util.annotations.RESTService;
import com.simplicite.util.annotations.RESTServiceParam;
import com.simplicite.util.annotations.RESTServiceOperation;

/**
 * Product catalog custom API
 */
@RESTService(title = "catalog", desc = "Product catalog API", auth = false)
public class DemoCatalogAPI extends com.simplicite.webapp.services.RESTServiceExternalObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Product catalog service.
	 * @return JSON array of products
	 */
	@RESTServiceOperation(method = "get", desc = "Available products")
	public JSONObject catalog(
		@RESTServiceParam(name = "ref", desc = "Product reference", required = false)
		String ref
	) {
		JSONArray res = new JSONArray();

		ObjectDB prd = getGrant().getTmpObject("DemoProduct");
		prd.resetFilters();
		prd.getField("demoPrdAvailable").setFilter(Tool.TRUE);
		if (!Tool.isEmpty(ref))
			prd.getField("demoPrdReference").setFilter(ref);
			
		List<String[]> rows = prd.search();
		for (int i = 0; i < rows.size(); i++) {
			prd.setValues(rows.get(i), false);

			res.put(new JSONObject()
				.put("ref", prd.getFieldValue("demoPrdReference"))
				.put("name", prd.getFieldValue("demoPrdName"))
			);
		}

		return new JSONObject().put("count", res.length()).put("products", res);
	}
	
	@Override
	public Object get(Parameters params) throws HTTPException {
		if (params.getParameter("openapi") != null)
			return openapi();
		return catalog(params.getParameter("ref"));
	}
}
