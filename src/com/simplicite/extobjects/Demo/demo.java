package com.simplicite.extobjects.Demo;

import com.simplicite.util.AppLog;
import com.simplicite.util.ExternalObject;
import com.simplicite.util.tools.HTMLTool;
import com.simplicite.util.tools.Parameters;
import com.simplicite.webapp.web.BootstrapWebPage;

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
		String render = getName() + ".render('" + HTMLTool.getRoot() + "', '" + HTMLTool.getResourceImageURL(this, "BANNER") + "');";
		try {
			if (isPublic()) { // Public page version (standalone Bootstrap page)
				BootstrapWebPage wp = new BootstrapWebPage(params.getRoot(), "Demo", false);
				wp.setFavicon(HTMLTool.getResourceIconURL(this, "FAVICON"));
				wp.appendAjax(true);
				wp.appendMustache();
				wp.appendJSInclude(HTMLTool.getResourceJSURL(this, "SCRIPT"));
				wp.appendCSSInclude(HTMLTool.getResourceCSSURL(this, "STYLES"));
				wp.append(HTMLTool.getResourceHTMLContent(this, "HTML"));
				wp.setReady(render);
				return wp.toString();
			} else { // Private page version
				addMustache();
				return javascript(render);
			}
		} catch (Exception e) {
			AppLog.error(getClass(), "display", null, e, getGrant());
			return e.getMessage();
		}
	}
}
