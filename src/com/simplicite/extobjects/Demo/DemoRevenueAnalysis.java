package com.simplicite.extobjects.Demo;

import java.util.*;
import com.simplicite.util.*;
import com.simplicite.util.tools.*;
import org.json.*;

/**
 * External object ExampleDevExtreme
 */
public class DemoRevenueAnalysis extends ExternalObject {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Display method
	 * @param params Request parameters
	 */
	@Override
	public Object display(Parameters params) {
		try {
			appendCSSIncludes(new String[] {
				"https://cdn3.devexpress.com/jslib/18.1.3/css/dx.common.css",
				"https://cdn3.devexpress.com/jslib/18.1.3/css/dx.light.css",
				getResourceJSURL("STYLES")
			});
			appendJSIncludes(new String[] {
				"https://cdn3.devexpress.com/jslib/18.1.3/js/dx.all.js",
				getResourceJSURL("SCRIPT")
			});
			
			String inst = params.getParameter("inst");
			ObjectDB obj = getGrant().getObject(inst, "DemoOrder");
			String fields = "[" +
				"{ caption: '" + Tool.toJS(obj.getField("demoOrdPrdId.demoPrdSupId.demoSupCode").getDisplay()) + "', width: 150, dataField: 'demoOrdPrdId__demoPrdSupId__demoSupCode', area: 'row', sortBySummaryField: 'Total' }," +
				"{ caption: '" + Tool.toJS(obj.getField("demoOrdPrdId.demoPrdReference").getDisplay()) + "', dataField: 'demoOrdPrdId__demoPrdReference', width: 100, area: 'row' }," +
				"{ caption: '" + Tool.toJS(obj.getField("demoOrdDate").getDisplay()) + "', dataField: 'demoOrdDate', dataType: 'date', area: 'column' }," +
				"{ caption: 'Quantity', dataField: 'demoOrdQuantity', dataType: 'number', summaryType: 'sum', area: 'data' }," +
				"{ caption: 'Total', dataField: 'demoOrdTotal', dataType: 'number', summaryType: 'sum', format: 'currency', area: 'data' }" +
			"]";
			//obj.search(false); // Re-apply current instance filters for a non paginated search
			Crosstab c = obj.getCrosstab("DemoOrder-TC2");
			c.build(obj);
			String data = c.toJSONCubes();
			c.clear();
			
			return
				"<div class=\"dx-viewport demo-container\">" +
        			"<div id=\"pivotgrid-demo\">" +
        				"<div id=\"pivotgrid-chart\"></div>" +
        				"<div id=\"pivotgrid\">" + getGrant().T("LOADING") + "</div>" +
        			"</div>" +
    			"</div>" +
    			HTMLTool.jsBlock(
    				"onload_functions.push(function() {" +
    					"grid(" + fields + ", " + data + ", null, "+ Tool.getCurrentYear() +");" +
    				"});");
		} catch (Exception e) {
			AppLog.error(getClass(), "display", null, e, getGrant());
			return e.getMessage();
		}
	}
}