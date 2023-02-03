package com.simplicite.tests.Demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.HashMap;

import org.junit.Test;

import com.simplicite.objects.Demo.DemoProduct;

import com.simplicite.util.AppLog;
import com.simplicite.util.Grant;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.ObjectField;

/**
 * Demo unit tests
 */
public class DemoTests {
	/** Product's stock increment test */
	/*@Test
	public void testIncrement() {
		try {
			ObjectDB prd = Grant.getSystemAdmin().getObject("test_DemoProduct", "DemoProduct");
			prd.setValues(prd.search().get(0), true);

			ObjectField s = prd.getField(DemoProduct.STOCK_FIELDNAME);
			int n = s.getInt(0);

			Map<String, String> params = new HashMap<>();
			params.put(DemoProduct.INCREMENT_FIELDNAME, String.valueOf(DemoProduct.DEFAULT_INCREMENT));
			prd.invokeAction("", params);

			assertEquals(n + DemoProduct.DEFAULT_INCREMENT, s.getInt(0));
		} catch (Throwable e) {
			fail(e.getMessage());
		}
	}*/
	
	/** Create order test */
	@Test
	public void testCreateOrder() {
		Grant sys = Grant.getSystemAdmin();
		try {
			AppLog.info("Order creation test...", sys);

			int quantity = 5;

			ObjectDB prd = sys.getObject("test_DemoProduct", "DemoProduct");
			prd.setValues(prd.search().get(0), true); // Select the first product
			ObjectField ps = prd.getField(DemoProduct.STOCK_FIELDNAME);
			int stock = ps.getInt(-1);
			AppLog.info("Product " + prd.getFieldValue("demoPrdReference") + " stock = " + stock, sys);

			if (stock < quantity) {
				ps.setValue(stock + quantity);
				prd.getTool().validateAndSave(); // Update product
				stock = ps.getInt(-1);
				AppLog.info("Product stock increased to = " + stock, sys);
			}
			assertTrue(stock > quantity);

			ObjectDB cli = sys.getObject("test_DemoClient", "DemoClient");
			cli.setValues(cli.search().get(0), true); // Select the first customer
			AppLog.info("Customer " + cli.getFieldValue("demoCliCode"), sys);
		
			ObjectDB ord = sys.getObject("test_DemoOrder", "DemoOrder");
			AppLog.info("Using " + ord.getDisplay(), sys);
			ord.resetValues(true);
			ObjectField q = ord.getField("demoOrdQuantity");
			assertEquals(q.getDefaultValue(), q.getValue()); // Check the default quantity
			ObjectField s = ord.getField("demoOrdStatus");
			assertEquals("P", s.getValue()); // Check the default status
			
			ObjectField p = ord.getField("demoOrdPrdId");
			p.setValue(prd.getRowId());
			ord.populateForeignKey(p.getName(), prd.getRowId()); // ZZZ populate the product's fields on the order
			ObjectField c = ord.getField("demoOrdCliId");
			c.setValue(cli.getRowId());
			ord.populateForeignKey(c.getName(), cli.getRowId()); // ZZZ populate the customer's fields on the order
			q.setValue(quantity);
			ord.getTool().validateAndSave(); // Create order
			String n = ord.getFieldValue("demoOrdNumber");
			AppLog.info("Created order #" + n + " for " + quantity + " quantity", sys);

			ord.select(ord.getRowId()); // Reload the order's record
			assertEquals(quantity, q.getInt(-1)); // Check the quantity
			assertEquals("P", s.getValue()); // Check the status value
			ObjectField d = ord.getField("demoOrdDeliveryDate");
			
			s.setOldValue(s.getValue());			
			s.setValue("V"); // Validated
			ord.getTool().validateAndSave();
			AppLog.info("Validated order #" + n, sys);

			ord.select(ord.getRowId()); // Reload the order's record
			assertEquals("V", s.getValue()); // Check the status value
			assertFalse(p.isUpdatable()); // Check that the product is not updatable anymore
			assertFalse(c.isUpdatable()); // Check that the customer is not updatable anymore
			assertFalse(q.isUpdatable()); // Check that the quantity is not updatable anymore
			assertTrue(d.isUpdatable()); // Check that the delivery date is still updatable

			s.setOldValue(s.getValue());			
			s.setValue("D"); // Delivered
			ord.getTool().validateAndSave();
			AppLog.info("Delivered order #" + n, sys);

			ord.select(ord.getRowId()); // Reload the order's record
			assertEquals("D", s.getValue()); // Check the status value
			assertFalse(d.isUpdatable()); // Check that the delivery date is not updatable anymore
			
			prd.select(prd.getRowId()); // Reload the product's record
			AppLog.info("Product stock was decreased to = " + ps.getInt(-1), sys);
			assertEquals(ps.getInt(-1), stock - quantity);

			ord.getTool().delete();
			AppLog.info("Deleted order #" + n, sys);

			AppLog.info("Success", sys);
		} catch (Throwable e) {
			AppLog.error("Failure:" + e.getMessage(), e, sys);
			fail(e.getMessage());
		}
	}
}
