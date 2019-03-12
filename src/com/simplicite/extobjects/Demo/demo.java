package com.simplicite.extobjects.Demo;

import com.simplicite.util.AppLog;
import com.simplicite.util.ExternalObject;
import com.simplicite.util.tools.HTMLTool;
import com.simplicite.util.tools.Parameters;
import com.simplicite.webapp.web.JQueryWebPage;

/**
 * Web site (using Mustache(R) templating) custom frontend UI
 */
public class demo extends ExternalObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Display method
	 * @param params Request parameters
	 */
	@Override
	public Object display(Parameters params) {
		setDecoration(false);
		setPublic(true);
		try {
			JQueryWebPage wp = new JQueryWebPage(params.getRoot(), "Demo");
			wp.setFavicon(HTMLTool.getResourceIconURL(this, "FAVICON"));
			wp.appendAjax(true);
			wp.appendMustache();
			wp.appendJSInclude(HTMLTool.getResourceJSURL(this, "SCRIPT"));
			wp.appendCSSInclude(HTMLTool.getResourceCSSURL(this, "STYLES"));
			wp.append(HTMLTool.getResourceHTMLContent(this, "HTML"));
			wp.setReady(getName() + ".render('" + HTMLTool.getRoot() + "', '" + HTMLTool.getResourceImageURL(this, "BANNER") + "');");
			return wp.toString();
		} catch (Exception e) {
			AppLog.error(getClass(), "display", null, e, getGrant());
			return e.getMessage();
		}
	}
}
