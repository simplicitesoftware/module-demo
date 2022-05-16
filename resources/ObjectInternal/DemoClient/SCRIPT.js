//----------------------------------------------------
// Client-side logic for customer business object
//----------------------------------------------------
var DemoClient = DemoClient || (function() {
	// UI hook
	Simplicite.UI.hooks.DemoClient = function(o, cbk) {
		function _val(name) {
			return $ui.getUIField(null, o, name).ui.val();
		}

		try {
			// Display Hangout buttons on form load
			o.locals.ui.form.onload = function() {
				try {
					// Display Google Map
					_map = function() {
						const rowId = _val("row_id");
						if (rowId && rowId != "0") {
							const c = _val("demoCliCoords");
							if (c !== "") {
								const latlgn = c.replace(";", ",").split(",");
								const pos = new google.maps.LatLng(latlgn[0], latlgn[1]);
								const map = new google.maps.Map($("#client-map").show()[0], { center: pos, zoom: 13, mapTypeId: google.maps.MapTypeId.ROADMAP });
								const mrk = new google.maps.Marker({ position: pos, map: map });
								const name = $.escapeHTML(_val("demoCliFirstname") + " " + _val("demoCliLastname"));
								const addr = $.escapeHTML(_val("demoCliAddress1") + ", " + _val("demoCliZipCode") + " " + _val("demoCliCity") + ", " + _val("demoCliCountry"));
								const inf = new google.maps.InfoWindow({ content: "<div style=\"width: 200px; height: 75px;\"><b>" + name + "</b><br/>" + addr + "</div>" });
								google.maps.event.addListener(mrk, "click", () => { inf.open(map, mrk); });
							}
							return false;
						}
					};
				} catch(el) {
					console.error(el);
				}
			}
		} catch(e) {
			console.error(e.message);
		} finally {
			cbk && cbk();
		}
	};

	return {
		// Action function
		map: () => {
			$ui.loadScript({
				url: Simplicite.GOOGLE_MAPS_JS_URL,
				onload: _map
			});
		}
	}
})();