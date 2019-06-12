package com.simplicite.objects.Demo;

import com.simplicite.util.AppLog;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.ObjectField;
import com.simplicite.util.tools.GMapTool;
import com.simplicite.util.tools.GMapTool.Location;

/**
 * Customer business object
 */
public class DemoClient extends ObjectDB {
	private static final long serialVersionUID = 1L;

	/** Hook override: geolocate from address fields */
	@Override
	public String preSave() {
		if (!isBatchInstance()) try {
			ObjectField coords = getField("demoCliCoords");

			ObjectField a1 = getField("demoCliAddress1");
			ObjectField a2 = getField("demoCliAddress2");
			ObjectField a3 = getField("demoCliAddress3");
			ObjectField zc = getField("demoCliZipCode");
			ObjectField ci = getField("demoCliCity");
			ObjectField co = getField("demoCliCountry");

			if (coords.isEmpty() || a1.hasChanged() || a2.hasChanged() || a3.hasChanged() || zc.hasChanged() || ci.hasChanged() || co.hasChanged()) {
				String a = a1.getValue() + (a2.isEmpty() ? "" : ", " + a2.getValue()) + (a3.isEmpty() ? "" : ", " + a3.getValue()) + ", " + zc.getValue() + ", " + ci.getValue() + ", " + co.getValue();
				AppLog.info(getClass(), "preSave", "Try to geocode " + a, getGrant());
				Location c = new GMapTool(getGrant()).geocodeOne(a);
				AppLog.info(getClass(), "preSave", "Coordinates = " + c, getGrant());
				coords.setValue(c==null ?  "" : c.toString());
			}
		} catch (Exception e) {
			AppLog.error(getClass(), "preSave", null, e, getGrant());
		}
		return super.preSave();
	}

	/** Hook override: custom short label */
	@Override
	public String getUserKeyLabel(String[] row) {
		return row!=null
			? row[getFieldIndex("demoCliFirstname")] + " " + row[getFieldIndex("demoCliLastname")]
			: getFieldValue("demoCliFirstname") + " " + getFieldValue("demoCliLastname");
	}
}
