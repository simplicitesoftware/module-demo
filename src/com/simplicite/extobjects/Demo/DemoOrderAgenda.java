package com.simplicite.extobjects.Demo;

import com.simplicite.util.AppLog;
import com.simplicite.util.ExternalObject;
import com.simplicite.util.tools.HTMLTool;
import com.simplicite.util.tools.Parameters;
import com.simplicite.webapp.Navigator;

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
			if (!getGrant().isResponsive()) { // Legacy version
				boolean embedded = params.getBooleanParameter("embedded");
				if (!embedded) {
					appendCSSIncludes(HTMLTool.fullcalendarCSS());
					appendJSIncludes(HTMLTool.fullcalendarJS(getGrant().getLang()));
				}
				String js = getName() + ".render(\"" + HTMLTool.getFormURL("DemoOrder", "agenda_DemoOrder", "ROWID", "nav="+Navigator.ADD) + "\");";
				return (embedded
						? HTMLTool.cssIncludes(HTMLTool.fullcalendarCSS()) +
						  HTMLTool.jsIncludes(HTMLTool.fullcalendarJS(getGrant().getLang()))
						: "") + HTMLTool.jsBlock(embedded ? js : HTMLTool.jsOnload(js));
			} else // Responsive version
				return javascript("$ui.loadCalendar(function() { " + getName() + ".render(); });");
		} catch (Exception e) {
			AppLog.error(getClass(), "display", null, e, getGrant());
			return e.getMessage();
		}
	}
}
