//-------------------------------------------------------
// Server side script for catalog API
//-------------------------------------------------------

DemoCatalog.display = function(params) {
	// ZZZ API external object ZZZ
	this.setJSONMIMEType();
	
	var method = params.getMethod();
	console.log("Method = " + method);
	
	var req = params.getJSONObject();
	console.log("Request = " + req);

	var ps = new JSONArray();
	var prd = this.getGrant().getTmpObject("DemoProduct");
	var rows = prd.search();
	for (var i = 0; i < rows.size(); i++) {
		prd.setValues(rows.get(i));
		var p = new JSONObject();
		p.put("ref", prd.getFieldValue("demoPrdReference"));
		p.put("name", prd.getFieldValue("demoPrdName"));
		ps.put(p);
	}
	
	var res = new JSONObject();
	if (req) res.put("request", req);
	res.put("response", ps);
	return res.toString();
};