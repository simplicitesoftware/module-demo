//-------------------------------------------------------
// Server side script for order agenda
//-------------------------------------------------------

DemoOrderAgenda.display = function(params) {
	if (!this.getGrant().isResponsive()) {
		// Legacy version
		var embedded = params.getBooleanParameter("embedded");
		if (!embedded) {
			this.appendCSSIncludes(HTMLTool.fullcalendarCSS());
			this.appendJSIncludes(HTMLTool.fullcalendarJS(this.getGrant().getLang()));
		}
		return HTMLTool.border("<div id=\"ordercalendar\"></div>") +
			(embedded ? HTMLTool.cssIncludes(HTMLTool.fullcalendarCSS()) + HTMLTool.jsIncludes(HTMLTool.fullcalendarJS(this.getGrant().getLang())) : "") + 
			HTMLTool.jsBlock(
				(embedded ? "" : "onload_functions.push(function() {") +
					this.getName() + ".display(\"" + HTMLTool.getFormURL("DemoOrder", "agenda_DemoOrder", "ROWID", true) + "\");" +
				(embedded ? "" : "});")
			);
	} else {
		// Responsive version
		this.setDecoration(false);
		return "<div id=\"ordercalendar\" class=\"calendar\"></div>" +
			HTMLTool.jsBlock(
				"$ui.loadCalendar(function() {" +
					"$ui.loadScripts([{ url: \"" + HTMLTool.getResourceJSURL(this, "SCRIPT") + "\" }], function() {" +
						this.getName() + ".display();" +
					"});" +
				"});"
			);

	}
};