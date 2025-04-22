package com.simplicite.extobjects.Demo;

import org.json.JSONObject;

import com.simplicite.util.AppLog;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.tools.Parameters;

/**
 * Counters
 */
public class DemoCounters extends com.simplicite.webapp.web.ResponsiveExternalObject {
	private static final long serialVersionUID = 1L;

	@Override
	public JSONObject data(Parameters params) {
		ObjectDB cli = getGrant().getTmpObject("DemoClient");
		ObjectDB ord = getGrant().getTmpObject("DemoOrder");
		ObjectDB ctc = getGrant().getTmpObject("DemoContact");
		JSONObject data = new JSONObject()
			.put("clients", cli.getPluralLabel())
			.put("orders", ord.getPluralLabel())
			.put("contacts", ctc.getPluralLabel());
		try {
			data.put("nb_clients", cli.getTool().count(new JSONObject()));
			data.put("nb_orders", ord.getTool().count(new JSONObject()));
			data.put("nb_contacts", ctc.getTool().count(new JSONObject()));
		} catch (Exception e) {
			AppLog.error("Unable to count", e, getGrant());
		}
		return data;
	}
}
