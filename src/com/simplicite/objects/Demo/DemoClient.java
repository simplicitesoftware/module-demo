package com.simplicite.objects.Demo;

import java.util.List;
import java.util.ArrayList;

import com.simplicite.util.AppLog;
import com.simplicite.util.Message;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.ObjectField;
import com.simplicite.util.tools.GMapTool;
import com.simplicite.util.tools.GMapTool.Location;
import com.simplicite.util.tools.PhoneNumTool;

/**
 * Customer business object
 */
public class DemoClient extends ObjectDB {
	private static final long serialVersionUID = 1L;

	@Override
	public void postLoad() {
		if (getGrant().hasResponsibility("DEMO_WEBSITE"))
			// Hide all fields except name fields from website
			for (ObjectField f : getFields())
				if (!f.isTechnicalField() && !"demoCliFirstname".equals(f.getName()) && !"demoCliLastname".equals(f.getName()))
					f.setVisibility(ObjectField.VIS_FORBIDDEN);
	}

	@Override
	public List<String> postValidate() {
		List<String> msgs = new ArrayList<>();

		// Validate phone numbers
		try {
			PhoneNumTool pnt = new PhoneNumTool();
			String[] pns = { "demoCliHomePhone", "demoCliWorkPhone", "demoCliMobilePhone", "demoCliFax" };
			for (String pn : pns) {
				ObjectField f = getField(pn);
				if (!f.isEmpty() && !pnt.isValid(f.getValue()))
					msgs.add(Message.formatError("ERR_DEMO_INVALID_PHONE_NUMBER:" + f.getDisplay(), null, f.getName()));
			}
		} catch (Exception e) {
			AppLog.error(null, e, getGrant());
		}

		return msgs;
	}

	/** Hook override: geo-locate from address fields */
	@Override
	public String preSave() {
		if (isMainInstance()) { // Only done for the main UI instance
			try {
				// Geocode address fields
				ObjectField coords = getField("demoCliCoords");
				ObjectField a1 = getField("demoCliAddress1");
				ObjectField a2 = getField("demoCliAddress2");
				ObjectField zc = getField("demoCliZipCode");
				ObjectField ci = getField("demoCliCity");
				ObjectField co = getField("demoCliCountry");

				if (coords.isEmpty() || a1.hasChanged() || a2.hasChanged() || zc.hasChanged() || ci.hasChanged() || co.hasChanged()) {
					String a = a1.getValue() + (a2.isEmpty() ? "" : ", " + a2.getValue()) + ", " + zc.getValue() + ", " + ci.getValue() + ", " + co.getValue();
					AppLog.info("Try to geocode " + a, getGrant());
					Location c = new GMapTool(getGrant()).geocodeOne(a);
					AppLog.info("Coordinates = " + c, getGrant());
					coords.setValue(c == null ?  "" : c.toString());
				}
			} catch (Exception e) {
				AppLog.warning(null, e, getGrant());
			}
		}
		return super.preSave();
	}

	/** Hook override: custom short label */
	@Override
	public String getUserKeyLabel(String[] row) {
		return getFieldValue("demoCliFirstname", row) + " " + getFieldValue("demoCliLastname", row);
	}
}
