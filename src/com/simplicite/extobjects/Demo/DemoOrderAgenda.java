package com.simplicite.extobjects.Demo;

import java.util.*;
import com.simplicite.util.*;
import com.simplicite.util.tools.*;

/**
 * Order agenda
 */
public class DemoOrderAgenda extends ExternalObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Display method
	 * @param params Request parameters
	 */
	@Override
	public Object display(Parameters params) {
		try {
			/*
			if (!getGrant().isResponsive()) { // Legacy version
				var embedded = params.getBooleanParameter("embedded");
				if (!embedded) {
					appendCSSIncludes(HTMLTool.fullcalendarCSS());
					appendJSIncludes(HTMLTool.fullcalendarJS(getGrant().getLang()));
				}
				var js = getName() + ".render(\"" + HTMLTool.getFormURL("DemoOrder", "agenda_DemoOrder", "ROWID", true) + "\");";
				return (embedded
						? HTMLTool.cssIncludes(HTMLTool.fullcalendarCSS()) +
						  HTMLTool.jsIncludes(HTMLTool.fullcalendarJS(getGrant().getLang()))
						: "") + HTMLTool.jsBlock(embedded ? js : HTMLTool.jsOnload(js));
			} else // Responsive version
				return javascript("$ui.loadCalendar(function() { " + getName() + ".render(); });");
			*/
			return "";
		} catch (Exception e) {
			AppLog.error(getClass(), "display", null, e, getGrant());
			return e.getMessage();
		}
	}
}
