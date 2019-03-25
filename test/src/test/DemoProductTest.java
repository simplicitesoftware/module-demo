package com.simplicite.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.simplicite.objects.Demo.DemoProduct;

/**
 * Local unit test example
 */
public class DemoProductTest {
	/** Increment test */
	@Test
	public void testIncrement() {
		assertEquals(10, DemoProduct.INCREMENT);
	}
}
