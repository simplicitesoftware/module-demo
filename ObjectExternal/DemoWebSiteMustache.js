DemoWebSiteMustache.display = function(params) {
	this.setDecoration(false);
	this.setPublic(true);

	var wp = new JQueryWebPage(params.getRoot(), "Demo web site");

	wp.appendAjax();
	wp.appendMustache();
	wp.appendCSSInclude(HTMLTool.getResourceCSSURL(this, "STYLES"));
	wp.setFavicon(HTMLTool.getResourceIconURL(this, "FAVICON"))
	wp.append(HTMLTool.getResourceHTMLContent(this, "TEMPLATE"));

	wp.setReady(
		"var app = new Simplicite.Ajax('" + HTMLTool.getRoot() + "', 'api', 'website', 'simplicite');" +
		"var prd = app.getBusinessObject('DemoProduct');" +
		"prd.toFixed = function() { return function(n, r) { return parseFloat(r(n)).toFixed(2); } };" + // Mustache rendering for decimal
		"prd.bannerURL = \"" + HTMLTool.getResourceImageURL(this, "BANNER") + "\";" + // Image banner URL
		"prd.search(function() { var t = $(\"#template\"); t.html(Mustache.render(t.html(), prd)).show(); }, {}, { inlineDocs: true });"
	);
	
	return wp.toString();
};