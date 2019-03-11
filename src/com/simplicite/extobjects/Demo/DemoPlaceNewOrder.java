package com.simplicite.extobjects.Demo;

import java.util.*;
import com.simplicite.util.*;
import com.simplicite.util.tools.*;

/**
 * Place new order
 */
public class DemoPlaceNewOrder extends ExternalObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Display method
	 * @param params Request parameters
	 */
	@Override
	public Object display(Parameters params) {
		return javascript(this.getName() + ".render();");
	}
}
