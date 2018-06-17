//-------------------------------------------------------
// Server side script for order agenda
//-------------------------------------------------------

DemoOrderAgenda.display = function(params) {
	if (!this.getGrant().isResponsive()) { // Legacy version
		var embedded = params.getBooleanParameter("embedded");
		if (!embedded) {
			this.appendCSSIncludes(HTMLTool.fullcalendarCSS());
			this.appendJSIncludes(HTMLTool.fullcalendarJS(this.getGrant().getLang()));
		}
		var js = this.getName() + ".render(\"" + HTMLTool.getFormURL("DemoOrder", "agenda_DemoOrder", "ROWID", true) + "\");";
		return (embedded
				? HTMLTool.cssIncludes(HTMLTool.fullcalendarCSS()) +
				  HTMLTool.jsIncludes(HTMLTool.fullcalendarJS(this.getGrant().getLang()))
				: "") + HTMLTool.jsBlock(embedded ? js : HTMLTool.jsOnload(js));
	} else // Responsive version
		return this.javascript("$ui.loadCalendar(function() { " + this.getName() + ".render(); });");
};