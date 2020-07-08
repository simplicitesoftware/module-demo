//----------------------------------------------------
// Client-side logic for customer business object
//----------------------------------------------------
var DemoClient = DemoClient || (function($) {
	// Display Google Map
	var _map = function() {
		var rowId = getFieldValue("row_id");
		if (rowId && rowId != "0") {
			var c = getFieldValue("demoCliCoords");
			if (c !== "") {
				var latlgn = c.replace(";", ",").split(",");
				var pos = new google.maps.LatLng(latlgn[0], latlgn[1]);
				var map = new google.maps.Map($("#client-map").show()[0], { center: pos, zoom: 13, mapTypeId: google.maps.MapTypeId.ROADMAP });
				var mrk = new google.maps.Marker({ position: pos, map: map });
				var name = $.escapeHTML(getFieldValue("demoCliFirstname") + " " + getFieldValue("demoCliLastname"));
				var addr = $.escapeHTML(getFieldValue("demoCliAddress1") + ", " + getFieldValue("demoCliZipCode") + " " + getFieldValue("demoCliCity") + ", " + getFieldValue("demoCliCountry"));
				var inf = new google.maps.InfoWindow({ content: "<div style=\"width: 200px; height: 75px;\"><b>" + name + "</b><br/>" + addr + "</div>" });
				google.maps.event.addListener(mrk, "click", function() { inf.open(map, mrk); });
			}
			return false;
		}
	};

	// Display Google Hangouts button
	var _hangout = function() {
		var email = getFieldValue("demoCliEmail");
		var invites = email ? [{ id: email, invite_type: "EMAIL" }]: [];
		gapi.hangout.render("hangout-button", { "render": "createhangout", "invites": invites });
	};

	// Responsive UI hook
	Simplicite.UI.hooks.DemoClient = function(o, cbk) {
		// Minimalistic backward compatibility with legacy UI getFieldValue for responsive UI
		window.getFieldValue = function(name) { return $ui.getUIField(null, o, name).ui.val(); };

		try {
			// Display Hangout buttons on form load
			o.locals.ui.form.onload = function() {
				try {
					if (typeof gapi === "undefined" || typeof gapi.hangout === "undefined") {
						$ui.loadScript({
							url: "https://apis.google.com/js/platform.js",
							onload: _hangout
						})
					} else {
						_hangout();
					}
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
		map: function() {
			if (typeof google === "undefined" || typeof google.maps === "undefined") {
				$ui.loadScript({
					url: Simplicite.GOOGLE_MAPS_JS_URL,
					onload: _map
				});
			} else {
				_map();
			}
		}
	}
})(jQuery);