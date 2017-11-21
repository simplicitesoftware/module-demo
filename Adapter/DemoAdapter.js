//------------------------------------
// Server side script for demo adapter
//------------------------------------

DemoAdapter.process = function() {
	var out = this.getOutputWriter();
	var log = this.getLogWriter();
	var err = this.getErrorWriter();

	this.appendLog(AppLogEvent.LEVEL_INFO, 0, "Begin as " + this.getGrant().getLogin());

	var sys = this.getGrant().getTmpObject("SystemParam");
	var syst = new BusinessObjectTool(sys);

	var n = 1;
	var l;
	var m = GroupDB.getModuleId("Application")
	while ((l = this.getInputReader().readLine()) !== null) {
		log.println("Line " + n + " = [" + l + "]");
		
		var v = l.split(";");
		for (var i = 0; i < v.length; i++) {
			log.println("\tItem " + i + " = [" + v[i] + "]");
		}

		sys.resetValues();
		sys.setFieldValue("sys_code", v[0]);
		sys.setFieldValue("sys_value", v[1]);
		sys.setFieldValue("row_module_id", m);
		try {
			syst.validateAndSave();
			out.println("<!-- " + l + " processed -->");
			this.appendLog(AppLogEvent.LEVEL_ERROR, n, "Line [" + l + "] processed");
		} catch (e) {
			err.println(l);
			out.println("<!-- " + l + " rejected -->");
			this.appendLog(AppLogEvent.LEVEL_INFO, n, "Line [" + l + "] rejected: " + e.javaException.getMessage());
		}
		n++;

	}

	this.appendLog(AppLogEvent.LEVEL_INFO, 0, "End, " + (n-1) + " lines processed");
};