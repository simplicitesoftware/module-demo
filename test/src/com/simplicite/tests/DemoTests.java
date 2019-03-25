package com.simplicite.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.simplicite.objects.Demo.DemoProduct;

/**
 * Local unit test example
 */
public class DemoTests {
	/** Increment test */
	@Test
	public void testProductIncrement() {
		assertEquals(10, DemoProduct.INCREMENT);
	}
}
