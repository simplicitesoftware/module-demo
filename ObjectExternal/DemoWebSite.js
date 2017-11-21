//-------------------------------------------------------
// Server side script for public web site external object
//-------------------------------------------------------

DemoWebSite.display = function(params) {
	// ZZZ IMPORTANT ZZZ Standalone page
	this.setDecoration(false);
	
	// Default Bootstrap page
	var wp = new BootstrapWebPage(params.getRoot(), "Demo web site", false);
	
	// Themed Boostrap page with custom styles
	//var wp = new BootstrapWebPage(params.getRoot(), "Demo web site", "dark");
	//wp.appendCSS(".navbar-inverse { background-color: #464545; } .navbar-inverse .navbar-nav>li>a:hover, .navbar-inverse .navbar-nav>li>a:focus { color: #A0A0A0; }");

	wp.appendJS("var ROOT = \"" + wp.getRoot() + "\";");
	wp.appendAjax();
	wp.appendFullcalendar();
	wp.appendJSInclude(HTMLPage.getResourceJSURL(this, "SCRIPT"));
	wp.setFavicon(HTMLPage.getResourceIconURL(this, "FAVICON"));
	wp.setReady("DemoWebSiteBootstrap.init(\"" + HTMLPage.getResourceImageURL(this, "LOADING") + "\");");
	
	var m = new LinkedHashMap();
	m.put("menu-catalog", "Catalog");
	m.put("menu-orders", "My orders");
	m.put("menu-messages", "My messages");
	m.put("menu-agenda", "My deliveries");
	m.put("menu-news", "News");
	wp.setMenu("menu-brand", "<img src=\"" + HTMLPage.getResourceImageURL(this, "LOGO") + "\"/>", m, true, false, true);
	//wp.setSideMenu(m);

	wp.appendHTML("<div id=\"header\" class=\"well\" style=\"display: none;\"></div>");
	wp.appendHTML("<div id=\"info\" class=\"alert alert-success\" style=\"display: none;\"></div>");
	wp.appendHTML("<div id=\"warning\" class=\"alert alert-warning\" style=\"display: none;\"></div>");
	wp.appendHTML("<div id=\"error\" class=\"alert alert-danger\" style=\"display: none;\"></div>");
	wp.appendHTML("<div id=\"main\"></div>");
	wp.appendHTML("<div id=\"footer\">&copy; Simplicit&eacute; Software</div>");

	return wp.toString();
};