package com.simplicite.extobjects.Demo;

import com.simplicite.util.ExternalObject;
import com.simplicite.util.tools.Parameters;

/**
 * Place new order custom UI component
 */
public class DemoPlaceNewOrder extends ExternalObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Display method
	 * @param params Request parameters
	 */
	@Override
	public Object display(Parameters params) {
		return javascript(getName() + ".render();");
	}
}
