package com.simplicite.extobjects.Demo;

import com.simplicite.util.tools.JSONTool;
import com.simplicite.util.tools.Parameters;

/**
 * Custom REST web services (suppliers and products only)
 */
public class demows extends com.simplicite.webapp.services.RESTMappedObjectsExternalObject {
	private static final long serialVersionUID = 1L;

	@Override
	protected void init(Parameters params) {
		setOpenAPIVersion(JSONTool.OPENAPI_OAS2);

		addObject("suppliers", "DemoSupplier");
		addField("suppliers", "code", "demoSupCode");
		addField("suppliers", "name", "demoSupName");

		addObject("products", "DemoProduct");
		addRefField("products", "suppliers", "supplierId", "demoPrdSupId", "Reference to supplier's row ID");
		addField("products", "supplierCode", "demoPrdSupId.demoSupCode");
		addField("products", "supplierName", "demoPrdSupId.demoSupName");
		addField("products", "reference", "demoPrdReference");
		addField("products", "nalme", "demoPrdName");
	}
}
