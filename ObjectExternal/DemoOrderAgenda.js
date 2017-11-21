//-------------------------------------------------------
// Server side script for order agenda
//-------------------------------------------------------

DemoOrderAgenda.display = function(params) {
	this.appendCSSIncludes(HTMLTool.fullcalendarCSS());
	this.appendJSIncludes(HTMLTool.fullcalendarJS(this.getGrant().getLang()));
	return HTMLTool.border("<div id=\"ordercalendar\"></div>") +
		HTMLTool.jsBlock(
			"onload_functions.push(function() {" +
				this.getName() + ".display(\"" + HTMLTool.getFormURL("DemoOrder", "agenda_DemoOrder", "ROWID", true) + "\");" +
			"});"
		);
};