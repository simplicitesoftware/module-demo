package com.simplicite.extobjects.Demo;

import com.simplicite.util.tools.Parameters;

/**
 * Order agenda custom UI component
 */
public class DemoOrderAgenda extends com.simplicite.webapp.web.ResponsiveExternalObject {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getRenderStatement(Parameters params) {
		return "$ui.loadCalendar(function() { " + super.getRenderStatement(params) + " });";
	}
}