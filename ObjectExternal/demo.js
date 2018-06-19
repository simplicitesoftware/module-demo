demo.display = function(params) {
	this.setDecoration(false);
	this.setPublic(true);

	var wp = new JQueryWebPage(params.getRoot(), "Demo");
	wp.setFavicon(HTMLTool.getResourceIconURL(this, "FAVICON"))
	wp.appendAjax();
	wp.appendMustache();
	wp.appendJSInclude(HTMLTool.getResourceJSURL(this, "SCRIPT"));
	wp.appendCSSInclude(HTMLTool.getResourceCSSURL(this, "STYLES"));
	wp.append(HTMLTool.getResourceHTMLContent(this, "HTML"));
	wp.setReady(this.getName() + ".render('" + HTMLTool.getRoot() + "', '" + HTMLTool.getResourceImageURL(this, "BANNER") + "');");
	return wp.toString();
};