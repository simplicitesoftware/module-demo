package com.simplicite.tests.Demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.HashMap;

import org.junit.Test;

import com.simplicite.objects.Demo.DemoProduct;
import com.simplicite.util.Grant;
import com.simplicite.util.ObjectField;

/**
 * Demo unit tests
 */
public class DemoTests {
	/** Increment test */
	@Test
	public void testIncrement() {
		try {
			DemoProduct prd = (DemoProduct)Grant.getSystemAdmin().getTmpObject("DemoProduct");
			prd.setValues(prd.search().get(0), true);
			ObjectField s = prd.getField("demoPrdStock");
			int n = s.getInt(0);
			Map<String, String> params = new HashMap<>();
			params.put("demoPrdIncrement", String.valueOf(DemoProduct.DEFAULT_INCREMENT));
			prd.increaseStock(params);
			assertEquals(n + DemoProduct.DEFAULT_INCREMENT, s.getInt(0));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
