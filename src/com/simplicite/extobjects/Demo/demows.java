package com.simplicite.extobjects.Demo;

import com.simplicite.util.tools.JSONTool;
import com.simplicite.util.tools.Parameters;

/**
 * Custom REST web services (suppliers and products only)
 */
public class demows extends com.simplicite.webapp.services.RESTMappedObjectsExternalObject {
	private static final long serialVersionUID = 1L;

	public static final String SUPPLIERS = "suppliers";
	public static final String PRODUCTS = "products";
	public static final String ORDERS = "orders";
	public static final String STATS = "stats";

	@Override
	public void init(Parameters params) {
		setOpenAPISpec(JSONTool.OPENAPI_OAS2);
		setOpenAPIDesc("This is a **simplified** variant of the demo API for the following business objects:\n\n- Suppliers\n- Products\n- Orders");
		setOpenAPIVers("v4.0");
		/*addOperationDesc(null, OPERATION_PING, "This is the **ping** operation");*/

		addObject(SUPPLIERS, "DemoSupplier");
		addField(SUPPLIERS, "code", "demoSupCode");
		addField(SUPPLIERS, "name", "demoSupName");

		addObject(PRODUCTS, "DemoProduct");
		// **Without** embedded list
		//addRefField(prd, sup, "supplierId", "demoPrdSupId", "Reference to supplier's row ID");
		// **With** embedded list
		addRefField(PRODUCTS, SUPPLIERS, "supplierId", "demoPrdSupId", "supplierProducts", true, "Reference to supplier's row ID");
		addField(PRODUCTS, "supplierCode", "demoPrdSupId.demoSupCode");
		addField(PRODUCTS, "supplierName", "demoPrdSupId.demoSupName");
		addField(PRODUCTS, "reference", "demoPrdReference");
		addField(PRODUCTS, "type", "demoPrdType");
		addField(PRODUCTS, "name", "demoPrdName");

		addObject(ORDERS, "DemoOrder");
		// **Without** embedded list
		//addRefField(ord, prd, "productId", "demoOrdPrdId", "Reference to product's row ID");
		// **With** embedded list
		addRefField(ORDERS, PRODUCTS, "productId", "demoOrdPrdId", "productOrders", true, "Reference to product's row ID");
		addField(ORDERS, "number", "demoOrdNumber");
		addField(ORDERS, "date", "demoOrdDate");
		addField(ORDERS, "status", "demoOrdStatus");
		addField(ORDERS, "productReference", "demoOrdPrdId.demoPrdReference");
		addField(ORDERS, "productName", "demoOrdPrdId.demoPrdName");
		addField(ORDERS, "productType", "demoOrdPrdId.demoPrdType");
		addField(ORDERS, "productSupplierCode", "demoOrdPrdId.demoPrdSupId.demoSupCode");
		addField(ORDERS, "productSupplierName", "demoOrdPrdId.demoPrdSupId.demoSupName");
		/*addOperationDesc("products", OPERATION_SEARCH, "This is the **search** operation for the _product_ business object");
		addOperationDesc("products", OPERATION_GET, "This is the **get** operation for the _product_ business object");
		addOperationDesc("products", OPERATION_CREATE, "This is the **create** operation for the _product_ business object");
		addOperationDesc("products", OPERATION_UPDATE, "This is the **update** operation for the _product_ business object");
		addOperationDesc("products", OPERATION_DELETE, "This is the **delete** operation for the _product_ business object");*/
		
		addObject(STATS, "DemoStats", DESC_HIDDEN_FROM_SCHEMA);
		addField(STATS, "status", "demoOrdStatus");
		addField(STATS, "count", "demoStsCount");
		addField(STATS, "quantity", "demoStsQuantity");
		addField(STATS, "total", "demoStsTotal");
	}
}
