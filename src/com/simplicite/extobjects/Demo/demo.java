package com.simplicite.extobjects.Demo;

import java.util.*;
import com.simplicite.util.*;
import com.simplicite.util.tools.*;

/**
 * Demo web site
 */
public class demo extends ExternalObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Display method
	 * @param params Request parameters
	 */
	@Override
	public Object display(Parameters params) {
		try {
			/* TODO
			demo.display = function(params) {
				this.setDecoration(false);
				this.setPublic(true);
			
				var wp = new JQueryWebPage(params.getRoot(), "Demo");
				wp.setFavicon(HTMLTool.getResourceIconURL(this, "FAVICON"))
				wp.appendAjax(true);
				wp.appendMustache();
				wp.appendJSInclude(HTMLTool.getResourceJSURL(this, "SCRIPT"));
				wp.appendCSSInclude(HTMLTool.getResourceCSSURL(this, "STYLES"));
				wp.append(HTMLTool.getResourceHTMLContent(this, "HTML"));
				wp.setReady(this.getName() + ".render('" + HTMLTool.getRoot() + "', '" + HTMLTool.getResourceImageURL(this, "BANNER") + "');");
				return wp.toString();
			};
			*/
			return "";
		} catch (Exception e) {
			AppLog.error(getClass(), "display", null, e, getGrant());
			return e.getMessage();
		}
	}
}
