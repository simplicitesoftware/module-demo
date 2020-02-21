package com.simplicite.extobjects.Demo;

import com.simplicite.util.tools.JSONTool;
import com.simplicite.util.tools.Parameters;

/**
 * Custom REST web services (suppliers and products only)
 */
public class demows extends com.simplicite.webapp.services.RESTMappedObjectsExternalObject {
	private static final long serialVersionUID = 1L;

	@Override
	public void init(Parameters params) {
		setOpenAPISpec(JSONTool.OPENAPI_OAS2);
		setOpenAPIDesc("This is a **simplified** variant of the demo API for the following business objects:\n\n- Suppliers\n- Products\n- Orders");
		setOpenAPIVers("v4.0");

		addObject("suppliers", "DemoSupplier");
		addField("suppliers", "code", "demoSupCode");
		addField("suppliers", "name", "demoSupName");

		addObject("products", "DemoProduct");
		// **Without** embedded list
		//addRefField("products", "suppliers", "supplierId", "demoPrdSupId", "Reference to supplier's row ID");
		// **With** embedded list
		addRefField("products", "suppliers", "supplierId", "demoPrdSupId", "supplierProducts", true, "Reference to supplier's row ID");
		addField("products", "supplierCode", "demoPrdSupId.demoSupCode");
		addField("products", "supplierName", "demoPrdSupId.demoSupName");
		addField("products", "reference", "demoPrdReference");
		addField("products", "type", "demoPrdType");
		addField("products", "name", "demoPrdName");

		addObject("orders", "DemoOrder");
		// **Without** embedded list
		//addRefField("orders", "products", "productId", "demoOrdPrdId", "Reference to product's row ID");
		// **With** embedded list
		addRefField("orders", "products", "productId", "demoOrdPrdId", "productOrders", true, "Reference to product's row ID");
		addField("orders", "number", "demoOrdNumber");
		addField("orders", "date", "demoOrdDate");
		addField("orders", "status", "demoOrdStatus");
		addField("orders", "productReference", "demoOrdPrdId.demoPrdReference");
		addField("orders", "productName", "demoOrdPrdId.demoPrdName");
		addField("orders", "productType", "demoOrdPrdId.demoPrdType");
		addField("orders", "productSupplierCode", "demoOrdPrdId.demoPrdSupId.demoSupCode");
		addField("orders", "productSupplierName", "demoOrdPrdId.demoPrdSupId.demoSupName");
	}
}
