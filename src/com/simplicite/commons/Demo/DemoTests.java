package com.simplicite.commons.Demo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.simplicite.objects.Demo.DemoProduct;
import com.simplicite.util.Grant;
import com.simplicite.util.ObjectField;

/**
 * Server unit tests
 */
public class DemoTests {
	/** Increment test */
	@Test
	public void testIncrement() {
		DemoProduct prd = (DemoProduct)Grant.getSystemAdmin().getTmpObject("DemoProduct");
		prd.setValues(prd.search().get(0));
		ObjectField s = prd.getField("demoPrdStock");
		prd.increaseStock();
		int n = s.getInt(0);
		assertEquals(s.getInt(0), n + DemoProduct.INCREMENT);
	}
}
